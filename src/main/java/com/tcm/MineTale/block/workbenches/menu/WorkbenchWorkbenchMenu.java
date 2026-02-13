package com.tcm.MineTale.block.workbenches.menu;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.registry.ModMenuTypes;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class WorkbenchWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    // No internal inventory needed anymore, but we pass an empty container to the super
    private static final int EMPTY_SIZE = 0;
    private static final int DATA_SIZE = 0;

    @Nullable
    private final AbstractWorkbenchEntity blockEntity;
    private final Inventory playerInventory;
    
   /**
     * Creates a client-side menu instance when the workbench UI is opened.
     *
     * @param syncId the synchronization id used to match this menu with the server
     * @param playerInventory the player's inventory bound to this menu
     */
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainerData(EMPTY_SIZE), null);
    }

    /**
     * Creates a workbench menu associated with the given player inventory and optional block entity.
     *
     * Uses an empty internal container (size 0) and the class's data size for syncing numeric state.
     *
     * @param syncId synchronization id for this menu
     * @param playerInventory the player's inventory used for slot access and recipe-book integration
     * @param data container data used to sync numeric state between server and client
     * @param blockEntity nullable block entity this menu is bound to, or {@code null} if not bound
     */
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory, ContainerData data, @Nullable AbstractWorkbenchEntity blockEntity) {
        // Note: The order of arguments depends on your AbstractWorkbenchContainerMenu,
        // but the 'expectedSize' parameter MUST be 0.
        super(
            ModMenuTypes.WORKBENCH_WORKBENCH_MENU, 
            syncId, 
            new SimpleContainer(EMPTY_SIZE),
            data, 
            DATA_SIZE, 
            playerInventory, 
            EMPTY_SIZE,
            EMPTY_SIZE
        );
        this.blockEntity = blockEntity;
        this.playerInventory = playerInventory;
    }

    /**
     * Accesses the block entity bound to this menu, if present.
     *
     * @return the bound AbstractWorkbenchEntity, or {@code null} if this menu is not bound to a block entity
     */
    @Override
    public @Nullable AbstractWorkbenchEntity getBlockEntity() {
        return this.blockEntity;
    }

    /**
     * Populate the given StackedItemContents with the items available through this menu for recipe-book calculations.
     *
     * @param stackedItemContents container to receive consolidated item counts from the menu's inventories
     */
    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        // This is vital for the recipe book to "see" what is currently in your workbench.
        // It allows the book to calculate if you have enough items to craft more.
        this.playerInventory.fillStackedContents(stackedItemContents);
    }

    /**
     * Selects the crafting recipe-book category for this menu.
     *
     * @return {@code RecipeBookType.CRAFTING}
     */
    @Override
    public RecipeBookType getRecipeBookType() {
        // This keeps the Crafting-style recipe book available on the UI
        return RecipeBookType.CRAFTING;
    }
    
    /**
     * Create the recipe input used by this menu's crafting UI; this implementation provides an empty input.
     *
     * @return a WorkbenchRecipeInput with both input stacks set to ItemStack.EMPTY
     */
    @Override
    public WorkbenchRecipeInput createRecipeInput() {
        // Since there are no slots, we return an empty input.
        // The actual crafting logic will scan the player inventory directly when a button is clicked.
        return new WorkbenchRecipeInput(ItemStack.EMPTY, ItemStack.EMPTY);
    }
}