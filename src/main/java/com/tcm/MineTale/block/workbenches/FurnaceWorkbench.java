package com.tcm.MineTale.block.workbenches;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.workbenches.entity.FurnaceWorkbenchEntity;
import com.tcm.MineTale.registry.ModBlockEntities;

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

    public static final MapCodec<FurnaceWorkbench> CODEC = simpleCodec(FurnaceWorkbench::new);

    /**
     * Standard constructor for registration.
     */
    public FurnaceWorkbench(Properties properties) {
        super(properties, () -> ModBlockEntities.FURNACE_WORKBENCH_BE, IS_WIDE, IS_TALL);
    }

    /**
     * Flexible constructor allowing for specialized Block Entity Types.
     */
    public FurnaceWorkbench(Properties properties, Supplier<BlockEntityType<? extends FurnaceWorkbenchEntity>> supplier) {
        super(properties, supplier, IS_WIDE, IS_TALL);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // Essential so that the 2x2 model is visible
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Only the Master block (Lower-Left) should tick to process smelting
        // This helper ensures the logic only runs on the Server side for our specific BE
        return createTickerHelper(type, ModBlockEntities.FURNACE_WORKBENCH_BE, (lvl, pos, st, be) -> {
            if (be instanceof FurnaceWorkbenchEntity furnace) {
                furnace.tick(lvl, pos, st);
            }
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // AbstractWorkbench logic ensures only the Master block gets the entity.
        // We override it here to point specifically to our Furnace entity.
        return super.newBlockEntity(pos, state);
    }

	@Override
    protected MapCodec<? extends FurnaceWorkbench> codec() {
        return CODEC;
    }
    
}
