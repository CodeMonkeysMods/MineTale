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
        // For a 3x3x2, we can be lazy: if ANY adjacent block of the same type is removed, 
        // the whole thing should probably go. 
        // You can refine this to only check the "Master" block if you want more stability.
        return true; 
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);
            CoopPart currentPart = state.getValue(PART);

            // Find the absolute origin (0,0,0) by subtracting the current part's offset
            BlockPos origin = pos.subtract(calculateOffset(BlockPos.ZERO, facing, 
                    currentPart.getXOffset(), currentPart.getZOffset(), currentPart.getYOffset()));

            // Break all 18 blocks in the 3x3x2 grid
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 2; z++) {
                    for (int y = 0; y < 3; y++) {
                        BlockPos targetPos = calculateOffset(origin, facing, x, z, y);
                        BlockState targetState = level.getBlockState(targetPos);
                        
                        // Only break blocks that belong to this mod's chicken coop
                        if (targetState.is(this)) {
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                            // 2001 is the ID for block break particles + sound
                            level.levelEvent(2001, targetPos, Block.getId(targetState)); 
                        }
                    }
                }
            }
        }
        
        // Call super and return the resulting state as required by 1.21.1
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