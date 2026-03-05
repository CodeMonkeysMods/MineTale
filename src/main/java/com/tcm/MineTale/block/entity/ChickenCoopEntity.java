package com.tcm.MineTale.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.Chicken;
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

    public ChickenCoopEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHICKEN_COOP_BE, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ChickenCoopEntity be) {
        if (level.isClientSide()) return;

        long time = level.getDayTime() % 24000;
        boolean isLate = time >= 13000 && time < 23000; // Monster spawn window

        if (isLate && !be.isNightMode) {
            System.out.println("Chicken Coop: Attempting to collect chickens!");
            be.collectChickens((ServerLevel) level, pos);
            be.isNightMode = true;
            be.setChanged();
        } else if (!isLate && be.isNightMode) {
            be.releaseChickens((ServerLevel) level, pos);
            be.isNightMode = false;
            be.setChanged();
        }
    }

    private void collectChickens(ServerLevel level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(8);
        List<Chicken> nearbyChickens = level.getEntitiesOfClass(Chicken.class, area);

        for (int i = 0; i < Math.min(nearbyChickens.size(), 6); i++) {
            Chicken chicken = nearbyChickens.get(i);
            CompoundTag chickenData = new CompoundTag();
            
            // Wrap our CompoundTag in the ValueOutput implementation
            ValueOutput output = new ChickenValueOutput(chickenData, level.registryAccess());
            
            // Satisfies the method signature perfectly!
            chicken.saveWithoutId(output);
            
            // Store the result
            storedChickensNbt.add(chickenData);
            chicken.discard();
        }
    }
    
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
        }
        
        storedChickensNbt.clear();
        this.setChanged();
    }

    /**
     * Scans the area around the coop for grass blocks with air above them.
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
        private final HolderLookup.Provider registries; // Added this

        public ChickenValueOutput(CompoundTag tag, HolderLookup.Provider registries) { 
            this.tag = tag; 
            this.registries = registries; // Added this
        }

        @Override public void putBoolean(String key, boolean value) { tag.putBoolean(key, value); }
        @Override public void putByte(String key, byte value) { tag.putByte(key, value); }
        @Override public void putShort(String key, short value) { tag.putShort(key, value); }
        @Override public void putInt(String key, int value) { tag.putInt(key, value); }
        @Override public void putLong(String key, long value) { tag.putLong(key, value); }
        @Override public void putFloat(String key, float value) { tag.putFloat(key, value); }
        @Override public void putDouble(String key, double value) { tag.putDouble(key, value); }
        @Override public void putString(String key, String value) { tag.putString(key, value); }
        @Override public void putIntArray(String key, int[] value) { tag.putIntArray(key, value); }

        @Override
        public <T> void store(String key, Codec<T> codec, T value) {
            // Use the codec to turn the object into NBT
            codec.encodeStart(NbtOps.INSTANCE, value).resultOrPartial(System.err::println)
                .ifPresent(nbt -> tag.put(key, nbt));
        }

        // Boilerplate for lists and children (can be implemented if needed)
        @Override public ValueOutput child(String key) { 
            CompoundTag childTag = new CompoundTag();
            tag.put(key, childTag);
            return new ChickenValueOutput(childTag, registries);
        }
        
        // Implement other required methods with empty/default logic or full NBT support
        @Override public boolean isEmpty() { return tag.isEmpty(); }
        @Override public void discard(String string) { tag.remove(string); }

        @Override
        public <T> void storeNullable(String key, Codec<T> codec, @Nullable T object) {
            if (object == null) return; // Don't write anything if it's null
            
            // Encode the object into an NBT element
            codec.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), object)
                .resultOrPartial(err -> { /* Log error if needed */ })
                .ifPresent(nbt -> tag.put(key, nbt));
        }

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

        @Override
        public ValueOutputList childrenList(String key) {
            ListTag listTag = new ListTag();
            tag.put(key, listTag);
            // You will need a helper class that writes to this ListTag
            return new ChickenValueOutputList(listTag, registries);
        }

        @Override
        public <T> TypedOutputList<T> list(String key, Codec<T> codec) {
            ListTag listTag = new ListTag();
            tag.put(key, listTag);
            // You will need a helper class that writes typed objects to this ListTag
            return new ChickenTypedOutputList<>(listTag, codec, registries);
        }
    }

    private record ChickenValueOutputList(ListTag list, HolderLookup.Provider registries) implements ValueOutputList {
        @Override
        public ValueOutput addChild() {
            CompoundTag nextTag = new CompoundTag();
            list.add(nextTag);
            // Return a new output worker for the tag we just added to the list
            return new ChickenValueOutput(nextTag, registries);
        }

        @Override
        public void discardLast() {
            if (!list.isEmpty()) {
                list.remove(list.size() - 1);
            }
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    private record ChickenTypedOutputList<T>(ListTag list, Codec<T> codec, HolderLookup.Provider registries) implements TypedOutputList<T> {
        @Override
        public void add(T value) {
            // serialize the object into NBT and add it to the list if successful
            codec.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), value)
                .resultOrPartial(System.err::println)
                .ifPresent(list::add);
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    private static class ChickenValueInput implements ValueInput {
        private final CompoundTag tag;
        private final HolderLookup.Provider registries;

        public ChickenValueInput(CompoundTag tag, HolderLookup.Provider registries) {
            this.tag = tag;
            this.registries = registries;
        }

        @Override
        public <T> Optional<T> read(String key, Codec<T> codec) {
            if (!tag.contains(key)) return Optional.empty();
            return codec.parse(NbtOps.INSTANCE, tag.get(key)).result();
        }

        @Override
        public <T> Optional<T> read(MapCodec<T> mapCodec) {
            // 1. Convert the MapCodec into a standard Codec
            // 2. Use the codec to parse the NBT tag
            // 3. We use 'registryAccess' (registries) to handle things like Item types or Biomes
            return mapCodec.codec()
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .result(); // This returns an Optional<T> automatically
        }

        @Override
        public Optional<ValueInput> child(String key) {
            if (!tag.contains(key)) return Optional.empty();
            ValueInput input = new ChickenValueInput(tag.getCompound(key).get(), registries);
            return Optional.of(input);
        }

        @Override
        public ValueInput childOrEmpty(String key) {
            return child(key).orElse(new ChickenValueInput(new CompoundTag(), registries));
        }

        // Primitive Getters with Fallbacks
        @Override public boolean getBooleanOr(String key, boolean fallback) { return tag.contains(key) ? tag.getBoolean(key).get() : fallback; }
        @Override public byte getByteOr(String key, byte fallback) { return tag.contains(key) ? tag.getByte(key).get() : fallback; }
        @Override public int getShortOr(String key, short fallback) { return tag.contains(key) ? tag.getShort(key).get() : fallback; }
        @Override public int getIntOr(String key, int fallback) { return tag.contains(key) ? tag.getInt(key).get() : fallback; }
        @Override public long getLongOr(String key, long fallback) { return tag.contains(key) ? tag.getLong(key).get() : fallback; }
        @Override public float getFloatOr(String key, float fallback) { return tag.contains(key) ? tag.getFloat(key).get() : fallback; }
        @Override public double getDoubleOr(String key, double fallback) { return tag.contains(key) ? tag.getDouble(key).get() : fallback; }
        @Override public String getStringOr(String key, String fallback) { return tag.contains(key) ? tag.getString(key).get() : fallback; }

        // Optional Getters
        @Override public Optional<Integer> getInt(String key) { return tag.contains(key) ? Optional.of(tag.getInt(key).get()) : Optional.empty(); }
        @Override public Optional<Long> getLong(String key) { return tag.contains(key) ? Optional.of(tag.getLong(key).get()) : Optional.empty(); }
        @Override public Optional<String> getString(String key) { return tag.contains(key) ? Optional.of(tag.getString(key).get()) : Optional.empty(); }
        @Override public Optional<int[]> getIntArray(String key) { return tag.contains(key) ? Optional.of(tag.getIntArray(key).get()) : Optional.empty(); }

        @Override public HolderLookup.Provider lookup() { return registries; }

        // Minimal implementation for Lists (can be expanded if chickens store list data)
        @Override public <T> Optional<TypedInputList<T>> list(String key, Codec<T> codec) { return Optional.empty(); }
        @Override public <T> TypedInputList<T> listOrEmpty(String key, Codec<T> codec) { 
            return new TypedInputList<T>() {
                @Override public boolean isEmpty() { return true; }
                @Override public Stream<T> stream() { return Stream.empty(); }
                @Override public java.util.Iterator<T> iterator() { return Stream.<T>empty().iterator(); }
            };
        }

        @Override public Optional<ValueInputList> childrenList(String string) { return Optional.empty(); }
        @Override public ValueInputList childrenListOrEmpty(String string) { 
            return new ValueInputList() {
                @Override public boolean isEmpty() { return true; }
                @Override public Stream<ValueInput> stream() { return Stream.empty(); }
                @Override public java.util.Iterator<ValueInput> iterator() { return Stream.<ValueInput>empty().iterator(); }
            };
        }
    }
}