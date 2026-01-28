package com.tcm.MineTale.block.workbenches.entity;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;
import com.tcm.MineTale.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class FurnaceWorkbenchEntity extends AbstractWorkbenchEntity {
    // Inventory Mapping: 
    // 0     -> Fuel Slots
    // 1, 2  -> Input Slot
    // 3-6   -> Output Slots
    private final SimpleContainer inventory = new SimpleContainer(7);

    private int cookTime;
    private int cookTimeTotal = 200; 
    private int fuelTime;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> fuelTime;
                case 1 -> 100; // Fuel total
                case 2 -> cookTime;
                case 3 -> cookTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> fuelTime = value;
                case 2 -> cookTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public FurnaceWorkbenchEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_WORKBENCH_BE, pos, state);
        this.scanRadius = 8.0;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        boolean changed = false;
        ItemStack input = inventory.getItem(0);
        ItemStack fuel = inventory.getItem(1);

        // TRAIT: Streamline crafting by pulling from nearby chests if input is empty
        if (input.isEmpty() && level.getGameTime() % 20 == 0) {
            pullFromNearbyChests();
        }

        if (canSmelt(input)) {
            // TRAIT: Upgrade System - Higher tier = faster smelting
            // Tier 1: 200 ticks, Tier 2: 150 ticks, Tier 3: 100 ticks...
            int speedBoost = (this.tier - 1) * 50;
            int currentTotal = Math.max(20, this.cookTimeTotal - speedBoost);

            if (fuelTime > 0 || !fuel.isEmpty()) {
                if (fuelTime <= 0 && consumeFuel(fuel)) {
                    changed = true;
                }

                if (fuelTime > 0) {
                    fuelTime--;
                    cookTime++;
                    if (cookTime >= currentTotal) {
                        smeltItem(input);
                        cookTime = 0;
                        changed = true;
                    }
                }
            }
        } else {
            cookTime = 0;
        }

        if (changed) setChanged();
    }

    private boolean canSmelt(ItemStack input) {
        if (input.isEmpty()) return false;
        // Logic: Check if it's an ore (Copper to Adamantite) or Logs for Charcoal
        return isOre(input) || isWood(input);
    }

    private boolean consumeFuel(ItemStack fuel) {
        // TRAIT: Use fibres (string), sticks, or logs
        if (fuel.is(Items.STICK) || fuel.is(Items.STRING) || isWood(fuel)) {
            this.fuelTime = 100; // Assign burn time
            fuel.shrink(1);
            return true;
        }
        return false;
    }

    private void smeltItem(ItemStack input) {
        ItemStack result;
        // TRAIT: Logs yield Charcoal
        if (isWood(input)) {
            result = new ItemStack(Items.CHARCOAL);
        } else {
            // Placeholder: Replace with your actual Ore-to-Ingot logic
            result = new ItemStack(Items.COPPER_INGOT); 
        }

        ItemStack output = inventory.getItem(2);
        if (output.isEmpty()) {
            inventory.setItem(2, result.copy());
        } else if (ItemStack.isSameItem(output, result)) {
            output.grow(result.getCount());
        }
        input.shrink(1);
    }

    private void pullFromNearbyChests() {
        List<Container> nearby = this.getNearbyInventories();
        for (Container chest : nearby) {
            for (int i = 0; i < chest.getContainerSize(); i++) {
                ItemStack stack = chest.getItem(i);
                if (isOre(stack) || isWood(stack)) {
                    inventory.setItem(0, stack.split(1));
                    chest.setChanged();
                    return;
                }
            }
        }
    }

    // --- Helpers ---
    private boolean isOre(ItemStack stack) { return stack.is(Items.RAW_COPPER); /* Add more ores */ }
    private boolean isWood(ItemStack stack) { return stack.getItem().toString().contains("log"); }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        // store() uses Codecs for type safety
        valueOutput.store("WorkbenchTier", Codec.INT, this.tier);
        valueOutput.store("ScanRadius", Codec.DOUBLE, this.scanRadius);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        // read() returns an Optional
        this.tier = valueInput.read("WorkbenchTier", Codec.INT).orElse(1);
        this.scanRadius = valueInput.read("ScanRadius", Codec.DOUBLE).orElse(5.0);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FurnaceWorkbenchMenu(syncId, playerInventory, this.inventory, this.data);
    }
}
