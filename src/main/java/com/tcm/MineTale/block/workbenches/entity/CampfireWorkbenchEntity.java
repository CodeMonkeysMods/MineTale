package com.tcm.MineTale.block.workbenches.entity;

import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CampfireWorkbenchEntity extends BlockEntity {
    public CampfireWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CAMPFIRE_WORKBENCH_BE, blockPos, blockState);
    }
}
