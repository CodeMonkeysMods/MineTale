package com.tcm.MineTale.block.workbenches;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.entity.CampfireWorkbenchEntity;
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

public class CampfireWorkbench extends AbstractWorkbench<CampfireWorkbenchEntity> {
    public static final boolean IS_WIDE = false;
    public static final boolean IS_TALL = false;

    public static final MapCodec<CampfireWorkbench> CODEC = simpleCodec(CampfireWorkbench::new);

    /**
     * Creates a CampfireWorkbench configured to use the mod's CAMPFIRE_WORKBENCH_BE block entity type.
     *
     * @param properties block properties used to construct this workbench
     */
    public CampfireWorkbench(Properties properties) {
        // Hardcode the supplier and sounds here if they never change
        super(properties, () -> ModBlockEntities.CAMPFIRE_WORKBENCH_BE, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Constructs a CampfireWorkbench using the provided block properties and block-entity type supplier.
     *
     * @param properties block properties to apply to this workbench
     * @param supplier   supplier that provides the BlockEntityType for the CampfireWorkbenchEntity
     */
    public CampfireWorkbench(Properties properties, Supplier<BlockEntityType<? extends CampfireWorkbenchEntity>> supplier) {
        // isWide = false, isTall = false (1x1 footprint)
        super(properties, supplier, IS_WIDE, IS_TALL, 1);
    }

    /**
     * Provides a ticker that updates campfire workbench block entities each tick.
     *
     * @return a BlockEntityTicker that invokes AbstractWorkbenchEntity.tick for CampfireWorkbenchEntity instances, or `null` if the supplied block entity type does not match the campfire workbench type.
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // This connects the Level's ticking system to your static tick method
        return createTickerHelper(type, ModBlockEntities.CAMPFIRE_WORKBENCH_BE, AbstractWorkbenchEntity::tick);
    }

    /**
     * The codec used to serialize and deserialize this CampfireWorkbench type.
     *
     * @return the MapCodec for this CampfireWorkbench
     */
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
     * Create a block entity for the master block of this workbench.
     *
     * Only the master block of the multi-block workbench receives an entity; other positions return {@code null}.
     *
     * @return the block entity for the master block ({@link CampfireWorkbenchEntity}), or {@code null} if this position does not host an entity
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // AbstractWorkbench logic ensures only the Master block gets the entity.
        // We override it here to point specifically to our Furnace entity.
        return super.newBlockEntity(pos, state);
    }

    // TODO: Check if we need this
    // /**
    //  * Compute the master (base) block position for this block based on its state.
    //  *
    //  * @param state the block state of the current block
    //  * @param pos   the position of the current block
    //  * @return the position of the master (base) block: if the block is the upper half, the block below is used; if the block's type is `RIGHT`, the position is offset one block counterclockwise from its facing direction; otherwise the original position
    //  */
    // public BlockPos getMasterPos(BlockState state, BlockPos pos) {
    //     BlockPos master = pos;
    //     Direction facing = state.getValue(FACING);
    //     if (state.getValue(HALF) == DoubleBlockHalf.UPPER) master = master.below();
    //     if (state.getValue(TYPE) == ChestType.RIGHT) master = master.relative(facing.getCounterClockWise());
    //     return master;
    // }
    
}