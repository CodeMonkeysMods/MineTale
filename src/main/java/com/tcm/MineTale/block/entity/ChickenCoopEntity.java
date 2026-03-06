package com.tcm.MineTale.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueOutput.TypedOutputList;
import net.minecraft.world.level.storage.ValueOutput.ValueOutputList;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.RecordBuilder;
import com.tcm.MineTale.registry.ModBlockEntities;

public class ChickenCoopEntity extends BlockEntity {
    private final List<CompoundTag> storedChickensNbt = new ArrayList<>();
    private boolean isNightMode = false;
    private int eggCount = 0;
    private int eggsLaidThisNight = 0;
    private static final int MAX_EGGS = 16; /**
     * Creates a ChickenCoopEntity for the given block position and block state.
     *
     * @param pos   the block position for this block entity
     * @param state the block state associated with this block entity
     */

    public ChickenCoopEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHICKEN_COOP_BE, pos, state);
    }

    /**
     * Update tick handler that transitions the coop between day and night, manages stored chickens and their release, and controls egg production.
     *
     * <p>Runs only on the server: when time enters the night range (game time 13000–22999) it collects nearby chickens into the coop and resets the per-night lay counter; when time exits that range it releases stored chickens. While in night mode and with chickens stored, the coop may produce eggs subject to constraints: total eggs are limited by MAX_EGGS, the number of eggs laid during the current night is limited to the number of chickens stored, and each tick there is a 1-in-1000 chance to lay a single egg (which increments the egg count and per-night counter and plays the chicken egg sound).</p>
     *
     * @param level the world in which the coop exists (must be server-side for effects to occur)
     * @param pos   the block position of the coop
     * @param state the block state of the coop
     * @param be    the ChickenCoopEntity instance to update
     */
    public static void tick(Level level, BlockPos pos, BlockState state, ChickenCoopEntity be) {
        if (level.isClientSide()) return;

        long time = level.getDayTime() % 24000;
        boolean isLate = time >= 13000 && time < 23000; 

        // Transition to Night
        if (isLate && !be.isNightMode) {
            be.collectChickens((ServerLevel) level, pos);
            be.isNightMode = true;
            be.eggsLaidThisNight = 0; // Reset the counter for the new night
            be.setChanged();
        } 
        // Transition to Day
        else if (!isLate && be.isNightMode) {
            be.releaseChickens((ServerLevel) level, pos);
            be.isNightMode = false;
            be.setChanged();
        }

        // Egg Laying Logic
        if (be.isNightMode && !be.storedChickensNbt.isEmpty()) {
            int chickensInside = be.storedChickensNbt.size();

            // Condition 1: Total coop storage isn't full (MAX_EGGS = 16)
            // Condition 2: This specific night hasn't exceeded the chicken count
            if (be.eggCount < MAX_EGGS && be.eggsLaidThisNight < chickensInside) {
                
                // 1 in 1000 chance per tick is balanced for a full night
                if (level.random.nextInt(1000) == 0) {
                    be.eggCount++;
                    be.eggsLaidThisNight++; // This ensures this specific chicken is "done" for the night
                    be.setChanged();
                    
                    level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.5f, 1.2f);
                }
            }
        }
    }

    /**
     * Spawn a burst of feather particles centred on the given block position.
     *
     * Spawns `count` feather particles with a small spread and velocity to simulate feathers
     * drifting from the block's centre.
     *
     * @param level the server level to emit the particles in
     * @param pos   the block position at whose centre the particles will appear
     * @param count the number of feather particles to spawn
     */
    private void spawnFeatherParticles(ServerLevel level, BlockPos pos, int count) {
        // Define the particle type using the Feather item texture
        ItemParticleOption particleData = 
            new ItemParticleOption(
                ParticleTypes.ITEM, 
                new ItemStack(Items.FEATHER)
            );

        // Spawn the particles
        // Parameters: particle, pos.x, pos.y, pos.z, count, speedX, speedY, speedZ, velocityScale
        level.sendParticles(particleData, 
            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 
            count,   // amount of feathers
            0.3, 0.3, 0.3, // spread (delta)
            0.15     // speed/velocity
        );
    }

    /**
     * Serialises the coop's persistent state into the provided ValueOutput.
     *
     * Saves the current egg count, the number of eggs laid this night, the night-mode flag,
     * and the stored chickens as a list under the key "StoredChickens".
     *
     * @param valueOutput destination used to write the entity's persisted fields
     */
    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.putInt("EggCount", this.eggCount);
        valueOutput.putInt("EggsLaidThisNight", this.eggsLaidThisNight); // Added
        valueOutput.putBoolean("IsNightMode", this.isNightMode);
        
        ValueOutput.TypedOutputList<CompoundTag> chickenList = valueOutput.list("StoredChickens", CompoundTag.CODEC);
        for (CompoundTag chickenNbt : storedChickensNbt) {
            chickenList.add(chickenNbt);
        }
    }

    /**
     * Restores the chicken coop's persisted state from the given input.
     *
     * Reads and restores eggCount, eggsLaidThisNight and isNightMode, clears any in-memory stored chicken data
     * then repopulates storedChickensNbt from the `StoredChickens` list in the input. The superclass state is loaded first.
     *
     * @param valueInput source of persisted values and tag lists for this block entity
     */
    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.eggCount = valueInput.getIntOr("EggCount", 0);
        this.eggsLaidThisNight = valueInput.getIntOr("EggsLaidThisNight", 0); // Added
        this.isNightMode = valueInput.getBooleanOr("IsNightMode", false);
        
        this.storedChickensNbt.clear();
        valueInput.list("StoredChickens", CompoundTag.CODEC)
            .map(ValueInput.TypedInputList::stream)
            .ifPresent(stream -> stream.forEach(this.storedChickensNbt::add));
    }

    /**
     * Remove and return all eggs currently stored in the coop.
     *
     * Resets the internal egg count to 0 and marks the block entity as changed.
     *
     * @return the number of eggs removed from storage, 0 if there were none
     */
    public int takeAllEggs() {
        int total = this.eggCount;
        if (total > 0) {
            this.eggCount = 0;
            this.setChanged();
            return total;
        }
        return 0;
    }

    /**
     * Finds nearby chickens around the coop, serialises up to six of them into
     * {@link #storedChickensNbt}, and removes the original entities from the world.
     *
     * Spawns feather particles at each collected chicken's position to indicate collection.
     *
     * @param level the server level containing the coop
     * @param pos   the block position of the coop used as the search centre
     */
    private void collectChickens(ServerLevel level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(8);
        List<Chicken> nearbyChickens = level.getEntitiesOfClass(Chicken.class, area);

        for (int i = 0; i < Math.min(nearbyChickens.size(), 6); i++) {
            Chicken chicken = nearbyChickens.get(i);
            CompoundTag chickenData = new CompoundTag();
            
            // Wrap our CompoundTag in the ValueOutput implementation
            ValueOutput output = new ChickenValueOutput(chickenData, level.registryAccess());

            spawnFeatherParticles(level, chicken.blockPosition(), 15);
            
            // Satisfies the method signature perfectly!
            chicken.saveWithoutId(output);
            // Store the result
            storedChickensNbt.add(chickenData);
            chicken.discard();
        }
    }
    
    /**
     * Deserialises stored chicken data and spawns those chickens at safe grass positions around the coop.
     *
     * For each stored chicken this method creates a new Chicken entity from its saved NBT, places it at a
     * nearby valid grass spawn within a 3‑block radius of the coop, adds the entity to the level and emits
     * feather particles at the spawn location. After spawning all stored chickens the stored data list is
     * cleared and the block entity is marked as changed.
     *
     * @param level the server level in which to spawn the chickens
     * @param pos   the block position of the coop used to find nearby spawn locations
     */
    private void releaseChickens(ServerLevel level, BlockPos pos) {
        for (CompoundTag chickenData : storedChickensNbt) {
            Chicken chicken = new Chicken(EntityType.CHICKEN, level);
            
            ChickenValueInput inputBridge = new ChickenValueInput(chickenData, level.registryAccess());
            chicken.load(inputBridge); 
            
            // 1. Find a safe spot on nearby grass
            BlockPos spawnPos = findSafeGrassSpawn(level, pos, 3); // Searches a 3-block radius
            
            // 2. Set the position based on the found spot (centered)
            // We add 0.5 to X/Z to center them in the block, and the Y stays at the top of the grass
            double x = spawnPos.getX() + 0.5;
            double y = spawnPos.getY() + 1.0; 
            double z = spawnPos.getZ() + 0.5;

            // 3. Apply the position with a random rotation
            chicken.absSnapTo(
                x, 
                y, 
                z, 
                level.random.nextFloat() * 360.0F, 
                0.0F
            );
            
            level.addFreshEntity(chicken);

            spawnFeatherParticles(level, chicken.blockPosition(), 15);
        }
        
        storedChickensNbt.clear();
        this.setChanged();
    }

    /**
     * Finds a safe spawn position near the coop, preferring grass with two air blocks above.
     *
     * @param level the server level to search in
     * @param origin the block position of the coop centre used as the search origin
     * @param radius horizontal radius, in blocks, to search from the origin
     * @return a BlockPos on a grass block that has two air blocks above for safe spawning, or origin.above() if no suitable grass spot is found
     */
    private BlockPos findSafeGrassSpawn(ServerLevel level, BlockPos origin, int radius) {
        java.util.List<BlockPos> grassSpots = new java.util.ArrayList<>();
        
        // Scan a cube around the coop: 
        // radius horizontally, and -1 to +2 vertically to catch slight hills
        for (BlockPos p : BlockPos.betweenClosed(origin.offset(-radius, -1, -radius), origin.offset(radius, 2, radius))) {
            // Is the block grass?
            if (level.getBlockState(p).is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) {
                // Is there air above it so the chicken doesn't suffocate?
                if (level.isEmptyBlock(p.above()) && level.isEmptyBlock(p.above(2))) {
                    grassSpots.add(p.immutable());
                }
            }
        }

        // If we found grass, pick a random one
        if (!grassSpots.isEmpty()) {
            return grassSpots.get(level.random.nextInt(grassSpots.size()));
        }
        
        // Fallback: If no grass is found (e.g., coop is on wood), 
        // spawn them 1 block above the coop center to avoid being stuck in the "brain" block.
        return origin.above();
    }

    private static class ChickenValueOutput implements ValueOutput {
        private final CompoundTag tag;
        private final HolderLookup.Provider registries; /**
         * Creates a ChickenValueOutput that writes to the given CompoundTag using the provided registry lookup.
         *
         * @param tag the CompoundTag that will be used as the backing NBT storage
         * @param registries a registry lookup provider used when encoding values with codecs
         */

        public ChickenValueOutput(CompoundTag tag, HolderLookup.Provider registries) { 
            this.tag = tag; 
            this.registries = registries; // Added this
        }

        /**
 * Store a boolean into the underlying NBT compound under the given key.
 *
 * @param key   the NBT key to write the value to
 * @param value the boolean value to store
 */
@Override public void putBoolean(String key, boolean value) { tag.putBoolean(key, value); }
        /**
 * Store a byte value in the backing tag under the given key.
 *
 * @param key   the NBT key name to write the byte to
 * @param value the byte value to store
 */
@Override public void putByte(String key, byte value) { tag.putByte(key, value); }
        /**
 * Stores a 16-bit signed integer into the underlying NBT compound under the given key.
 *
 * @param key   the NBT key to write to
 * @param value the 16-bit signed integer value to store
 */
@Override public void putShort(String key, short value) { tag.putShort(key, value); }
        /**
 * Stores an integer value in the underlying NBT tag under the given key.
 *
 * @param key   the NBT key to store the integer under
 * @param value the integer value to store
 */
@Override public void putInt(String key, int value) { tag.putInt(key, value); }
        /**
 * Store a long value in the internal NBT tag under the specified key.
 *
 * @param key   the NBT key to write the value to
 * @param value the long value to store
 */
@Override public void putLong(String key, long value) { tag.putLong(key, value); }
        /**
 * Stores a float under the specified key in the underlying CompoundTag.
 *
 * @param key   the NBT key to store the value under
 * @param value the float value to write
 */
@Override public void putFloat(String key, float value) { tag.putFloat(key, value); }
        /**
 * Store a double value in the underlying compound tag under the specified key.
 *
 * @param key   the NBT key to store the value under
 * @param value the double value to write
 */
@Override public void putDouble(String key, double value) { tag.putDouble(key, value); }
        /**
 * Store a string value in the underlying NBT tag under the given key.
 *
 * @param key   the NBT key to write the string to
 * @param value the string value to store
 */
@Override public void putString(String key, String value) { tag.putString(key, value); }
        /**
 * Store an integer array in the underlying CompoundTag under the specified key.
 *
 * @param key   the NBT key to write the array to
 * @param value the integer array to store
 */
@Override public void putIntArray(String key, int[] value) { tag.putIntArray(key, value); }

        /**
         * Encode an object with the provided Codec and store its resulting NBT under the given key.
         *
         * @param key   the tag key to store the encoded value under
         * @param codec the Codec used to encode the value into NBT
         * @param value the object to encode and store
         */
        @Override
        public <T> void store(String key, Codec<T> codec, T value) {
            // Use the codec to turn the object into NBT
            codec.encodeStart(NbtOps.INSTANCE, value).resultOrPartial(System.err::println)
                .ifPresent(nbt -> tag.put(key, nbt));
        }

        /**
         * Create and attach a new child compound tag under the given key and provide a ValueOutput that writes into it.
         *
         * @param key the key under which the new child CompoundTag will be stored
         * @return a `ValueOutput` that targets the newly created child CompoundTag
         */
        @Override public ValueOutput child(String key) { 
            CompoundTag childTag = new CompoundTag();
            tag.put(key, childTag);
            return new ChickenValueOutput(childTag, registries);
        }
        
        /**
 * Indicates whether the underlying NBT list is empty.
 *
 * @return `true` if the list contains no elements, `false` otherwise.
 */
        @Override public boolean isEmpty() { return tag.isEmpty(); }
        /**
 * Removes the mapping for the given key from the underlying NBT tag.
 *
 * @param string the key to remove from the compound tag
 */
@Override public void discard(String string) { tag.remove(string); }

        /**
         * Serialises a non-null value to NBT and stores it under the given key in this tag.
         *
         * Uses the provided `codec` and the entity's registry-backed serialization context to encode `object`
         * into an NBT element; if encoding succeeds the resulting NBT is written to `tag` under `key`.
         *
         * @param key   the NBT key to store the encoded value under
         * @param codec the codec used to serialise the value into NBT
         * @param object the value to serialise; if `null` nothing is written
         */
        @Override
        public <T> void storeNullable(String key, Codec<T> codec, @Nullable T object) {
            if (object == null) return; // Don't write anything if it's null
            
            // Encode the object into an NBT element
            codec.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), object)
                .resultOrPartial(err -> { /* Log error if needed */ })
                .ifPresent(nbt -> tag.put(key, nbt));
        }

       /**
         * Serialises the given object with the provided MapCodec and stores the resulting NBT in this.tag.
         *
         * Encodes using a registry-aware NBT serialization context; on encoding failure an error message is printed to standard error.
         *
         * @param mapCodec the MapCodec used to encode the object into NBT
         * @param object the object to serialise and store
         */
        @Override
        public <T> void store(MapCodec<T> mapCodec, T object) {
            // 1. Create the context that knows about Minecraft registries
            DynamicOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);

            // 2. MapCodec.encode requires a RecordBuilder. 
            // ops.mapBuilder() provides a fresh one that works with NBT.
            RecordBuilder<Tag> builder = ops.mapBuilder();

            // 3. Tell the codec to fill the builder with the object's data
            mapCodec.encode(object, ops, builder)
                // 4. Build the result directly into our existing 'tag' (CompoundTag)
                .build(this.tag)
                .resultOrPartial(err -> {
                    // Optional: Log if something went wrong during encoding
                    System.err.println("Failed to encode map codec: " + err);
                });
        }

        /**
         * Create and attach an empty list tag under the given key and return a writer for its elements.
         *
         * @param key the NBT key under which the new ListTag will be stored
         * @return a ValueOutputList backed by the newly created ListTag for adding child compound entries
         */
        @Override
        public ValueOutputList childrenList(String key) {
            ListTag listTag = new ListTag();
            tag.put(key, listTag);
            // You will need a helper class that writes to this ListTag
            return new ChickenValueOutputList(listTag, registries);
        }

        /**
         * Create and attach a new NBT list under the given key and provide a typed output view for adding encoded elements.
         *
         * @param key the tag name under which the new list will be stored
         * @param codec the codec used to encode elements placed into the list
         * @return a {@link TypedOutputList} whose additions are encoded with the provided codec and appended to the created NBT list
         */
        @Override
        public <T> TypedOutputList<T> list(String key, Codec<T> codec) {
            ListTag listTag = new ListTag();
            tag.put(key, listTag);
            // You will need a helper class that writes typed objects to this ListTag
            return new ChickenTypedOutputList<>(listTag, codec, registries);
        }
    }

    private record ChickenValueOutputList(ListTag list, HolderLookup.Provider registries) implements ValueOutputList {
        /**
         * Create a new child output element and append it to the list.
         *
         * @return a ValueOutput that writes into the newly added CompoundTag list element
         */
        @Override
        public ValueOutput addChild() {
            CompoundTag nextTag = new CompoundTag();
            list.add(nextTag);
            // Return a new output worker for the tag we just added to the list
            return new ChickenValueOutput(nextTag, registries);
        }

        /**
         * Remove the last child element from the list.
         *
         * If the list is empty this method performs no action.
         */
        @Override
        public void discardLast() {
            if (!list.isEmpty()) {
                list.remove(list.size() - 1);
            }
        }

        /**
         * Checks whether the output list contains no elements.
         *
         * @return `true` if the list contains no elements, `false` otherwise.
         */
        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    private record ChickenTypedOutputList<T>(ListTag list, Codec<T> codec, HolderLookup.Provider registries) implements TypedOutputList<T> {
        /**
         * Encodes a value with the list's codec and appends the resulting NBT element to the backing list.
         *
         * @param value the item to encode and add
         * If encoding fails the error is logged and the value is not added.
        @Override
        public void add(T value) {
            // serialize the object into NBT and add it to the list if successful
            codec.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), value)
                .resultOrPartial(System.err::println)
                .ifPresent(list::add);
        }

        /**
         * Checks whether the output list contains no elements.
         *
         * @return `true` if the list contains no elements, `false` otherwise.
         */
        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    private static class ChickenValueInput implements ValueInput {
        private final CompoundTag tag;
        private final HolderLookup.Provider registries;

        /**
         * Create a ChickenValueInput that reads values from the given NBT tag using the provided registries.
         *
         * @param tag       the CompoundTag containing the serialized chicken data to read
         * @param registries a HolderLookup.Provider used to resolve registry-backed codecs and lookups during decoding
         */
        public ChickenValueInput(CompoundTag tag, HolderLookup.Provider registries) {
            this.tag = tag;
            this.registries = registries;
        }

        /**
         * Read and decode a value stored under the given key from the backing CompoundTag.
         *
         * @param <T>   the target type produced by the codec
         * @param key   the tag key to read
         * @param codec the codec used to decode the tag value
         * @return      an Optional containing the decoded value if the key exists and decoding succeeds, `Optional.empty()` otherwise
         */
        @Override
        public <T> Optional<T> read(String key, Codec<T> codec) {
            if (!tag.contains(key)) return Optional.empty();
            return codec.parse(NbtOps.INSTANCE, tag.get(key)).result();
        }

        /**
         * Decode a value from this CompoundTag using the provided MapCodec and the entity's registry context.
         *
         * @param mapCodec the MapCodec describing how to decode the tag into a value of type `T`
         * @return an Optional containing the decoded value when parsing succeeds, or an empty Optional when parsing fails
         */
        @Override
        public <T> Optional<T> read(MapCodec<T> mapCodec) {
            // 1. Convert the MapCodec into a standard Codec
            // 2. Use the codec to parse the NBT tag
            // 3. We use 'registryAccess' (registries) to handle things like Item types or Biomes
            return mapCodec.codec()
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .result(); // This returns an Optional<T> automatically
        }

        /**
         * Obtain a nested ValueInput for the compound tag stored under the given key.
         *
         * @param key the name of the compound tag to read
         * @return an Optional containing a ValueInput for the nested CompoundTag if the key exists, otherwise Optional.empty()
         */
        @Override
        public Optional<ValueInput> child(String key) {
            if (!tag.contains(key)) return Optional.empty();
            ValueInput input = new ChickenValueInput(tag.getCompound(key).get(), registries);
            return Optional.of(input);
        }

        /**
         * Return a child ValueInput for the given key, or an empty input when no child tag exists.
         *
         * @param key the name of the child tag to retrieve
         * @return a `ValueInput` for the child tag if present, otherwise an empty `ValueInput` backed by a fresh `CompoundTag`
         */
        @Override
        public ValueInput childOrEmpty(String key) {
            return child(key).orElse(new ChickenValueInput(new CompoundTag(), registries));
        }

        /**
 * Retrieves the boolean stored under the given key in the tag, or returns the supplied fallback if the key is absent.
 *
 * @param key the key to look up in the tag
 * @param fallback value to return when the key is not present
 * @return `true` if the tag contains `true` for the key, otherwise the supplied fallback
 */
        @Override public boolean getBooleanOr(String key, boolean fallback) { return tag.contains(key) ? tag.getBoolean(key).get() : fallback; }
        /**
 * Retrieve the byte stored under the given key from the entity tag, or return the provided fallback if the key is missing.
 *
 * @param key the NBT key to read
 * @param fallback value to return when the key is not present
 * @return the byte stored under `key`, or `fallback` if the key is absent
 */
@Override public byte getByteOr(String key, byte fallback) { return tag.contains(key) ? tag.getByte(key).get() : fallback; }
        /**
 * Retrieve the short value stored under the given key, or return the provided fallback.
 *
 * @param key the NBT key to read
 * @param fallback value to return if the key is not present
 * @return the stored short value as an int if present, otherwise the fallback
 */
@Override public int getShortOr(String key, short fallback) { return tag.contains(key) ? tag.getShort(key).get() : fallback; }
        /**
 * Retrieve an integer value for the given key from the backing tag, or return the provided fallback if the key is absent.
 *
 * @param key the tag key to read
 * @param fallback the value to return when the key is not present
 * @return the integer stored under `key`, or `fallback` if the key is missing
 */
@Override public int getIntOr(String key, int fallback) { return tag.contains(key) ? tag.getInt(key).get() : fallback; }
        /**
 * Retrieve the long value stored under the given key in the tag, or return the provided fallback if the key is absent.
 *
 * @param key the tag key to read
 * @param fallback the value to return when the key is not present
 * @return the long value for `key` if present, `fallback` otherwise
 */
@Override public long getLongOr(String key, long fallback) { return tag.contains(key) ? tag.getLong(key).get() : fallback; }
        /**
 * Retrieve the float value stored under the given key in the tag, or return the provided fallback if the key is not present.
 *
 * @param key the tag key to read
 * @param fallback the value to return when the key is absent
 * @return the float value stored under {@code key}, or {@code fallback} if the key is missing
 */
@Override public float getFloatOr(String key, float fallback) { return tag.contains(key) ? tag.getFloat(key).get() : fallback; }
        /**
 * Retrieve a double value from the entity's NBT tag or return a fallback.
 *
 * @param key      the tag key to read
 * @param fallback the value to return if the tag key is not present
 * @return         the double stored under `key`, or `fallback` if the key is absent
 */
@Override public double getDoubleOr(String key, double fallback) { return tag.contains(key) ? tag.getDouble(key).get() : fallback; }
        /**
 * Get the string stored under the given key, or the provided fallback if the key is absent.
 *
 * @param key the NBT key to read
 * @param fallback the value to return when the key is not present
 * @return the string value associated with `key`, or `fallback` if the key is missing
 */
@Override public String getStringOr(String key, String fallback) { return tag.contains(key) ? tag.getString(key).get() : fallback; }

        /**
 * Retrieve an integer value stored under the given NBT key, if present.
 *
 * @param key the NBT tag key to read
 * @return an {@code Optional} containing the integer value if the key exists, {@code Optional.empty()} otherwise
 */
        @Override public Optional<Integer> getInt(String key) { return tag.contains(key) ? Optional.of(tag.getInt(key).get()) : Optional.empty(); }
        /**
 * Retrieve the long value stored under the given NBT key.
 *
 * @param key the NBT key to read from
 * @return an Optional containing the long value for the given key, or empty if the key is not present
 */
@Override public Optional<Long> getLong(String key) { return tag.contains(key) ? Optional.of(tag.getLong(key).get()) : Optional.empty(); }
        /**
 * Retrieve the string value stored under the given NBT key, if present.
 *
 * @param key the NBT key to read
 * @return an Optional containing the string value for the key, or Optional.empty() if the key is not present
 */
@Override public Optional<String> getString(String key) { return tag.contains(key) ? Optional.of(tag.getString(key).get()) : Optional.empty(); }
        /**
 * Obtain the value stored under the given NBT key as an array, if present.
 *
 * @param key the NBT key to read from
 * @return an Optional containing the int array stored under the key, or empty if the key is not present
 */
@Override public Optional<int[]> getIntArray(String key) { return tag.contains(key) ? Optional.of(tag.getIntArray(key).get()) : Optional.empty(); }

        /**
 * Gets the registry lookup provider used by this input for decoding values.
 *
 * @return the HolderLookup.Provider used for registry lookups
 */
@Override public HolderLookup.Provider lookup() { return registries; }

        // Ensure the helper record implements stream()
        private record ChickenTypedInputList<T>(List<T> items) implements TypedInputList<T> {
            /**
 * Check whether the entity contains no items.
 *
 * @return `true` if no items are stored, `false` otherwise.
 */
@Override public boolean isEmpty() { return items.isEmpty(); }
            /**
 * Provide a stream over the list's elements.
 *
 * @return a stream of the list's elements
 */
@Override public Stream<T> stream() { return items.stream(); }
            /**
 * Provide an iterator over the list's elements.
 *
 * @return an Iterator over the contained items.
 */
@Override public java.util.Iterator<T> iterator() { return items.iterator(); }
        }

        /**
         * Read a list of values from the CompoundTag under the given key and decode them with the provided codec.
         *
         * @param key   the tag key that should contain a ListTag of element NBTs
         * @param codec the codec used to decode each list element from NBT
         * @return      an Optional containing a TypedInputList of decoded items if the key exists; Optional.empty() if the key is absent.
         *              Elements that fail to decode are omitted from the returned list and decoding errors are printed to standard error.
         */
        @Override 
        public <T> Optional<TypedInputList<T>> list(String key, Codec<T> codec) { 
            if (!tag.contains(key)) return Optional.empty();
            
            Optional<ListTag> listTag = tag.getList(key);
            List<T> items = listTag.stream()
                .map(nbt -> codec.parse(registries.createSerializationContext(NbtOps.INSTANCE), nbt)
                                .resultOrPartial(System.err::println))
                .flatMap(Optional::stream)
                .toList();
            
            return Optional.of(new ChickenTypedInputList<>(items));
        }

        /**
         * Obtain a typed list decoded from the stored NBT under the given key, or an empty list if the key is not present.
         *
         * @param key   the NBT tag key to read the list from
         * @param codec the codec used to decode each list element
         * @return      a TypedInputList containing decoded items for the key, or an empty list if the key is absent
         */
        @Override 
        public <T> TypedInputList<T> listOrEmpty(String key, Codec<T> codec) { 
            return list(key, codec).orElse(new ChickenTypedInputList<>(List.of()));
        }

        /**
         * Retrieve the list of child compound tags stored under the specified NBT key as a ValueInputList.
         *
         * @param key the NBT key whose value is expected to be a ListTag of CompoundTag elements
         * @return an Optional containing a ValueInputList wrapping the child CompoundTags found at `key`,
         *         or Optional.empty() if the key is absent or its value is not a list
         */
        @Override 
        public Optional<ValueInputList> childrenList(String key) { 
            // Check if the tag exists and is a List
            if (!(tag.get(key) instanceof ListTag listTag)) {
                return Optional.empty();
            }

            // Map each element inside the ListTag (which should be CompoundTags) to ValueInputs
            List<ValueInput> children = listTag.stream()
                .filter(t -> t instanceof CompoundTag) // Ensure the entry is a CompoundTag
                .map(t -> (ValueInput) new ChickenValueInput((CompoundTag) t, registries))
                .toList();
                
            return Optional.of(new ChickenValueInputListImpl(children));
        }

        /**
         * Get the list of child ValueInput entries stored under the given key, or an empty list if the key is absent or not a list.
         *
         * @param key the tag key to read child elements from
         * @return a ValueInputList containing a ValueInput for each child CompoundTag under `key`, or an empty ValueInputList if none exist
         */
        @Override 
        public ValueInputList childrenListOrEmpty(String key) { 
            return childrenList(key).orElse(new ChickenValueInputListImpl(List.of()));
        }

        // Support record for ValueInputList
        private record ChickenValueInputListImpl(List<ValueInput> children) implements ValueInputList {
            /**
 * Checks whether there are no child value inputs in the list.
 *
 * @return `true` if the list contains no child inputs, `false` otherwise.
 */
@Override public boolean isEmpty() { return children.isEmpty(); }
            /**
 * Provide a stream of the child ValueInput elements.
 *
 * @return a Stream of the contained ValueInput instances in iteration order
 */
@Override public Stream<ValueInput> stream() { return children.stream(); }
            /**
 * Provide an iterator over the contained ValueInput elements.
 *
 * @return an Iterator over the contained ValueInput instances
 */
@Override public java.util.Iterator<ValueInput> iterator() { return children.iterator(); }
        }
    }
}
