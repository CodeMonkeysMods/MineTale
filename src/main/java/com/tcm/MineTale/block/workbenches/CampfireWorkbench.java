package com.tcm.MineTale.block.workbenches;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.workbenches.entity.CampfireWorkbenchEntity;
import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// ChestBlock

public class CampfireWorkbench extends AbstractWorkbench<CampfireWorkbenchEntity> {
    public static final boolean IS_WIDE = false;
    public static final boolean IS_TALL = false;

    public static final MapCodec<CampfireWorkbench> CODEC = simpleCodec((properties) -> 
        new CampfireWorkbench(properties, () -> null));

    public CampfireWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.CAMPFIRE_WORKBENCH_BE, IS_WIDE, IS_TALL);
    }

    public CampfireWorkbench(Properties properties, Supplier<BlockEntityType<? extends CampfireWorkbenchEntity>> supplier) {
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

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 7, 16);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Only spawn the entity at the "Master" position (LOWER + LEFT or LOWER + SINGLE)
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(TYPE) != ChestType.RIGHT) {
            return blockEntityType.get().create(pos, state);
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        // BlockPos masterPos = getMasterPos(state, pos);
        // BlockEntity be = level.getBlockEntity(masterPos);

        // if (be instanceof AbstractWorkbenchEntity) {
        //     // Open UI or handle Recycling logic here
        //     // Example: if player is holding a tool, try to recycle it
        //     return InteractionResult.CONSUME;
        // }

        return InteractionResult.PASS;
    }

    public BlockPos getMasterPos(BlockState state, BlockPos pos) {
        BlockPos master = pos;
        Direction facing = state.getValue(FACING);
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) master = master.below();
        if (state.getValue(TYPE) == ChestType.RIGHT) master = master.relative(facing.getCounterClockWise());
        return master;
    }
    
}
