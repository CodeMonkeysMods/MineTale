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

public class WorkbenchWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    private static final int containerDataSize = 4;

    @Nullable
    private final AbstractWorkbenchEntity blockEntity;
    
    /**
     * Constructs a WorkbenchWorkbenchMenu backed by default internal storage and data.
     *
     * Initializes the menu with a new 7-slot SimpleContainer and a SimpleContainerData of size
     * {@code containerDataSize}, and binds it to the provided player inventory.
     *
     * @param syncId          synchronization id for the menu
     * @param playerInventory the player's inventory interacting with this menu
     */
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(containerDataSize), null);
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
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, @Nullable AbstractWorkbenchEntity blockEntity) {
        super(ModMenuTypes.WORKBENCH_WORKBENCH_MENU, syncId, container, data, containerDataSize, playerInventory, Constants.INPUT_START + 1, 6);
        this.blockEntity = blockEntity;
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
        if (this.container instanceof StackedContentsCompatible compatible) {
            compatible.fillStackedContents(stackedItemContents);
        }
    }

    /**
     * Identifies the recipe book category used by this menu.
     *
     * @return the crafting recipe book type, `RecipeBookType.CRAFTING`.
     */
    @Override
    public RecipeBookType getRecipeBookType() {
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
        // We grab the items currently sitting in the container at indices 0 and 1
        // These correspond to the "Left" and "Right" input slots added in your constructor
        return new WorkbenchRecipeInput(
            this.container.getItem(Constants.INPUT_START), 
            this.container.getItem(this.inputEnd)
        );
    }
}