package com.tcm.MineTale.block.workbenches;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// ChestBlock

public class CampfireWorkbench extends AbstractWorkbench<BlockEntity> {
    public static final boolean IS_WIDE = false;
    public static final boolean IS_TALL = false;

    public static final MapCodec<CampfireWorkbench> CODEC = simpleCodec((properties) -> 
        new CampfireWorkbench(properties, () -> null));

    public CampfireWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.CAMPFIRE_WORKBENCH_BE, IS_WIDE, IS_TALL);
    }

    public CampfireWorkbench(Properties properties, Supplier<BlockEntityType<? extends BlockEntity>> supplier) {
        // isWide = false, isTall = false (1x1 footprint)
        super(properties, supplier, IS_WIDE, IS_TALL);
    }

    @Override
    protected MapCodec<? extends CampfireWorkbench> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // BaseEntityBlock defaults to INVISIBLE. 
        // We set it to MODEL so the JSON model is rendered.
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // For a 1x1, we always create the Block Entity at this position.
        return this.blockEntityType.get().create(pos, state);
    }

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 7, 16);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
}
