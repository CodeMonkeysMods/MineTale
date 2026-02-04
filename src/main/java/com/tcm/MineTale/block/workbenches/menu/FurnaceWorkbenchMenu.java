package com.tcm.MineTale.block.workbenches.menu;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractFurnaceWorkbenchEntity;
import com.tcm.MineTale.registry.ModMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;

public class FurnaceWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerDataSize = 4;

    private final AbstractFurnaceWorkbenchEntity blockEntity;

    /**
     * Creates a client-side FurnaceWorkbenchMenu with a new internal container and container data.
     *
     * @param syncId          the window synchronization id assigned by the client
     * @param playerInventory the player's inventory to attach to this menu
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(6 + 1), new SimpleContainerData(containerDataSize), null);
    }

    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, @Nullable AbstractFurnaceWorkbenchEntity blockEntity) {
        super(ModMenuTypes.FURNACE_WORKBENCH_MENU, syncId, container, data, containerDataSize, playerInventory, 2, 6);
        this.blockEntity = blockEntity;
    }

    @Override
    public @Nullable AbstractFurnaceWorkbenchEntity getBlockEntity() {
        // Return the block entity instance you passed into this menu's constructor
        return this.blockEntity;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        // This is vital for the recipe book to "see" what is currently in your furnace.
        // It allows the book to calculate if you have enough items to craft more.
        if (this.container instanceof StackedContentsCompatible compatible) {
            compatible.fillStackedContents(stackedItemContents);
        }
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        // Tells the game which tab/category of the recipe book to save your settings under.
        // Even though it's a custom furnace, using FURNACE ensures it behaves like one.
        return RecipeBookType.FURNACE;
    }
}