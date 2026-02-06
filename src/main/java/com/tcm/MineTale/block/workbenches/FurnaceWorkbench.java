package com.tcm.MineTale.block.workbenches;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tcm.MineTale.block.workbenches.entity.FurnaceWorkbenchEntity;
import com.tcm.MineTale.registry.ModTiers;
import com.tcm.MineTale.registry.ModTiers.FurnaceTier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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

    /**
     * Create a block entity for the master block of this workbench.
     *
     * Only the master block of the multi-block workbench receives an entity; other positions return {@code null}.
     *
     * @return the block entity for the master block ({@link FurnaceWorkbenchEntity}), or {@code null} if this position does not host an entity
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // AbstractWorkbench logic ensures only the Master block gets the entity.
        // We override it here to point specifically to our Furnace entity.
        return super.newBlockEntity(pos, state);
    }
}