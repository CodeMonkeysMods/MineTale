package com.tcm.MineTale.block.workbenches;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class AbstractWorkbench<E extends BlockEntity> extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;

    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
    protected final boolean isWide;
    protected final boolean isTall;

    protected AbstractWorkbench(Properties properties, Supplier<BlockEntityType<? extends E>> supplier, boolean isWide, boolean isTall) {
        super(properties);
        this.blockEntityType = supplier;
        this.isWide = isWide;
        this.isTall = isTall;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(TYPE, ChestType.SINGLE));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection().getOpposite();

        // Check horizontal space
        if (isWide) {
            BlockPos sidePos = pos.relative(facing.getClockWise());
            if (!level.getBlockState(sidePos).canBeReplaced(context)) return null;
            if (isTall && !level.getBlockState(sidePos.above()).canBeReplaced(context)) return null;
        }
        
        // Check vertical space
        if (isTall) {
            if (!level.getBlockState(pos.above()).canBeReplaced(context)) return null;
        }

        return this.defaultBlockState().setValue(FACING, facing).setValue(TYPE, isWide ? ChestType.LEFT : ChestType.SINGLE);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        
        if (isWide) {
            BlockPos sidePos = pos.relative(facing.getClockWise());
            // Place Right Side
            level.setBlock(sidePos, state.setValue(TYPE, ChestType.RIGHT), 3);
            
            if (isTall) {
                // Place Upper Row
                level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(TYPE, ChestType.LEFT), 3);
                level.setBlock(sidePos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(TYPE, ChestType.RIGHT), 3);
            }
        } else if (isTall) {
            level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if (isWide || isTall) {
            if (isNeighborRequired(blockState, direction)) {
                // If the neighbor we depend on is gone or is no longer a workbench part, break this block
                if (!blockState2.is(this) || !isCompatiblePart(blockState, blockState2)) {
                    return Blocks.AIR.defaultBlockState();
                }
            }
        }
        return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    /**
     * Determines if a specific direction is "required" for this block's survival.
     */
    private boolean isNeighborRequired(BlockState state, Direction direction) {
        DoubleBlockHalf half = state.getValue(HALF);
        ChestType type = state.getValue(TYPE);
        Direction facing = state.getValue(FACING);

        // If I am LOWER, I need the block ABOVE (if tall)
        if (isTall && half == DoubleBlockHalf.LOWER && direction == Direction.UP) return true;
        // If I am UPPER, I need the block BELOW
        if (half == DoubleBlockHalf.UPPER && direction == Direction.DOWN) return true;
        // If I am LEFT, I need the block to my RIGHT
        if (type == ChestType.LEFT && direction == facing.getClockWise()) return true;
        // If I am RIGHT, I need the block to my LEFT
        if (type == ChestType.RIGHT && direction == facing.getCounterClockWise()) return true;

        return false;
    }

    /**
     * Checks if the neighbor state is actually the correct "other half" of this multi-block.
     */
    private boolean isCompatiblePart(BlockState current, BlockState neighbor) {
        // Ensure they face the same way
        if (current.getValue(FACING) != neighbor.getValue(FACING)) return false;
        
        // Additional checks could go here (e.g. matching half/type logic)
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, TYPE);
    }
}