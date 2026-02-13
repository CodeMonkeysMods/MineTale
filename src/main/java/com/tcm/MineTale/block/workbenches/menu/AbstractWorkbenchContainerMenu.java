package com.tcm.MineTale.block.workbenches.menu;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.recipe.WorkbenchRecipeInput;
import com.tcm.MineTale.util.Constants;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class AbstractWorkbenchContainerMenu extends RecipeBookMenu implements StackedContentsCompatible {
    protected final Container container;
    private final ContainerData data;
    
    protected final int inputEnd;
    protected final int outputEnd;

    protected final Inventory playerInventory;

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
    public RecipeBookMenu.PostPlaceAction handlePlacement(boolean placeAll, boolean isSpecial, RecipeHolder<?> recipe, ServerLevel serverLevel, Inventory inventory
	) {
        if (recipe.value() instanceof WorkbenchRecipe) {
            @SuppressWarnings("unchecked")
            RecipeHolder<WorkbenchRecipe> castRecipe = (RecipeHolder<WorkbenchRecipe>) recipe;
            // 2. Call the static placeRecipe method
            return ServerPlaceRecipe.placeRecipe(
                new ServerPlaceRecipe.CraftingMenuAccess<WorkbenchRecipe>() {
                    @Override
                    public void fillCraftSlotsStackedContents(StackedItemContents contents) {
                        AbstractWorkbenchContainerMenu.this.fillCraftSlotsStackedContents(contents);
                    }

                    @Override
                    public void clearCraftingContent() {
                        // Instead of setting to EMPTY, return items to player inventory
                        // This allows the Recipe Book to 'refill' or 'stack' properly
                        for (int i : new int[]{Constants.INPUT_START, AbstractWorkbenchContainerMenu.this.inputEnd}) {
                            ItemStack stack = AbstractWorkbenchContainerMenu.this.getSlot(i).getItem();
                            if (!stack.isEmpty()) {
                                AbstractWorkbenchContainerMenu.this.playerInventory.placeItemBackInInventory(stack);
                                AbstractWorkbenchContainerMenu.this.getSlot(i).set(ItemStack.EMPTY);
                            }
                        }
                    }

                    @Override
                    public boolean recipeMatches(RecipeHolder<WorkbenchRecipe> holder) {
                        return holder.value().matches(
                            AbstractWorkbenchContainerMenu.this.createRecipeInput(), 
                            serverLevel
                        );
                    }
                },
                1, // Grid Width
                1, // Grid Height
                // FIX: Pass Slots 1 and 2 here. 
                // If Constants.INPUT_START is 1 and inputEnd is 2, this is correct:
                List.of(this.getSlot(Constants.INPUT_START), this.getSlot(this.inputEnd)), 
                // Result Slots (3, 4, 5, 6)
                List.of(this.getSlot(this.inputEnd + 1), this.getSlot(this.inputEnd + 2), 
                        this.getSlot(this.outputEnd - 1), this.getSlot(this.outputEnd)),
                inventory,
                castRecipe,
                placeAll,
                false
            );
        }

        return PostPlaceAction.NOTHING;
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
        // 1. Tell the server what is in the player's pockets
        this.playerInventory.fillStackedContents(contents);
        
        // 2. Tell the server what is already in the workbench slots
        // This allows the server to 'add' to the existing count
        for (int i = Constants.INPUT_START; i <= this.inputEnd; i++) {
            contents.accountStack(this.container.getItem(i));
        }
    }

    public abstract WorkbenchRecipeInput createRecipeInput();
}