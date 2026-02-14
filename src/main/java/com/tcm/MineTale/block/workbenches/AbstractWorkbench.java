package com.tcm.MineTale.block.workbenches;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;

import java.util.function.Supplier;

public abstract class AbstractWorkbench<E extends AbstractWorkbenchEntity> extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;

    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
    protected final boolean isWide;
    protected final boolean isTall;
    protected int tier;

    protected AbstractWorkbench(Properties properties, Supplier<BlockEntityType<? extends E>> supplier, boolean isWide, boolean isTall, int tier) {
        super(properties);
        this.blockEntityType = supplier;
        this.isWide = isWide;
        this.isTall = isTall;
        this.tier = tier;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(TYPE, ChestType.SINGLE));
    }

    public int getTier() {
        return this.tier;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection();

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

    /**
     * Registers the block state properties used by this block.
     *
     * @param builder the state builder to populate with this block's properties (`FACING`, `HALF`, `TYPE`)
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, TYPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        boolean isLower = state.getValue(HALF) == DoubleBlockHalf.LOWER;
        ChestType type = state.getValue(TYPE);

        // Only the Master block gets the entity
        if (isLower && (type == ChestType.LEFT || type == ChestType.SINGLE)) {
            // We call create() on the supplier we were given in the constructor
            return this.blockEntityType.get().create(pos, state);
        }
        return null;
    }

    /**
     * Open the workbench menu for the block's master part when a player interacts without an item.
     *
     * @param state     the block state at the clicked position
     * @param level     the level in which the interaction occurs
     * @param pos       the position of the block that was interacted with
     * @param player    the player performing the interaction
     * @param hitResult details about the hit (hit position and face)
     * @return {@code InteractionResult.CONSUME} if a menu was opened on the server, {@code InteractionResult.SUCCESS} on the client, {@code InteractionResult.PASS} otherwise
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        // 1. Find the Master Position (Bottom-Left)
        BlockPos masterPos = getMasterPos(state, pos);
        BlockEntity blockEntity = level.getBlockEntity(masterPos);

        // 2. Check if the Master block has the MenuProvider trait
        if (blockEntity instanceof MenuProvider menuProvider) {
            // 3. Open the Screen (this triggers the ScreenHandler/Menu)
            player.openMenu(menuProvider);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    /**
     * Compute the master (bottom-left) anchor position for this workbench block.
     *
     * The master is the block that serves as the primary anchor for multi-block
     * behavior and block-entity placement: if this block is the upper half the
     * master is one block below; if this block is the right-side part the master
     * is one block to the left relative to the block's facing.
     *
     * @param state the block state used to determine HALF, TYPE, and FACING
     * @param pos   the current block position
     * @return the position of the master (bottom-left) block for this workbench
     */
    public BlockPos getMasterPos(BlockState state, BlockPos pos) {
        BlockPos master = pos;
        Direction facing = state.getValue(FACING);
        
        // Move down if we are the upper half
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            master = master.below();
        }
        
        // Move left if we are the right side (relative to facing)
        if (state.getValue(TYPE) == ChestType.RIGHT) {
            master = master.relative(facing.getCounterClockWise());
        }
        
        return master;
    }
}