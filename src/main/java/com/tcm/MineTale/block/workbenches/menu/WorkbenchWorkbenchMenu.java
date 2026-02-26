package com.tcm.MineTale.block.workbenches.menu;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.registry.ModMenuTypes;
import com.tcm.MineTale.registry.ModRecipes;

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
     * Creates a client-side WorkbenchWorkbenchMenu for the opened workbench UI.
     *
     * @param syncId          the synchronization id that matches this menu to the server-side menu
     * @param playerInventory the player's inventory to bind to this menu
     */
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainerData(EMPTY_SIZE), null);
    }

    /**
     * Creates a workbench menu bound to a player's inventory and an optional block entity.
     *
     * Uses an empty internal container and the class's data size for numeric state synchronization.
     *
     * @param syncId           synchronization id for this menu
     * @param playerInventory  the player's inventory used for slot access and recipe-book integration
     * @param data             container data used to sync numeric state between server and client
     * @param blockEntity      nullable block entity this menu is bound to, or {@code null} if not bound
     */
    public WorkbenchWorkbenchMenu(int syncId, Inventory playerInventory, ContainerData data, @Nullable AbstractWorkbenchEntity blockEntity) {        // Note: The order of arguments depends on your AbstractWorkbenchContainerMenu,
        // but the 'expectedSize' parameter MUST be 0.
        super(
            ModMenuTypes.WORKBENCH_WORKBENCH_MENU, 
            syncId, 
            new SimpleContainer(EMPTY_SIZE),
            data, 
            DATA_SIZE, 
            playerInventory, 
            EMPTY_SIZE,
            EMPTY_SIZE,
            ModRecipes.WORKBENCH_TYPE
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
     * Populate the given StackedItemContents with all item stacks available to the crafting UI.
     *
     * Accounts for items in the player's inventory, items in this menu's internal container,
     * and nearby item stacks synchronised from the server so the recipe book can consider them.
     *
     * @param contents the StackedItemContents to populate with available item stacks
     */
    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents contents) {
        // 1. Account for items in the player's pockets
        this.playerInventory.fillStackedContents(contents);
        
        // 2. Account for items sitting in the Workbench slots (if any)
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            contents.accountStack(this.container.getItem(i));
        }

        // 3. THE FIX: Use the list provided by the Packet (Networked Items)
        // We stop calling be.getNearbyInventories() here because it returns empty on Client
        List<ItemStack> nearbyItems = this.getNetworkedNearbyItems();
        
        if (!nearbyItems.isEmpty() && this.playerInventory.player.level().isClientSide()) {
            System.out.println("DEBUG: Recipe Book is now accounting for " + nearbyItems.size() + " stacks from the packet!");
        }

        for (ItemStack stack : nearbyItems) {
            contents.accountStack(stack);
        }
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