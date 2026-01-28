package com.tcm.MineTale.block.workbenches.menu;

import com.tcm.MineTale.registry.ModMenuTypes;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FurnaceWorkbenchMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    private final int containerSize = 7;

    // Slot Constants
    private static final int FUEL_SLOT = 0;
    private static final int INPUT_1 = 1;
    private static final int INPUT_2 = 2;
    private static final int OUTPUT_START = 3;
    private static final int OUTPUT_END = 6;

    /**
     * Client-side constructor that creates a FurnaceWorkbenchMenu backed by a new internal container and container data.
     *
     * @param syncId         the window sync id assigned by the client
     * @param playerInventory the player's inventory to attach to this menu
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(4));
    }

    /**
     * Initializes a server-side furnace-workbench menu for a player, setting up the container slots
     * and attaching progress synchronization data.
     *
     * @param syncId         the window synchronization id assigned by the server
     * @param playerInventory the player's Inventory used to populate player inventory and hotbar slots
     * @param container      the backing container (expected size 7) that provides the workbench slots
     * @param data           the ContainerData (expected count 4) used to sync cook and burn progress
     */
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.FURNACE_WORKBENCH_MENU, syncId);
        checkContainerSize(container, containerSize);
        checkContainerDataCount(data, 4);
        this.container = container;
        this.data = data;

        container.startOpen(playerInventory.player);

        // --- FURNACE SLOTS ---
        
        // 1. Fuel Slot (Center-ish bottom)
        this.addSlot(new Slot(container, FUEL_SLOT, 80, 53) {
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
        this.addSlot(new Slot(container, INPUT_1, 44, 17));
        this.addSlot(new Slot(container, INPUT_2, 44, 35));

        // 3. Four Output Slots (2x2 Grid on the right)
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, OUTPUT_START, 116, 21));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, OUTPUT_START + 1, 134, 21));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, OUTPUT_END - 1, 116, 39));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, OUTPUT_END, 134, 39));

        // --- PLAYER INVENTORY ---
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // Sync the cooking/fuel progress bars
        this.addDataSlots(data);
    }

    /**
     * Handles a quick (shift-click) transfer of an item stack between the furnace-workbench container and the player's inventory.
     *
     * Attempts to move the stack from the container area (slots belonging to this menu) to the player's inventory, or from the
     * player inventory into the appropriate container slots. When moving into the container, fuel items are sent to the fuel
     * slot and other items are sent to the input slots. If the transfer cannot be completed, no change is applied.
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
            if (index < 7) {
                if (!this.moveItemStackTo(itemStack2, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }
            } 
            // From Player to Furnace
            else {
                // If it's fuel, try fuel slot
                if (isFuel(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) return ItemStack.EMPTY;
                } 
                // Otherwise, try inputs
                else if (!this.moveItemStackTo(itemStack2, 1, 3, false)) {
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

    /**
     * Checks whether an ItemStack is accepted as fuel by this furnace workbench.
     *
     * @param stack the ItemStack to test
     * @return {@code true} if the stack is a stick, string, or matches the `ItemTags.LOGS_THAT_BURN` tag; {@code false} otherwise
     */
    private boolean isFuel(ItemStack stack) {
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
}