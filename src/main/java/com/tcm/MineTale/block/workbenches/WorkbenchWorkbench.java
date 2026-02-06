package com.tcm.MineTale.block.workbenches;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.WorkbenchWorkbenchEntity;
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

public class WorkbenchWorkbench extends AbstractWorkbench<WorkbenchWorkbenchEntity> {
    public static final boolean IS_WIDE = false;
    public static final boolean IS_TALL = false;

    public static final MapCodec<WorkbenchWorkbench> CODEC = simpleCodec(WorkbenchWorkbench::new);

    /**
     * Constructs a WorkbenchWorkbench that uses the mod's WORKBENCH_WORKBENCH_BE block entity type.
     *
     * @param properties block properties for this workbench
     */
    public WorkbenchWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.WORKBENCH_WORKBENCH_BE, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Constructs a WorkbenchWorkbench using the provided block properties and block-entity type supplier.
     *
     * @param properties block properties to apply to this workbench
     * @param supplier   supplier that provides the BlockEntityType for the WorkbenchWorkbenchEntity
     */
    public WorkbenchWorkbench(Properties properties, Supplier<BlockEntityType<? extends WorkbenchWorkbenchEntity>> supplier) {
        // isWide = false, isTall = false (1x1 footprint)
        super(properties, supplier, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Supplies a ticker that updates WorkbenchWorkbench block entities each tick.
     *
     * @return a BlockEntityTicker that invokes AbstractWorkbenchEntity.tick for matching WorkbenchWorkbenchEntity instances, `null` if the supplied block entity type does not match
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // This connects the Level's ticking system to your static tick method
        return createTickerHelper(type, ModBlockEntities.WORKBENCH_WORKBENCH_BE, AbstractWorkbenchEntity::tick);
    }

    /**
     * Provides the MapCodec used to serialize and deserialize this workbench.
     *
     * @return the MapCodec for this WorkbenchWorkbench
     */
    @Override
    protected MapCodec<? extends WorkbenchWorkbench> codec() {
        return CODEC;
    }

    /**
     * Specifies that this block is rendered using its block model.
     *
     * @return RenderShape.MODEL to render the block using its JSON/model representation.
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        // BaseEntityBlock defaults to INVISIBLE. 
        // We set it to MODEL so the JSON model is rendered.
        return RenderShape.MODEL;
    }

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 7, 16);

    /**
     * Provides the block's collision and interaction shape.
     *
     * <p>Uses a fixed voxel shape covering a 1×1 footprint with a height of 7 units (coordinates: x 0–16, y 0–7, z 0–16).</p>
     *
     * @return the voxel shape used for collision and interaction
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}