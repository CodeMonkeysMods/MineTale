package com.tcm.MineTale.block.workbenches;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.AlchemistsWorkbenchEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class AlchemistsWorkbench extends AbstractWorkbench<AlchemistsWorkbenchEntity> {
    public static final boolean IS_WIDE = true;
    public static final boolean IS_TALL = false;

    public static final MapCodec<AlchemistsWorkbench> CODEC = simpleCodec(AlchemistsWorkbench::new);

    /**
     * Create a new AlchemistsWorkbench that is configured to use the mod's Alchemists Workbench block entity type.
     *
     * @param properties block properties for this workbench
     */
    public AlchemistsWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.ALCHEMISTS_WORKBENCH_BE, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Create an AlchemistsWorkbench with the given block properties and a supplier for its block-entity type.
     *
     * @param properties block properties applied to this workbench
     * @param supplier   supplier that provides the BlockEntityType for the AlchemistsWorkbenchEntity
     */
    public AlchemistsWorkbench(Properties properties, Supplier<BlockEntityType<? extends AlchemistsWorkbenchEntity>> supplier) {
        super(properties, supplier, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Provides a ticker for workbench block entities when the supplied block entity type matches this block's entity type.
     *
     * @param type the block entity type to match against this block's workbench entity type
     * @return a BlockEntityTicker that updates matching workbench block entities, or {@code null} if the types do not match
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // This connects the Level's ticking system to your static tick method
        return createTickerHelper(type, ModBlockEntities.ALCHEMISTS_WORKBENCH_BE, AbstractWorkbenchEntity::tick);
    }

    /**
     * Provide the MapCodec used for serialisation and deserialisation of this workbench.
     *
     * @return the MapCodec for this AlchemistsWorkbench
     */
    @Override
    protected MapCodec<? extends AlchemistsWorkbench> codec() {
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