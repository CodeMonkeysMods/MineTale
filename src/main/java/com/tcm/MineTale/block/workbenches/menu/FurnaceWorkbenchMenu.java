package com.tcm.MineTale.block.workbenches.menu;

import com.tcm.MineTale.registry.ModMenuTypes;

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

    // This constructor is used by the Client
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(7), new SimpleContainerData(4));
    }

    // This constructor is used by the Server (via the BlockEntity)
    public FurnaceWorkbenchMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.FURNACE_WORKBENCH_MENU, syncId);
        checkContainerSize(container, containerSize);
        this.container = container;
        this.data = data;

        container.startOpen(playerInventory.player);

        // --- FURNACE SLOTS ---
        
        // 1. Fuel Slot (Center-ish bottom)
        this.addSlot(new Slot(container, FUEL_SLOT, 80, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // Trait: Sticks, Fibres (String), and Logs
                return stack.is(Items.STICK) || stack.is(Items.STRING) || stack.getItem().toString().contains("log");
            }
        });

        // 2. Two Input Slots (Stacked on the left)
        this.addSlot(new Slot(container, INPUT_1, 44, 17));
        this.addSlot(new Slot(container, INPUT_2, 44, 35));

        // 3. Four Output Slots (2x2 Grid on the right)
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, 3, 116, 21));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, 4, 134, 21));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, 5, 116, 39));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, 6, 134, 39));

        // --- PLAYER INVENTORY ---
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // Sync the cooking/fuel progress bars
        this.addDataSlots(data);
    }

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

    private boolean isFuel(ItemStack stack) {
        return stack.is(Items.STICK) || stack.is(Items.STRING) || stack.getItem().toString().contains("log");
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    // Methods for the Screen to use for rendering progress bars
    public int getCookProgress() {
        int i = this.data.get(2); // cookTime
        int j = this.data.get(3); // cookTimeTotal
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(1); // fuelTimeTotal
        if (i == 0) i = 200;
        return this.data.get(0) * 13 / i; // fuelTime
    }
}
