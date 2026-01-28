package com.tcm.MineTale.block.workbenches.entity;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CampfireWorkbenchEntity extends AbstractWorkbenchEntity {
    /**
     * Creates a CampfireWorkbenchEntity at the given position with the provided block state.
     *
     * Initializes the entity's scan radius to 6.0 and tier to 1.
     *
     * @param blockPos   the world position of this block entity
     * @param blockState the block state for this block entity
     */
    public CampfireWorkbenchEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CAMPFIRE_WORKBENCH_BE, blockPos, blockState);

        this.scanRadius = 6.0; 
        this.tier = 1;
    }

    /**
     * Creates the server-side container menu for this workbench for the given player and window ID.
     *
     * @param syncId the window synchronization ID provided by the client
     * @param playerInventory the player's inventory
     * @param player the player opening the menu
     * @return the created {@link AbstractContainerMenu}, or `null` if no menu should be opened
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMenu'");
    }
}