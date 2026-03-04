package com.tcm.MineTale.block;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.util.CoopPart;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class ChickenCoopBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<CoopPart> PART = EnumProperty.create("part", CoopPart.class);

    public static final MapCodec<ChickenCoopBlock> CODEC = simpleCodec(ChickenCoopBlock::new);

    public ChickenCoopBlock(Properties properties) {
        super(properties);
        // Default to the origin part (Bottom Front Left) facing North
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, CoopPart.BOTTOM_FRONT_LEFT));
    }

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
     * Rotates the 3x3x2 grid logic based on which way the player is facing.
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

   @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}