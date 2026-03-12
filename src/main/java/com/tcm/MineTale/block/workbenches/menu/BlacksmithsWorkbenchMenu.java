package com.tcm.MineTale.block.workbenches.menu;

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
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BlacksmithsWorkbenchMenu extends AbstractWorkbenchContainerMenu {
    // No internal inventory needed anymore, but we pass an empty container to the super
    private static final int EMPTY_SIZE = 0;
    private static final int DATA_SIZE = 0;

    @Nullable
    private final AbstractWorkbenchEntity blockEntity;
    private final Inventory playerInventory;

   /**
     * Constructs a client-side BlacksmithsWorkbenchMenu for the player's workbench UI.
     *
     * Convenience constructor used on the client; delegates to the main constructor with empty container data and no bound block entity.
     *
     * @param syncId           the synchronisation id used to match this menu with the server
     * @param playerInventory  the player's inventory to bind for slot access
     */
    public BlacksmithsWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainerData(EMPTY_SIZE), null);
    }

    /**
     * Creates a workbench menu bound to the given player inventory and optional block entity.
     *
     * Uses an empty internal container (size 0) and the class's data size to synchronise numeric state.
     *
     * @param syncId the synchronisation id for this menu
     * @param playerInventory the player's inventory used for slot access and recipe-book integration
     * @param data container data used to synchronise numeric state between server and client
     * @param blockEntity nullable block entity this menu is bound to, or {@code null} if not bound
     */
    public BlacksmithsWorkbenchMenu(int syncId, Inventory playerInventory, ContainerData data, @Nullable AbstractWorkbenchEntity blockEntity) {
        // Note: The order of arguments depends on your AbstractWorkbenchContainerMenu,
        // but the 'expectedSize' parameter MUST be 0.
        super(
            ModMenuTypes.BLACKSMITHS_WORKBENCH_MENU,
            syncId, 
            new SimpleContainer(EMPTY_SIZE),
            data, 
            DATA_SIZE, 
            playerInventory, 
            EMPTY_SIZE,
            EMPTY_SIZE,
            ModRecipes.BLACKSMITHS_TYPE
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
     * Populate the provided StackedItemContents with item stacks available for crafting lookups.
     *
     * Accounts stacks from the bound player inventory, this menu's internal container and any
     * networked nearby item stacks supplied to the menu.
     *
     * @param contents the StackedItemContents to populate with accounted item stacks
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
         * Indicates that the crafting recipe book should be displayed for this menu.
         *
         * @return `RecipeBookType.CRAFTING`
         */
    @Override
    public RecipeBookType getRecipeBookType() {
        // This keeps the Crafting-style recipe book available on the UI
        return RecipeBookType.CRAFTING;
    }
    
    /**
     * Creates an empty recipe input for this menu's crafting UI.
     *
     * Both input slots are empty because the menu does not provide internal crafting slots; crafting reads from the player inventory when invoked.
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