package com.tcm.MineTale.block.workbenches.menu;

import com.tcm.MineTale.registry.ModMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class CampfireWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerSize = 7;
    private static final int containerDataSize = 4;
    
    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(containerSize), new SimpleContainerData(containerDataSize));
    }

    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, syncId, container, data, containerSize, containerDataSize, playerInventory);
    }
}
