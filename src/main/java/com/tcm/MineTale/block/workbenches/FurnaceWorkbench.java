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
     * Ensures the block is rendered using its model so the 2x2 workbench model is visible.
     *
     * @return {@code RenderShape.MODEL} to render the block with its model
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        // Essential so that the 2x2 model is visible
        return RenderShape.MODEL;
    }

    protected static final VoxelShape LOWER_LEFT_SHAPE = Shapes.or(
        Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),  // Base
        Block.box(2.0, 8.0, 2.0, 16.0, 16.0, 14.0)  // Mid-section
    );

    protected static final VoxelShape LOWER_RIGHT_SHAPE = Shapes.or(
        Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),  // Base
        Block.box(0.0, 8.0, 2.0, 13.0, 16.0, 14.0)  // Mid-section (Shifted X: 29-16=13)
    );

    protected static final VoxelShape UPPER_LEFT_SHAPE = Shapes.or(
        Block.box(1.0, 0.0, 1.0, 16.0, 5.0, 14.0),   // Tabletop (flush right)
        Block.box(8.0, 5.0, 4.0, 16.0, 9.0, 12.0),   // Shelf (flush right)
        Block.box(6.0, 9.0, 3.0, 16.0, 12.0, 13.0)   // Top Cap (flush right)
    );


    protected static final VoxelShape UPPER_RIGHT_SHAPE = Shapes.or(
        Block.box(0.0, 0.0, 1.0, 15.0, 5.0, 14.0),  // Tabletop (Shifted X: 31-16=15)
        Block.box(0.0, 5.0, 4.0, 8.0, 9.0, 12.0),   // Shelf (Shifted X: 24-16=8)
        Block.box(0.0, 9.0, 3.0, 10.0, 12.0, 13.0)  // Top Cap (Shifted X: 26-16=10)
    );

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        DoubleBlockHalf half = state.getValue(HALF);
        ChestType type = state.getValue(TYPE);
        
        // We select the correct pre-defined VoxelShape based on position
        if (half == DoubleBlockHalf.LOWER) {
            return (type == ChestType.LEFT || type == ChestType.SINGLE) 
                ? LOWER_LEFT_SHAPE : LOWER_RIGHT_SHAPE;
        } else {
            return (type == ChestType.LEFT || type == ChestType.SINGLE) 
                ? UPPER_LEFT_SHAPE : UPPER_RIGHT_SHAPE;
        }
    }
}