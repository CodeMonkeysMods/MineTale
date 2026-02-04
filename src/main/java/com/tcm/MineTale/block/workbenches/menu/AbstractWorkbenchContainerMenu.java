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
     * Creates a workbench container menu backed by the given inventory and sync data, sets up slots
     * (fuel slot, two inputs, four result slots) and binds player inventory/hotbar and data for progress syncing.
     *
     * @param menuType the menu type (may be null for dynamic registration)
     * @param syncId the window synchronization id
     * @param container the underlying container inventory for the workbench
     * @param data the container data used to synchronize cook and burn progress
     * @param containerSize expected size of {@code container}; validated by this constructor
     * @param containerDataSize expected size of {@code data}; validated by this constructor
     * @param playerInventory the player's inventory used to add player slots and to identify the player for result slots
     */
    public AbstractWorkbenchContainerMenu(@Nullable MenuType<?> menuType, int syncId, Container container, ContainerData data, int containerDataSize, Inventory playerInventory, int inputEnd, int outputEnd) {
        super(menuType, syncId);

        this.outputEnd = outputEnd;
        this.inputEnd = Constants.INPUT_START + 1;

        checkContainerSize(container, outputEnd + 1);
        checkContainerDataCount(data, containerDataSize);
        this.container = container;
        this.data = data;
        this.playerInventory = playerInventory;

        container.startOpen(playerInventory.player);

        // 1. Fuel Slot (Center-ish bottom)
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

        // --- PLAYER INVENTORY ---
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // Sync the cooking/fuel progress bars
        this.addDataSlots(data);
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
    private void addPlayerInventory(Inventory playerInventory) {
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
    private void addPlayerHotbar(Inventory playerInventory) {
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
        int i = this.data.get(1); // fuelTimeTotal
        if (i == 0) i = 200;
        return this.data.get(0) * 13 / i; // fuelTime
    }

    /**
         * Performs a shift-click transfer between this container and the player's inventory.
         *
         * Moves the clicked stack into the player's inventory if it came from the container, or into the appropriate container
         * slots if it came from the player's inventory. Fuel items are moved to the fuel slot; all other items are moved to the
         * input slots. If the transfer cannot be completed, no changes are applied to the source slot.
         *
         * @param player the player performing the transfer
         * @param index  the index of the slot that was shift-clicked
         * @return the original ItemStack from the clicked slot, or ItemStack.EMPTY if the transfer failed
         */
    // @Override
    // public ItemStack quickMoveStack(Player player, int index) {
    //     ItemStack itemStack = ItemStack.EMPTY;
    //     Slot slot = this.slots.get(index);
    //     if (slot != null && slot.hasItem()) {
    //         ItemStack itemStack2 = slot.getItem();
    //         itemStack = itemStack2.copy();

    //         // From Furnace to Player
    //         int containerSlots = this.outputEnd + 1;
    //         int playerStart = containerSlots;
    //         int playerEnd = playerStart + 36;

    //         // From Furnace to Player
    //         if (index < containerSlots) {
    //             if (!this.moveItemStackTo(itemStack2, playerStart, playerEnd, true)) {
    //                 return ItemStack.EMPTY;
    //             }
    //         } 
    //         // From Player to Furnace
    //         else {
    //             // If it's fuel, try fuel slot
    //             if (isFuel(itemStack2)) {
    //                 if (!this.moveItemStackTo(itemStack2, Constants.FUEL_SLOT, Constants.FUEL_SLOT + 1, false)) return ItemStack.EMPTY;
    //             } 
    //             // Otherwise, try inputs
    //             else if (!this.moveItemStackTo(itemStack2, Constants.INPUT_START, this.inputEnd, false)) {
    //                 return ItemStack.EMPTY;
    //             }
    //         }

    //         if (itemStack2.isEmpty()) {
    //             slot.setByPlayer(ItemStack.EMPTY);
    //         } else {
    //             slot.setChanged();
    //         }
    //     }
    //     return itemStack;
    // }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();

            // Indices: 
            // 0-6: Workbench (0: Fuel, 1-2: Input, 3-6: Output)
            // 7-33: Player Inventory
            // 34-42: Player Hotbar
            int workbenchSlotsEnd = 7; 

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
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        // 2. If fuel is full, try the Input slots (Indices 1 to 3) as backup
                        if (!this.moveItemStackTo(itemStack2, 1, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else {
                    // 3. Not fuel? Go straight to Input slots (Indices 1 to 3)
                    // This ensures Slot 1 is checked BEFORE Slot 2
                    if (!this.moveItemStackTo(itemStack2, 1, 3, false)) {
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
        if (recipe.value() instanceof WorkbenchRecipe workbenchRecipe) {
            
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
                        // Use the block entity's logic to see if current inputs match
                        AbstractWorkbenchContainerMenu.this.getBlockEntity(); // Just to ensure it exists
                        // Note: Ensure your sub-classes provide createRecipeInput() or similar logic
                        holder.value().matches(null, serverLevel);
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
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            contents.accountStack(this.container.getItem(i));
        }
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents contents) {
        // 1. Tell the server what is in the player's pockets
        this.playerInventory.fillStackedContents(contents);
        
        // 2. Tell the server what is already in the workbench slots
        // This allows the server to 'add' to the existing count
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            contents.accountStack(this.container.getItem(i));
        }
    }

    public abstract WorkbenchRecipeInput createRecipeInput();
}