package com.tcm.MineTale.block.workbenches;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tcm.MineTale.block.workbenches.entity.FurnaceWorkbenchEntity;
import com.tcm.MineTale.registry.ModTiers;
import com.tcm.MineTale.registry.ModTiers.FurnaceTier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FurnaceWorkbench extends AbstractWorkbench<FurnaceWorkbenchEntity> {
    // Setting these to true creates the 2x2 multi-block footprint
    private static final boolean IS_WIDE = true;
    private static final boolean IS_TALL = true;

    public static final MapCodec<FurnaceWorkbench> CODEC = RecordCodecBuilder.mapCodec(instance -> 
        instance.group(
            // This handles the standard block properties
            propertiesCodec(), 
            // This handles the tier (assuming FurnaceTier is a record/enum with its own codec)
            Codec.INT.fieldOf("tier").forGetter(block -> block.getTier())
        ).apply(instance, (props, id) -> new FurnaceWorkbench(props, ModTiers.getTierFromInt(id)))
    );

    /**
     * Creates a FurnaceWorkbench using the default furnace workbench block entity type and a 2×2 footprint.
     *
     * @param properties block properties for this workbench
     */
    public FurnaceWorkbench(Properties properties, FurnaceTier tier) {
        super(properties, () -> ModTiers.TIER_MAP.get(tier), IS_WIDE, IS_TALL, tier.id());
    }

    /**
     * Provides a ticker that drives furnace workbench logic for the master block.
     *
     * The returned ticker invokes {@link FurnaceWorkbenchEntity#tick(Level, BlockPos, BlockState)} on the master
     * workbench block's entity when the supplied `type` matches the furnace workbench block entity type;
     * otherwise no ticker is provided.
     *
     * @param <T>   block entity type
     * @param level the level in which the ticker will run
     * @param state the block state for which the ticker is requested
     * @param type  the block entity type being ticked
     * @return a ticker that calls `FurnaceWorkbenchEntity.tick(...)` for the master furnace workbench entity, or
     *         `null` if the provided `type` does not match the furnace workbench block entity type
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Only the Master block (Lower-Left) should tick to process smelting
        // This helper ensures the logic only runs on the Server side for our specific BE
        return createTickerHelper(type, ModTiers.TIER_MAP.get(ModTiers.getTierFromInt(this.tier)), (lvl, pos, st, be) -> {
            if (be instanceof FurnaceWorkbenchEntity furnace) {
                furnace.tick(lvl, pos, st);
            }
        });
    }

    /**
     * The codec used to serialize and deserialize this FurnaceWorkbench type.
     *
     * @return the MapCodec for this FurnaceWorkbench
     */
    @Override
    protected MapCodec<? extends FurnaceWorkbench> codec() {
        return CODEC;
    }

    /**
     * Selects the render shape so only the master (LEFT) or single workbench block renders its model.
     *
     * Returns `RenderShape.MODEL` when the block state's `TYPE` is `LEFT` or `SINGLE`; returns
     * `RenderShape.INVISIBLE` otherwise, hiding non-master halves while preserving their collision shape.
     *
     * @return `RenderShape.MODEL` when `TYPE` is `LEFT` or `SINGLE`; `RenderShape.INVISIBLE` otherwise
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        // We only render the model for the LEFT side (Master)
        // Both Bottom-Left and Top-Left are 'LEFT', so they both render their respective halves
        if (state.getValue(TYPE) == ChestType.LEFT || state.getValue(TYPE) == ChestType.SINGLE) {
            return RenderShape.MODEL;
        }
        
        // Hide the RIGHT side blocks (They still have collision thanks to your getShape method)
        return RenderShape.INVISIBLE;
    }
    
    // left, bottom, back, right, top, front;
    private static final VoxelShape HALF_SLAB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    private static final VoxelShape FURNACE_CORE = Block.box(4.0, 8.0, 2.0, 16.0, 15.0, 14.0);
    private static final VoxelShape TOP_SLAB = Block.box(2.0, -1.0, 1.0, 16.0, 5.0, 15.0);
    private static final VoxelShape FURNACE_CHIMNEY = Block.box(7.0, 5.0, 4.0, 16.0, 15.0, 12.0);

    // Pre-combine them into the 4 final shapes
    private static final VoxelShape RAW_LL = Shapes.or(HALF_SLAB, FURNACE_CORE);
    private static final VoxelShape RAW_LR = Shapes.or(HALF_SLAB, mirrorX(FURNACE_CORE));
    private static final VoxelShape RAW_UL = Shapes.or(TOP_SLAB, FURNACE_CHIMNEY);
    private static final VoxelShape RAW_UR = Shapes.or(mirrorX(TOP_SLAB), mirrorX(FURNACE_CHIMNEY));

    /**
     * Computes the block's voxel shape according to its facing, upper/lower half, and multi-block side.
     *
     * The returned shape corresponds to the appropriate quadrant of the 2×2 workbench model and is rotated to match the block's facing.
     *
     * @param state   the block state used to determine facing, half (upper/lower), and left/single vs right side
     * @param level   the world context (unused for shape selection but provided by the framework)
     * @param pos     the block position (unused for shape selection but provided by the framework)
     * @param context the collision context (unused for shape selection but provided by the framework)
     * @return the voxel shape for this block state, rotated to the block's facing direction
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        boolean isUpper = state.getValue(HALF) == DoubleBlockHalf.UPPER;
        boolean isLeftSide = state.getValue(TYPE) == ChestType.LEFT || state.getValue(TYPE) == ChestType.SINGLE;

        VoxelShape baseShape = isUpper 
            ? (isLeftSide ? RAW_UL : RAW_UR) 
            : (isLeftSide ? RAW_LL : RAW_LR);

        // Call the method now living in AbstractWorkbench
        return rotateShape(dir, baseShape);
    }

    /**
     * Create a new VoxelShape that is the horizontal mirror of the given shape across the West/East (X) axis.
     *
     * @param shape the original VoxelShape to mirror
     * @return the mirrored VoxelShape with X coordinates reflected across the block's center
     */
    public static VoxelShape mirrorX(VoxelShape shape) {
        VoxelShape[] result = { Shapes.empty() };

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            // Mirror the X coordinates: 
            // The new minX is 1.0 minus the old maxX
            // The new maxX is 1.0 minus the old minX
            // (Note: VoxelShape uses 0.0-1.0 scale internally, not 0-16)
            VoxelShape flippedBox = Block.box(
                (1.0 - maxX) * 16.0, 
                minY * 16.0, 
                minZ * 16.0, 
                (1.0 - minX) * 16.0, 
                maxY * 16.0, 
                maxZ * 16.0
            );
            result[0] = Shapes.or(result[0], flippedBox);
        });

        return result[0];
    }
}