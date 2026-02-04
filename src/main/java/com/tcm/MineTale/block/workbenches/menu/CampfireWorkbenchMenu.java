package com.tcm.MineTale.block.workbenches.menu;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.registry.ModMenuTypes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;

public class CampfireWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerDataSize = 4;

    private final AbstractWorkbenchEntity blockEntity;
    
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
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(containerDataSize), null);
    }

    /**
     * Creates a CampfireWorkbenchMenu bound to the given player inventory, container, and container data.
     *
     * @param syncId the synchronization id for this menu (used by the client/server container sync)
     * @param playerInventory the player's inventory
     * @param container the backing container for the workbench slots
     * @param data the container data used for syncing additional numeric state
     */
    public CampfireWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, @Nullable AbstractWorkbenchEntity blockEntity) {
        super(ModMenuTypes.CAMPFIRE_WORKBENCH_MENU, syncId, container, data, containerDataSize, playerInventory, Constants.INPUT_START + 1, 6);
        this.blockEntity = blockEntity;
    }

    @Override
    public @Nullable AbstractWorkbenchEntity getBlockEntity() {
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
        return RecipeBookType.CRAFTING;
    }
    
    @Override
    public WorkbenchRecipeInput createRecipeInput() {
        // We grab the items currently sitting in the container at indices 0 and 1
        // These correspond to the "Left" and "Right" input slots added in your constructor
        return new WorkbenchRecipeInput(
            this.container.getItem(Constants.INPUT_START), 
            this.container.getItem(this.inputEnd)
        );
    }
}