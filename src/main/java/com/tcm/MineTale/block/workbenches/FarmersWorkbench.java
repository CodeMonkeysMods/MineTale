package com.tcm.MineTale.block.workbenches;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.FarmersWorkbenchEntity;
import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// ChestBlock

public class FarmersWorkbench extends AbstractWorkbench<FarmersWorkbenchEntity> {
    public static final boolean IS_WIDE = true;
    public static final boolean IS_TALL = false;

    public static final MapCodec<FarmersWorkbench> CODEC = simpleCodec(FarmersWorkbench::new);

    /**
     * Constructs a FarmersWorkbench that uses the mod's FARMERS_WORKBENCH_BE block entity type.
     *
     * @param properties block properties for this workbench
     */
    public FarmersWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.FARMERS_WORKBENCH_BE, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Constructs a FarmersWorkbench using the provided block properties and block-entity type supplier.
     *
     * @param properties block properties to apply to this workbench
     * @param supplier   supplier that provides the BlockEntityType for the FarmersWorkbenchEntity
     */
    public FarmersWorkbench(Properties properties, Supplier<BlockEntityType<? extends FarmersWorkbenchEntity>> supplier) {
        super(properties, supplier, IS_WIDE, IS_TALL, 1);
    }

    /**
         * Return a ticker for this workbench's block entity when the supplied block entity type matches.
         *
         * @param type the block entity type to match against this workbench's entity type
         * @return the BlockEntityTicker that updates matching workbench block entities, or {@code null} if the supplied type does not match
         */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // This connects the Level's ticking system to your static tick method
        return createTickerHelper(type, ModBlockEntities.FARMERS_WORKBENCH_BE, AbstractWorkbenchEntity::tick);
    }

    /**
     * Provides the MapCodec used to serialize and deserialize this workbench.
     *
     * @return the MapCodec for this FarmersWorkbench
     */
    @Override
    protected MapCodec<? extends FarmersWorkbench> codec() {
        return CODEC;
    }

    /**
     * Indicates the block should be rendered using its block model.
     *
     * @return `RenderShape.MODEL` when the block uses its JSON/model representation.
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        // BaseEntityBlock defaults to INVISIBLE. 
        // We set it to MODEL so the JSON model is rendered.
        return RenderShape.MODEL;
    }

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    /**
     * The block's collision and interaction shape as a 1×1 footprint (x 0–16, y 0–16, z 0–16).
     *
     * @return the voxel shape used for collision and interaction
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}