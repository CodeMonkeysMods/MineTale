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

    /**
     * Constructs a ChickenCoopBlock and initialises its default state to face north and be the bottom front left part.
     *
     * @param properties block properties used to create this block
     */
    public ChickenCoopBlock(Properties properties) {
        super(properties);
        // Default to the origin part (Bottom Front Left) facing North
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, CoopPart.BOTTOM_FRONT_LEFT));
    }

    /**
     * Declare the block-state properties used by this block.
     *
     * Adds the horizontal facing and coop-part properties to the provided state definition builder so instances
     * can track orientation and which sub-part of the multi-block coop they represent.
     *
     * @param builder the block state definition builder to receive properties
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    /**
     * Determine the block state to use when placing a chicken coop, after verifying the full 3×2×3 area is replaceable.
     *
     * @return the initial block state with FACING set to the placement direction and PART set to BOTTOM_FRONT_CENTER, or `null` if any position in the required area cannot be replaced
     */
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

    /**
     * Populates the remaining parts of the 3x2x3 chicken coop around the placed block on the server.
     *
     * For each coordinate in the coop's local grid (3×2×3) except the bottom-front-centre cell (the block placed by the item),
     * places the same block state with the corresponding `PART` value so the multiblock structure is fully constructed.
     *
     * @param level the level in which the block was placed; placement actions are only performed on the server side
     * @param pos the position of the block placed by the player/item
     * @param state the block state of the placed block (its `FACING` is used to orient the coop)
     * @param placer the entity that placed the block, or `null` if none
     * @param stack the item stack used to place the block
     */
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

    /**
     * Ensures a coop part is removed when an adjacent block that should belong to the same coop is missing.
     *
     * @param direction     the direction from this block to the neighbouring block that changed
     * @param neighborState the new state of the neighbouring block
     * @return               the AIR block state if the neighbour was expected to be part of this coop and is no longer present; otherwise the result of the superclass implementation
     */
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
     * Determine whether the neighbouring block in the given direction belongs to the same 3x3x2 coop structure for this part.
     *
     * @param state the current block state of this coop part (must contain `PART` and `FACING`)
     * @param dir the direction from this block to the neighbouring block
     * @return `true` if the neighbour in the given direction corresponds to a valid coop part within the 3x3x2 grid for this block, `false` otherwise
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

    /**
     * Removes the whole 3x3x2 chicken coop structure on the server when any coop part is destroyed by a player.
     *
     * For each part in the coop grid this method:
     * - drops the part's resources if the player is not in Creative mode,
     * - replaces the part with AIR,
     * - triggers the break visual/sound effect.
     *
     * This operation is performed only on the server side; client-side destruction is not handled here.
     *
     * @return the block state that will remain at the destroyed position after processing
     */
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
     * Map a local 3x3x2 grid coordinate to a world position based on an origin and horizontal facing.
     *
     * <p>The local grid uses x ∈ {0,1,2} for left→right, z ∈ {0,1} for near→far (away from the player),
     * and y ≥ 0 for height above the origin.</p>
     *
     * @param origin the base world position corresponding to local grid coordinate (1,0,0)
     * @param facing the horizontal facing that orients the grid's depth (z) direction
     * @param x      local x index: 0 = left, 1 = centre, 2 = right
     * @param z      local z index: 0 = front/near, 1 = back/far (further from the player)
     * @param y      local y index: vertical offset above the origin
     * @return       the world BlockPos corresponding to the given local grid coordinate
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
     * Provide the MapCodec used by this block for serialising its state.
     *
     * @return the MapCodec for this block type
     */
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}