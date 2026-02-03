package com.tcm.MineTale.block.workbenches.menu;

import org.jspecify.annotations.Nullable;

import com.tcm.MineTale.util.Constants;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public abstract class AbstractWorkbenchContainerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    /**
     * Initializes a workbench container menu backed by the given inventory and sync data, validates sizes, opens the container, sets up the fuel slot, two input slots and four result slots, binds the player's inventory and hotbar, and registers data for cook/burn progress synchronization.
     *
     * @param menuType the menu type; may be null for dynamic registration
     * @param syncId the window synchronization id
     * @param container the underlying workbench inventory
     * @param data container data used to synchronize cook and burn progress
     * @param containerSize expected size of {@code container}; validated by this constructor
     * @param containerDataSize expected size of {@code data}; validated by this constructor
     * @param playerInventory the player's inventory used to add player slots and to identify the player for result slots
     */
    public AbstractWorkbenchContainerMenu(@Nullable MenuType<?> menuType, int syncId, Container container, ContainerData data, int containerSize, int containerDataSize, Inventory playerInventory) {
        super(menuType, syncId);

        checkContainerSize(container, containerSize);
        checkContainerDataCount(data, containerDataSize);
        this.container = container;
        this.data = data;

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
        this.addSlot(new Slot(container, Constants.INPUT_1, 35, 17)); //LEFT
        this.addSlot(new Slot(container, Constants.INPUT_2, 53, 17)); //RIGHT

        // 3. Four Output Slots (2x2 Grid on the right)
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, Constants.OUTPUT_START, 107, 26)); //TOP LEFT
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, Constants.OUTPUT_START + 1, 125, 26)); //TOP RIGHT
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, Constants.OUTPUT_END - 1, 107, 44)); //BOTTOM LEFT
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, Constants.OUTPUT_END, 125, 44)); //BOTTOM RIGHT

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
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();

            // From Furnace to Player
            int containerSlots = Constants.TOTAL_SLOTS;
            int playerStart = containerSlots;
            int playerEnd = playerStart + 36;

            // From Furnace to Player
            if (index < containerSlots) {
                if (!this.moveItemStackTo(itemStack2, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } 
            // From Player to Furnace
            else {
                // If it's fuel, try fuel slot
                if (isFuel(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, Constants.FUEL_SLOT, Constants.FUEL_SLOT + 1, false)) return ItemStack.EMPTY;
                } 
                // Otherwise, try inputs
                else if (!this.moveItemStackTo(itemStack2, Constants.INPUT_1, Constants.INPUT_2 + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }
}