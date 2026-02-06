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
     * Client-side constructor used for initialization when the menu is opened.
     */
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainerData(EMPTY_SIZE), null);
    }

    /**
     * Creates a workbench menu bound to the given player inventory, container, container data, and optional block entity.
     *
     * @param syncId synchronization id for this menu
     * @param playerInventory the player's inventory
     * @param container backing container for the workbench slots
     * @param data container data used to sync numeric state
     * @param blockEntity optional block entity this menu is bound to, or `null` if not bound
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
     * Populate the provided StackedItemContents with the items currently present in this menu's crafting slots so the recipe book can evaluate available recipes.
     *
     * @param stackedItemContents container to be filled with consolidated item counts from the menu's crafting/container slots
     */
    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        // This is vital for the recipe book to "see" what is currently in your furnace.
        // It allows the book to calculate if you have enough items to craft more.
        this.playerInventory.fillStackedContents(stackedItemContents);
    }

    /**
     * Identifies the recipe book category used by this menu.
     *
     * @return the crafting recipe book type, `RecipeBookType.CRAFTING`.
     */
    @Override
    public RecipeBookType getRecipeBookType() {
        // This keeps the Crafting-style recipe book available on the UI
        return RecipeBookType.CRAFTING;
    }
    
    /**
     * Creates a WorkbenchRecipeInput using the current items in the menu's left and right input slots.
     *
     * The left input is read from index {@code Constants.INPUT_START} and the right input from {@code inputEnd}.
     *
     * @return a WorkbenchRecipeInput containing the items currently in the left and right input slots
     */
    @Override
    public WorkbenchRecipeInput createRecipeInput() {
        // Since there are no slots, we return an empty input.
        // The actual crafting logic will scan the player inventory directly when a button is clicked.
        return new WorkbenchRecipeInput(ItemStack.EMPTY, ItemStack.EMPTY);
    }
}