package com.tcm.MineTale.block;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.entity.ChickenCoopEntity;
import com.tcm.MineTale.registry.ModBlockEntities;
import com.tcm.MineTale.util.CoopPart;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

public class ChickenCoopBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<CoopPart> PART = EnumProperty.create("part", CoopPart.class);

    public static final MapCodec<ChickenCoopBlock> CODEC = simpleCodec(ChickenCoopBlock::new);

    /**
     * Create a ChickenCoopBlock configured with the provided block properties and a default state.
     *
     * The default state sets FACING to NORTH and PART to CoopPart.BOTTOM_FRONT_LEFT.
     *
     * @param properties block properties used to configure this block's behaviour and characteristics
     */
    public ChickenCoopBlock(Properties properties) {
        super(properties);
        // Default to the origin part (Bottom Front Left) facing North
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, CoopPart.BOTTOM_FRONT_LEFT));
    }

    /**
     * Provides a BlockEntityTicker for the coop's centre-front part on the server.
     *
     * Returns a ticker that delegates to ChickenCoopEntity.tick when the call is on the logical server,
     * the block state's PART is BOTTOM_FRONT_CENTER and the requested BlockEntityType equals ModBlockEntities.CHICKEN_COOP_BE.
     *
     * @param <T>   the block entity type
     * @param level the level containing the block
     * @param state the block state for which a ticker is requested
     * @param type  the requested block entity type
     * @return      a ticker delegating to ChickenCoopEntity.tick when applicable, `null` otherwise
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // 1. Only tick on server side
        if (level.isClientSide()) return null;

        // 2. Only tick if this is the correct part of the coop
        if (state.getValue(PART) != CoopPart.BOTTOM_FRONT_CENTER) return null;

        // 3. Link to the static tick method in your Entity class
        return type == ModBlockEntities.CHICKEN_COOP_BE 
            ? (lvl, pos, st, be) -> ChickenCoopEntity.tick(lvl, pos, st, (ChickenCoopEntity) be) 
            : null;
    }

    /**
     * Creates the block entity for the coop when this block represents the centre-front (brain) part.
     *
     * @param pos   the block position
     * @param state the current block state
     * @return {@code ChickenCoopEntity} for the centre-front part, {@code null} otherwise
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Only the center-front part gets the "brain"
        if (state.getValue(PART) == CoopPart.BOTTOM_FRONT_CENTER) {
            return new ChickenCoopEntity(pos, state);
        }
        return null;
    }

    /**
     * Registers this block's state properties.
     *
     * Adds the horizontal facing and coop part properties so block states can represent orientation and segment.
     *
     * @param builder the state definition builder to register properties with
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection();

        // Verification loop to ensure space is clear
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 2; z++) {
                for (int y = 0; y < 3; y++) {
                    BlockPos targetPos = calculateOffset(clickedPos, facing, x, z, y);
                    if (!level.getBlockState(targetPos).canBeReplaced(context)) {
                        return null; 
                    }
                }
            }
        }
        // Set the initial block to the Center-Front part
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(PART, CoopPart.BOTTOM_FRONT_CENTER);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);

            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 2; z++) { // z=0 is front, z=1 is back (away)
                    for (int y = 0; y < 3; y++) {
                        // Skip the block actually placed by the item (Bottom Front Center)
                        if (x == 1 && z == 0 && y == 0) continue; 

                        BlockPos targetPos = calculateOffset(pos, facing, x, z, y);
                        CoopPart part = CoopPart.getPartFromCoords(x, z, y);
                        
                        level.setBlock(targetPos, state.setValue(PART, part), 3);
                    }
                }
            }
        }
    }

    @Override
    protected BlockState updateShape(
        BlockState state, 
        LevelReader levelReader, 
        ScheduledTickAccess scheduledTickAccess, 
        BlockPos pos, 
        Direction direction, 
        BlockPos neighborPos, 
        BlockState neighborState, 
        RandomSource randomSource
    ) {
        // If a neighbor block that is supposed to be part of this coop is now AIR, 
        // we return AIR to destroy this part of the coop as well.
        if (!neighborState.is(this) && isNeighborPartOfCoop(state, direction)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, randomSource);
    }

    /**
     * Helper to check if the block in a specific direction is technically "connected" 
     * to this specific part of the 3x3x2 grid.
     */
    private boolean isNeighborPartOfCoop(BlockState state, Direction dir) {
        CoopPart part = state.getValue(PART);
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);

        // 1. Get the local offset of the neighbor block relative to this part
        // We convert the world Direction into a local x, y, z change
        int dx = dir.getStepX();
        int dy = dir.getStepY();
        int dz = dir.getStepZ();

        // 2. Adjust for rotation (Facing) 
        // This ensures that "Front" always matches your Enum's Z-axis logic
        // Note: This math varies slightly depending on how your placement logic 
        // maps "Front" to the world. Below is a standard mapping:
        int localDx, localDz;
        switch (facing) {
            case NORTH -> { localDx = dx; localDz = dz; }
            case SOUTH -> { localDx = -dx; localDz = -dz; }
            case WEST  -> { localDx = dz; localDz = -dx; }
            case EAST  -> { localDx = -dz; localDz = dx; }
            default    -> { localDx = dx; localDz = dz; }
        }

        // 3. Calculate the neighbor's hypothetical grid position
        int neighborX = part.getXOffset() + localDx;
        int neighborZ = part.getZOffset() + localDz;
        int neighborY = part.getYOffset() + dy;

        // 4. Check if these coordinates are within the 3x2x3 bounds
        // Width: 0-2 (X), Depth: 0-1 (Z), Height: 0-2 (Y)
        return neighborX >= 0 && neighborX < 3 &&
            neighborZ >= 0 && neighborZ < 2 &&
            neighborY >= 0 && neighborY < 3;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);
            CoopPart currentPart = state.getValue(PART);

            // Calculate origin based on the piece being broken
            BlockPos origin = pos.subtract(calculateOffset(BlockPos.ZERO, facing, 
                    currentPart.getXOffset(), currentPart.getZOffset(), currentPart.getYOffset()));

            // Use a flag to prevent re-entry if isNeighborPartOfCoop triggers
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 2; z++) {
                    for (int y = 0; y < 3; y++) {
                        BlockPos targetPos = calculateOffset(origin, facing, x, z, y);
                        BlockState targetState = level.getBlockState(targetPos);
                        
                        if (targetState.is(this)) {
                            // 1. Handle Drops: This checks the loot table (JSON) and drops items
                            if (!player.isCreative()) {
                                BlockEntity blockEntity = targetState.hasBlockEntity() ? level.getBlockEntity(targetPos) : null;
                                Block.dropResources(targetState, level, targetPos, blockEntity, player, player.getMainHandItem());
                            }
                            
                            // 2. Set to AIR with flag 3 (Update neighbors + Send to clients)
                            // Using destroyBlock with 'false' for drops since we handled it above 
                            // for better control, or just setBlock to AIR.
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                            
                            // 3. Play break effects
                            level.levelEvent(2001, targetPos, Block.getId(targetState));
                        }
                    }
                }
            }
        }
        
        return super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * Compute the world block position for a local coordinate inside the coop's 3×2×3 grid, taking block facing into account.
     *
     * @param origin the reference origin position (the block considered as the grid origin)
     * @param facing the horizontal direction the coop is facing; used to convert local depth into world direction
     * @param x      local x index in the 3-wide grid (0 = left, 1 = centre, 2 = right)
     * @param z      local depth index along the facing direction (0..2); larger values are further away from the player
     * @param y      local vertical index (0..2) measured as blocks above the origin
     * @return       the computed BlockPos in world coordinates for the given local grid coordinate
     */
    private BlockPos calculateOffset(BlockPos origin, Direction facing, int x, int z, int y) {
        // x-1 centers the 3-wide structure (0=left, 1=center, 2=right)
        int xAdjusted = x - 1; 
        
        // We use the 'facing' direction for depth (z). 
        // This ensures z=1 is always "further away" from the player.
        return origin.relative(facing, z) 
                    .relative(facing.getClockWise(), xAdjusted) 
                    .above(y);
    }

    /**
     * Provide the block's MapCodec used by the game's codec system for (de)serialisation.
     *
     * @return the MapCodec instance for this block's state
     */
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    /**
 * Handle interaction with the coop when used without an item, dispensing any collected eggs to the player.
 *
 * On the client this returns `InteractionResult.SUCCESS`. On the server this locates the coop's brain
 * block entity (the bottom-front-center part); if that entity has eggs they are transferred to the player
 * (or dropped at the player's feet if their inventory is full) and a chicken-egg sound is played.
 *
 * @param state     the current block state
 * @param level     the level where the block is located
 * @param pos       the position of the interacted block
 * @param player    the player performing the interaction
 * @param hitResult hit information for the interaction
 * @return `InteractionResult.SUCCESS` if eggs were given or on the client, `InteractionResult.PASS` otherwise.
 */
@Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        // 1. Find the "brain" position (the Bottom Front Center)
        Direction facing = state.getValue(FACING);
        CoopPart currentPart = state.getValue(PART);
        
        // Calculate the origin (Bottom Front Center is our origin in calculateOffset logic)
        // Based on your calculateOffset, the BFC is at x=1, z=0, y=0.
        BlockPos brainPos = pos.subtract(calculateOffset(BlockPos.ZERO, facing, 
                currentPart.getXOffset(), currentPart.getZOffset(), currentPart.getYOffset()))
                .relative(facing, 0) // already at z=0
                .relative(facing.getClockWise(), 0); // x=1 is center, so we shift back to it

        // Easier way: Since you know the brain is always at BOTTOM_FRONT_CENTER:
        // We just need to find where that specific part is relative to the current block.
        // However, your 'calculateOffset' is already the source of truth.
        
        if (level.getBlockEntity(brainPos) instanceof ChickenCoopEntity coopBe) {
            int eggsToGive = coopBe.takeAllEggs();
            
            if (eggsToGive > 0) {
                // Give the player an egg
                ItemStack eggStack = new ItemStack(Items.EGG, eggsToGive);
                if (!player.getInventory().add(eggStack)) {
                    // If inventory full, drop at player's feet
                    player.drop(eggStack, false);
                }
                
                // Play a sound to give feedback
                level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
}
    
}
