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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;

import java.util.function.Supplier;

// DoorBlock

public abstract class AbstractWorkbench<E extends AbstractWorkbenchEntity> extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
    protected final boolean isWide;
    protected final boolean isTall;
    protected int tier;

    /**
     * Constructs an AbstractWorkbench configured with its block properties, block-entity factory, size flags, and tier.
     *
     * @param properties the block properties passed to the base block constructor
     * @param supplier   supplier of the BlockEntityType used to create the master block entity
     * @param isWide     true if the workbench occupies two horizontal blocks (master + side)
     * @param isTall     true if the workbench occupies two vertical levels (lower + upper)
     * @param tier       the tier level of the workbench
     */
    protected AbstractWorkbench(Properties properties, Supplier<BlockEntityType<? extends E>> supplier, boolean isWide, boolean isTall, int tier) {
        super(properties);
        this.blockEntityType = supplier;
        this.isWide = isWide;
        this.isTall = isTall;
        this.tier = tier;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(TYPE, ChestType.SINGLE)
                .setValue(LIT, false));
    }

    /**
     * Retrieve the workbench tier level.
     *
     * @return the tier level of the workbench
     */
    public int getTier() {
        return this.tier;
    }

    /**
     * Compute the initial block state when this workbench is placed by a player.
     *
     * <p>If the workbench is configured as wide, verifies the adjacent "side" position
     * (the block to the right relative to the placed facing) can be replaced; if not,
     * placement is considered invalid and this method returns {@code null}.</p>
     *
     * The resulting state sets the block's facing, partition type (LEFT for wide, SINGLE otherwise),
     * half to LOWER, and LIT to {@code false}.
     *
     * @param context the placement context provided by the game
     * @return the configured BlockState for placement, or {@code null} if placement is invalid
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (isWide) {
            // Find the 'Side' block relative to the player's perspective
            // Clockwise from the 'Front' (Opposite) is the Right side
            BlockPos sidePos = pos.relative(facing.getClockWise());
            
            if (!level.getBlockState(sidePos).canBeReplaced(context)) return null;
        }

        // MANDATORY: The block you clicked MUST be the LEFT (Master) block.
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(TYPE, isWide ? ChestType.LEFT : ChestType.SINGLE)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(LIT, false);
    }

    /**
     * Places the additional blocks that compose the multi-block workbench after the master block is placed.
     *
     * When the workbench is wide, this places the right (slave) part adjacent to the master; when tall, this places the upper parts above the master (and above the right part when wide).
     *
     * @param level the world in which blocks are being placed
     * @param pos the position of the master (initially placed) block
     * @param state the BlockState of the master block as placed
     * @param placer the entity that placed the block, or null if placed by non-entity logic
     * @param stack the ItemStack used to place the block
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        
        if (isWide) {
            // Calculate the side block exactly as we did in getStateForPlacement
            BlockPos sidePos = pos.relative(facing.getClockWise());
            
            // Place the RIGHT (Slave/Invisible) side
            level.setBlock(sidePos, state.setValue(TYPE, ChestType.RIGHT), 3);
            
            if (isTall) {
                // Place the TOP row
                // The block above the click is TOP-LEFT
                level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(TYPE, ChestType.LEFT), 3);
                // The block above the side is TOP-RIGHT
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
         * Register this block's state properties used for placement and rendering.
         * <p>
         * Adds the horizontal facing, vertical half, chest-type partitioning, and lit state.
         *
         * @param builder the state builder to populate with this block's properties
         */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, TYPE, LIT);
    }

    /**
     * Create the block entity for this block when this state represents the master lower part of the workbench.
     *
     * The method returns an instance created by the configured BlockEntityType supplier only if the block's
     * HALF is LOWER and its TYPE is LEFT or SINGLE; otherwise it returns null.
     *
     * @param pos   the block position for the new entity
     * @param state the block state used to determine whether to create an entity
     * @return the block entity for the master lower part, or `null` if this state does not own an entity
     */
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

    /**
     * Produce a new VoxelShape representing the given shape rotated from North to the specified horizontal direction.
     *
     * @param to    the horizontal direction to rotate the shape to (target orientation)
     * @param shape the original VoxelShape oriented facing North
     * @return      a VoxelShape equal to the input shape rotated to face the `to` direction
     */
    protected static VoxelShape rotateShape(Direction to, VoxelShape shape) {
        VoxelShape[] buffer = { shape, Shapes.empty() };
        // get2DDataValue returns: S=0, W=1, N=2, E=3. 
        // We calculate steps relative to North.
        int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        
        for (int i = 0; i < times; i++) {
            buffer[1] = Shapes.empty();
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                // Standard 90-degree rotation formula for bounding boxes
                buffer[1] = Shapes.or(buffer[1], Block.box(
                    (1.0 - maxZ) * 16.0, 
                    minY * 16.0, 
                    minX * 16.0, 
                    (1.0 - minZ) * 16.0, 
                    maxY * 16.0, 
                    maxX * 16.0
                ));
            });
            buffer[0] = buffer[1];
        }
        return buffer[0];
    }

    /**
     * Allow skylight to pass through this block.
     *
     * @return true if skylight propagates through this block, false otherwise
     */
    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true; 
    }

    /**
     * Provides the block's occlusion shape used for face-culling and neighbor-face visibility.
     *
     * @return the VoxelShape representing the block's occlusion geometry; used to determine which faces of adjacent blocks are hidden (may be a non-full-cube shape to prevent incorrect culling)
     */
    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        // This tells the engine exactly which parts of the block hide others.
        // Returning the custom shape instead of a full cube prevents culling of neighbor faces.
        return state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
    }

    /**
     * Force the block to use full brightness for shading to avoid dark self-shadowing.
     *
     * @param state the block state at the position
     * @param level the world accessor used for context
     * @param pos   the block position
     * @return      the brightness multiplier for shading; `1.0F` for full brightness
     */
    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        // In your source: return blockState.isCollisionShapeFullBlock(...) ? 0.2F : 1.0F;
        // We want 1.0F to ensure the block doesn't cast a pitch-black shadow on itself.
        return 1.0F;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            BlockPos masterPos = getMasterPos(state, pos);
            boolean isMaster = pos.equals(masterPos);

            // Iterate through the entire 2x2 or 1x2 structure
            for (int y = 0; y <= (isTall ? 1 : 0); y++) {
                for (int x = 0; x <= (isWide ? 1 : 0); x++) {
                    Direction facing = state.getValue(FACING);
                    BlockPos targetPos = masterPos.above(y).relative(facing.getClockWise(), x);

                    // Skip the block the player is currently mining
                    if (targetPos.equals(pos)) continue;

                    BlockState targetState = level.getBlockState(targetPos);
                    if (targetState.is(this) && getMasterPos(targetState, targetPos).equals(masterPos)) {
                        if (isMaster) {
                            // If we are mining the Master, destroy others SILENTLY
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 35);
                        } else {
                            // If we are mining a Slave block, we need to handle the Master carefully.
                            // If the Master is at this targetPos, destroy it WITH drops.
                            if (targetPos.equals(masterPos)) {
                                level.destroyBlock(targetPos, !player.isCreative());
                            } else {
                                // Otherwise, it's just another slave block, remove silently.
                                level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 35);
                            }
                        }
                        level.gameEvent(GameEvent.BLOCK_DESTROY, targetPos, GameEvent.Context.of(player, targetState));
                    }
                }
            }

            // Final tweak: If the player is mining a SLAVE block, 
            // we must prevent the slave block itself from dropping.
            if (!isMaster && !player.isCreative()) {
                // This prevents the current block from dropping its loot table 
                // because we already triggered the Master's drop above.
                state = state.setValue(BlockStateProperties.LIT, false); // Optional: change state to desync loot
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }
}