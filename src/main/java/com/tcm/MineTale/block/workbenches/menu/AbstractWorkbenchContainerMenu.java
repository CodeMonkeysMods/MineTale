package com.tcm.MineTale.block.workbenches.menu;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.util.Constants;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class AbstractWorkbenchContainerMenu extends RecipeBookMenu implements StackedContentsCompatible {
    protected final Container container;
    private final ContainerData data;
    
    protected final int inputEnd;
    protected final int outputEnd;

    protected final Inventory playerInventory;

    private List<ItemStack> networkedNearbyItems = new ArrayList<>();

    /**
     * Updates the list of items available from nearby chests.
     * Called by the networking system when a sync packet arrives.
     */
    public void setNetworkedNearbyItems(List<ItemStack> items) {
        this.networkedNearbyItems = items;
    }

    /**
     * Gets the list of items found in nearby chests.
     */
    public List<ItemStack> getNetworkedNearbyItems() {
        return this.networkedNearbyItems;
    }

    /**
     * Constructs a workbench container menu, initializes inventory and sync state, and opens the container for the player.
     *
     * The constructor conditionally validates container size and attaches container data slots only when the container actually
     * contains slots (i.e., when `outputEnd >= 0` and the container size is > 0). For slotless workbenches it only verifies the
     * container is non-null with size 0. If the container has slots, workbench-specific slots are added; the player's inventory
     * and hotbar are always added. The container is opened for the provided player.
     *
     * @param menuType            the menu type or null for an unregistered type
     * @param syncId              synchronization id for the menu
     * @param container           the underlying container backing this menu
     * @param data                container data used to sync progress/state (e.g., burn/cook times)
     * @param containerDataSize   expected size of `data` when the container provides slots; used for data count validation
     * @param playerInventory     the player's inventory to attach to this menu
     * @param inputEnd            index (inclusive) of the last input slot in the container
     * @param outputEnd           index (inclusive) of the last output slot in the container; if negative or container is empty,
     *                            the menu is treated as slotless and slot/data initialization is skipped
     */
    public AbstractWorkbenchContainerMenu(@Nullable MenuType<?> menuType, int syncId, Container container, ContainerData data, int containerDataSize, Inventory playerInventory, int inputEnd, int outputEnd) {
        super(menuType, syncId);

        this.outputEnd = outputEnd;
        this.inputEnd = inputEnd;

        // FIX: Only validate size if the workbench actually expects slots.
        // If outputEnd is -1 or 0 (and container is empty), we skip or adjust the check.
        if (outputEnd >= 0 && container.getContainerSize() > 0) {
            checkContainerSize(container, outputEnd + 1);
            // Sync the cooking/fuel progress bars
            checkContainerDataCount(data, containerDataSize);
            this.addDataSlots(data);
        } else {
            // For slotless workbenches, we just ensure the container isn't null.
            checkContainerSize(container, 0);
        }

        this.container = container;
        this.data = data;
        this.playerInventory = playerInventory;

        container.startOpen(playerInventory.player);

        // Only attempt to add slots if the container actually has them
        if (container.getContainerSize() > 0) {
            this.addWorkbenchSlots(container);
        }

        // --- PLAYER INVENTORY ---
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    /**
     * Adds the workbench-specific slots to this menu: a fuel slot, two input slots, and four result slots.
     *
     * The fuel slot restricts placement to items accepted by isFuel(ItemStack). The two input slots accept any item.
     * The four output slots are result slots (FurnaceResultSlot) that deliver crafted/output items to the player.
     *
     * @param container the container that backs the workbench slots
     */
    protected void addWorkbenchSlots(Container container) {
        this.addSlot(new Slot(container, Constants.FUEL_SLOT, 44, 53) {
            /**
             * Determines whether the given item stack is allowed in the fuel slot.
             *
             * @param stack the item stack to test for fuel eligibility
             * @return `true` if the stack is accepted as fuel, `false` otherwise
             */
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isFuel(stack);
            }
        });

        // 2. Two Input Slots (Stacked on the left)
        this.addSlot(new Slot(container, Constants.INPUT_START, 35, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // If this logic is too restrictive (e.g., checking for fuel only), 
                // the Recipe Book simulation will fail.
                return true; 
            }
        }); //LEFT
        this.addSlot(new Slot(container, this.inputEnd, 53, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // If this logic is too restrictive (e.g., checking for fuel only), 
                // the Recipe Book simulation will fail.
                return true; 
            }
        }); //RIGHT


        // 3. Four Output Slots (2x2 Grid on the right)
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, this.inputEnd + 1, 107, 26)); //TOP LEFT
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, this.inputEnd + 2, 125, 26)); //TOP RIGHT
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, this.outputEnd - 1, 107, 44)); //BOTTOM LEFT
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, this.outputEnd, 125, 44)); //BOTTOM RIGHT
    }

    /**
     * Checks whether an ItemStack is accepted as fuel by this furnace workbench.
     *
     * @param stack the ItemStack to test
     * @return {@code true} if the stack is a stick, string, or matches the `ItemTags.LOGS_THAT_BURN` tag; {@code false} otherwise
     */
    public boolean isFuel(ItemStack stack) {
        return stack.is(Items.STICK) || stack.is(Items.STRING) || stack.is(ItemTags.LOGS_THAT_BURN);
    }

    /**
     * Checks whether the given player may continue interacting with this menu.
     *
     * @param player the player to check
     * @return `true` if the player may continue interacting with the menu, `false` otherwise
     */
    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    /**
     * Adds the player's main 3x9 inventory grid to this menu.
     *
     * The grid is positioned starting at (8, 84) with 18-pixel spacing between slots.
     *
     * @param playerInventory the player's Inventory to populate the menu slots from
     */
    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    /**
     * Adds the player's 9-slot hotbar to this menu at the standard hotbar position.
     *
     * @param playerInventory the player's inventory to populate hotbar slots from
     */
    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    /**
     * Provides the current cook progress for rendering the cook progress bar.
     *
     * @return the cook progress scaled to a 24-pixel width (`0` if there is no progress or total cook time is zero)
     */
    public int getCookProgress() {
        // Guard against null data or missing indices (needs at least index 2 and 3)
        if (this.data == null || this.data.getCount() < 4) {
            return 0;
        }
        
        int i = this.data.get(2); // cookTime
        int j = this.data.get(3); // cookTimeTotal
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    /**
     * Calculates the fuel burn progress for the GUI fuel indicator.
     *
     * Uses container data index 0 as the current fuel time and index 1 as the total fuel time;
     * if the total is zero, a fallback of 200 ticks is used. The result is scaled to a 13-pixel width.
     *
     * @return an integer between 0 and 13 representing the current burn progress in pixels
     */
    public int getBurnProgress() {
        // Guard against null data or missing indices (needs at least index 0 and 1)
        if (this.data == null || this.data.getCount() < 2) {
            return 0;
        }

        int i = this.data.get(1); // fuelTimeTotal
        if (i == 0) i = 200;
        return this.data.get(0) * 13 / i; // fuelTime
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (this.outputEnd <= 0) return ItemStack.EMPTY;

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();

            int workbenchSlotsEnd = this.outputEnd + 1; 

            // CASE 1: Moving from Workbench to Player Inventory
            if (index < workbenchSlotsEnd) {
                // Try to move to player inventory (indices 7 to 43)
                // Use reverse = true to fill the hotbar last (standard vanilla behavior)
                if (!this.moveItemStackTo(itemStack2, workbenchSlotsEnd, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } 
            // CASE 2: Moving from Player Inventory to Workbench
            else {
                if (this.isFuel(itemStack2)) {
                    // 1. Try the Fuel Slot (Index 0)
                    if (!this.moveItemStackTo(itemStack2, Constants.FUEL_SLOT, Constants.FUEL_SLOT + 1, false)) {
                        // 2. If fuel is full, try the Input slots (Indices 1 to 3) as backup
                        if (!this.moveItemStackTo(itemStack2, Constants.INPUT_START, this.inputEnd + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else {
                    // 3. Not fuel? Go straight to Input slots (Indices 1 to 3)
                    // This ensures Slot 1 is checked BEFORE Slot 2
                    if (!this.moveItemStackTo(itemStack2, Constants.INPUT_START, this.inputEnd + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    public abstract @Nullable AbstractWorkbenchEntity getBlockEntity();

    @Override
    public RecipeBookMenu.PostPlaceAction handlePlacement(boolean placeAll, boolean isSpecial, RecipeHolder<?> recipe, ServerLevel serverLevel, Inventory inventory) {
        if (!(recipe.value() instanceof WorkbenchRecipe workbenchRecipe)) {
            return PostPlaceAction.NOTHING;
        }

        AbstractWorkbenchEntity be = this.getBlockEntity();
        // A workbench is slotless if outputEnd was passed as -1 or 0 in the constructor
        boolean isSlotless = this.outputEnd <= 0;

        // --- 1. SLOTTED LOGIC (Furnace-style) ---
        if (!isSlotless && be != null && be.isCanPullFromNearby()) {
            StackedItemContents contents = new StackedItemContents();
            inventory.fillStackedContents(contents);
            
            // Account for items already in the workbench slots
            for (int i = 0; i < this.container.getContainerSize(); i++) {
                contents.accountStack(this.container.getItem(i));
            }

            // Pull missing items from nearby chests into the BE slots first
            be.fillMissingIngredientsFromNearby(workbenchRecipe, contents, Constants.INPUT_START, this.inputEnd);
        }

        // --- 2. SLOTLESS LOGIC (Direct Crafting) ---
        if (isSlotless && be != null) {
            // Since there are no slots, we check if Player + Nearby has enough
            if (this.canCraftSlotless(workbenchRecipe, inventory, be)) {
                // Physically remove items from world/player and give result
                this.consumeAndGiveToPlayer(workbenchRecipe, inventory, be);
                
                // We return NOTHING because the standard Ghost Recipe logic 
                // shouldn't try to "place" items into non-existent slots.
                return PostPlaceAction.NOTHING; 
            }
        } 
        
        // --- 3. STANDARD SLOTTED PLACEMENT ---
        // If it's not slotless, perform the vanilla-style placement into slots
        if (!isSlotless) {
            return this.performStandardPlacement(placeAll, recipe, serverLevel, inventory);
        }

        return PostPlaceAction.NOTHING;
    }

    private RecipeBookMenu.PostPlaceAction performStandardPlacement(boolean placeAll, RecipeHolder<?> recipe, ServerLevel level, Inventory inventory) {
        RecipeHolder<WorkbenchRecipe> castRecipe = (RecipeHolder<WorkbenchRecipe>) recipe;
        
        return ServerPlaceRecipe.placeRecipe(
            new ServerPlaceRecipe.CraftingMenuAccess<WorkbenchRecipe>() {
                @Override public void fillCraftSlotsStackedContents(StackedItemContents contents) { 
                    AbstractWorkbenchContainerMenu.this.fillCraftSlotsStackedContents(contents); 
                }
                @Override public void clearCraftingContent() {
                    // Clear input slots and return to player
                    int[] slots = {Constants.INPUT_START, AbstractWorkbenchContainerMenu.this.inputEnd};
                    for (int i : slots) {
                        if (i < 0) continue;
                        ItemStack stack = AbstractWorkbenchContainerMenu.this.container.getItem(i);
                        if (!stack.isEmpty()) {
                            AbstractWorkbenchContainerMenu.this.playerInventory.placeItemBackInInventory(stack);
                            AbstractWorkbenchContainerMenu.this.container.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
                @Override public boolean recipeMatches(RecipeHolder<WorkbenchRecipe> holder) {
                    return holder.value().matches(AbstractWorkbenchContainerMenu.this.createRecipeInput(), level);
                }
            },
            1, 1, 
            List.of(this.getSlot(Constants.INPUT_START), this.getSlot(this.inputEnd)), 
            List.of(this.getSlot(this.inputEnd + 1), this.getSlot(this.inputEnd + 2), 
                    this.getSlot(this.outputEnd - 1), this.getSlot(this.outputEnd)),
            inventory, castRecipe, placeAll, false
        );
    }

    private boolean canCraftSlotless(WorkbenchRecipe recipe, Inventory playerInv, AbstractWorkbenchEntity be) {
        StackedItemContents totalContents = new StackedItemContents();
        
        // 1. Add Player items
        playerInv.fillStackedContents(totalContents);
        
        // 2. Add Nearby items (This works perfectly on Server!)
        if (be != null && be.isCanPullFromNearby()) {
            for (Container nearby : be.getNearbyInventories()) {
                for (int i = 0; i < nearby.getContainerSize(); i++) {
                    totalContents.accountStack(nearby.getItem(i));
                }
            }
        }
        
        // Use the Recipe's ingredients to verify
        return totalContents.canCraft(recipe, null);
    }

    private void consumeAndGiveToPlayer(WorkbenchRecipe recipe, Inventory playerInv, AbstractWorkbenchEntity be) {
        for (Ingredient ingredient : recipe.ingredients()) {
            // Priority 1: Take from Player
            if (removeItemFromInventory(playerInv, ingredient)) continue;
            
            // Priority 2: Take from Nearby
            if (be.isCanPullFromNearby()) {
                for (Container nearby : be.getNearbyInventories()) {
                    if (removeItemFromContainer(nearby, ingredient)) break;
                }
            }
        }
        
        // Give results to player
        for (ItemStack result : recipe.results()) {
            playerInv.placeItemBackInInventory(result.copy());
        }
    }

    private boolean removeItemFromInventory(Inventory inv, Ingredient ing) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && ing.test(stack)) {
                inv.removeItem(i, 1);
                return true;
            }
        }
        return false;
    }

    private boolean removeItemFromContainer(Container inv, Ingredient ing) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && ing.test(stack)) {
                inv.removeItem(i, 1);
                inv.setChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public void fillStackedContents(StackedItemContents contents) {
        // You MUST manually add the items from your SimpleContainer 
        // to the contents for the recipe book to "simulate" correctly.
        for (int i = Constants.INPUT_START; i <= this.inputEnd; i++) {
            contents.accountStack(this.container.getItem(i));
        }
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents contents) {
        // 1. Account for items in the player's actual inventory
        this.playerInventory.fillStackedContents(contents);
        
        // 2. Account for items sitting in the Workbench slots (if any)
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            contents.accountStack(this.container.getItem(i));
        }

        // 3. USE THE PACKET DATA: This is the list sent from the server
        // This bypasses the client-side world-scan and uses the "trusted" list
        for (ItemStack stack : this.getNetworkedNearbyItems()) {
            if (!stack.isEmpty()) {
                contents.accountStack(stack);
            }
        }
    }
    
    public abstract WorkbenchRecipeInput createRecipeInput();
}