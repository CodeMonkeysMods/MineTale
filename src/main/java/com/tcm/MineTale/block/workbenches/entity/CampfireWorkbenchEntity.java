package com.tcm.MineTale.block.workbenches.entity;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CampfireWorkbenchEntity extends AbstractWorkbenchEntity {
    public CampfireWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CAMPFIRE_WORKBENCH_BE, blockPos, blockState);

        this.scanRadius = 6.0; 
        this.tier = 1;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMenu'");
    }
}
