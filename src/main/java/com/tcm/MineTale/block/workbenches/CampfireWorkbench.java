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

    /**
     * Creates a CampfireWorkbench configured to use the mod's CAMPFIRE_WORKBENCH_BE block entity type.
     *
     * @param properties block properties used to construct this workbench
     */
    public CampfireWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.CAMPFIRE_WORKBENCH_BE, IS_WIDE, IS_TALL);
    }

    /**
     * Creates a CampfireWorkbench with the given block properties and block-entity type supplier.
     *
     * @param properties the block's properties
     * @param supplier   supplier that provides the BlockEntityType for this workbench
     */
    public CampfireWorkbench(Properties properties, Supplier<BlockEntityType<? extends CampfireWorkbenchEntity>> supplier) {
        // isWide = false, isTall = false (1x1 footprint)
        super(properties, supplier, IS_WIDE, IS_TALL);
    }

    @Override
    protected MapCodec<? extends CampfireWorkbench> codec() {
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
     * Gets the block's collision and interaction shape.
     *
     * @return the voxel shape representing the block's collision and interaction bounds
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /**
     * Creates and returns the block entity for this block only when the block represents the master
     * position (the lower half and not of type RIGHT).
     *
     * @return the created BlockEntity when this block is the master (HALF == LOWER and TYPE != RIGHT), or `null` otherwise
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Only spawn the entity at the "Master" position (LOWER + LEFT or LOWER + SINGLE)
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(TYPE) != ChestType.RIGHT) {
            return blockEntityType.get().create(pos, state);
        }
        return null;
    }

    /**
     * Handles a player's interaction with the workbench when no item is used.
     *
     * <p>On the client this acknowledges the interaction. On the server this method
     * is a hook for workbench-specific handling; if the workbench processes the
     * interaction it will consume it, otherwise the interaction is passed to other handlers.</p>
     *
     * @param state the block state of the workbench
     * @param level the world in which the interaction occurs
     * @param pos   the position of the interacted block
     * @param player the player performing the interaction
     * @param hit   the hit result describing the interaction point
     * @return {@code InteractionResult.SUCCESS} on client, {@code InteractionResult.CONSUME} if handled by the workbench, or {@code InteractionResult.PASS} otherwise
     */
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

    /**
     * Compute the master (base) block position for this block based on its state.
     *
     * @param state the block state of the current block
     * @param pos   the position of the current block
     * @return the position of the master (base) block: if the block is the upper half, the block below is used; if the block's type is `RIGHT`, the position is offset one block counterclockwise from its facing direction; otherwise the original position
     */
    public BlockPos getMasterPos(BlockState state, BlockPos pos) {
        BlockPos master = pos;
        Direction facing = state.getValue(FACING);
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) master = master.below();
        if (state.getValue(TYPE) == ChestType.RIGHT) master = master.relative(facing.getCounterClockWise());
        return master;
    }
    
}