package com.tcm.MineTale.block.workbenches.entity;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractWorkbenchEntity extends BlockEntity implements MenuProvider {
    protected int tier = 1;
    protected double scanRadius = 5.0;

    public AbstractWorkbenchEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // --- TIER SYSTEM ---
    public int getTier() { return tier; }
    public void setTier(int tier) { this.tier = tier; setChanged(); }

    // --- CHEST SCANNING ---
    public List<Container> getNearbyInventories() {
        List<Container> inventories = new ArrayList<>();
        if (level == null) {
            return inventories;
        } 
        BlockPos.betweenClosed(
            worldPosition.offset((int)-scanRadius, -2, (int)-scanRadius),
            worldPosition.offset((int)scanRadius, 2, (int)scanRadius)
        ).forEach(pos -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof Container container) {
                inventories.add(container);
            }
        });
        
        // Prioritization: Sort by proximity to prevent "chest prioritization" issues
        inventories.sort((a, b) -> {
            double distA = ((BlockEntity)a).getBlockPos().distSqr(this.worldPosition);
            double distB = ((BlockEntity)b).getBlockPos().distSqr(this.worldPosition);
            return Double.compare(distA, distB);
        });
        
        return inventories;
    }

    // --- RECYCLER LOGIC ---
    public void attemptRecycle(ItemStack stack) {
        // Logic to break down stack.getItem() and return components 
        // to nearby chests or drop them at worldPosition.
    }

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player);

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    
}
