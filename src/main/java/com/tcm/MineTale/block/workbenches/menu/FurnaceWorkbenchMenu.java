package com.tcm.MineTale.block.workbenches.menu;

import com.tcm.MineTale.registry.ModMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class FurnaceWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerSize = 7;
    private static final int containerDataSize = 4;

    /**
     * Creates a client-side FurnaceWorkbenchMenu with a new internal container and container data.
     *
     * @param syncId          the window synchronization id assigned by the client
     * @param playerInventory the player's inventory to attach to this menu
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(containerSize), new SimpleContainerData(containerDataSize));
    }

    /**
     * Initializes a server-side furnace-workbench menu for a player, setting up the container slots
     * and attaching progress synchronization data.
     *
     * @param syncId         the window synchronization id assigned by the server
     * @param playerInventory the player's Inventory used to populate player inventory and hotbar slots
     * @param container      the backing container (expected size 7) that provides the workbench slots
     * @param data           the ContainerData (expected count 4) used to sync cook and burn progress
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.FURNACE_WORKBENCH_MENU, syncId, container, data, containerSize, containerDataSize, playerInventory);
    }
}