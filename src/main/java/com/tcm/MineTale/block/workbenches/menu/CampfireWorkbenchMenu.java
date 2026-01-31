package com.tcm.MineTale.block.workbenches.menu;

import com.tcm.MineTale.registry.ModMenuTypes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class CampfireWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerSize = Constants.TOTAL_SLOTS;
    private static final int containerDataSize = 4;
    
    /**
     * Creates a CampfireWorkbenchMenu using default internal storage and data containers.
     *
     * Constructs a menu with a new SimpleContainer of size {@code containerSize} and a new
     * SimpleContainerData of size {@code containerDataSize}, then delegates to the primary constructor.
     *
     * @param syncId          synchronization id for the menu
     * @param playerInventory the player's inventory interacting with this menu
     */
    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(containerSize), new SimpleContainerData(containerDataSize));
    }

    /**
     * Creates a CampfireWorkbenchMenu bound to the given player inventory, container, and container data.
     *
     * @param syncId the synchronization id for this menu (used by the client/server container sync)
     * @param playerInventory the player's inventory
     * @param container the backing container for the workbench slots
     * @param data the container data used for syncing additional numeric state
     */
    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, syncId, container, data, containerSize, containerDataSize, playerInventory);
    }
}