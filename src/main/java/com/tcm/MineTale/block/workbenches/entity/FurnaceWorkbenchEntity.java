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
    private static final int FUEL_SLOT = 0;
    private static final int INPUT_1 = 1;
    private static final int INPUT_2 = 2;
    private static final int OUTPUT_START = 3;
    private static final int OUTPUT_END = 6;
    private final SimpleContainer inventory = new SimpleContainer(7);

    private int cookTime;
    private int cookTimeTotal = 200; 
    private int fuelTime;

    protected final ContainerData data = new ContainerData() {
        /**
         * Retrieves an internal data value by index for UI synchronization.
         *
         * @param index the data index: 0 = remaining fuel time, 1 = fuel total (constant 100),
         *              2 = current cook progress, 3 = total cook time
         * @return the value associated with {@code index}, or 0 for any other index
         */
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

        /**
         * Sets an internal workbench data field identified by index.
         *
         * Supported indices:
         * <ul>
         *   <li>0 — sets {@code fuelTime}</li>
         *   <li>2 — sets {@code cookTime}</li>
         * </ul>
         * Other indices are ignored.
         *
         * @param index the data index to set
         * @param value the value to assign to the indexed field
         */
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> fuelTime = value;
                case 2 -> cookTime = value;
            }
        }

        /**
         * The number of data values exposed by this ContainerData.
         *
         * @return the number of data entries (4)
         */
        @Override
        public int getCount() {
            return 4;
        }
    };

    /**
     * Creates a new FurnaceWorkbenchEntity at the specified position and block state.
     *
     * Initializes the entity and sets the default nearby-inventory scan radius to 8.0.
     *
     * @param pos   the block position of the entity
     * @param state the block state at that position
     */
    public FurnaceWorkbenchEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_WORKBENCH_BE, pos, state);
        this.scanRadius = 8.0;
    }

    /**
     * Performs server-side per-tick processing for the furnace workbench: attempts to pull input items from nearby
     * containers, manages fuel consumption, advances smelting progress according to workbench tier, and produces output
     * when a smelt cycle completes.
     *
     * <p>Behavioral notes:
     * - Runs only on the server side.
     * - When input is empty, attempts to pull a single eligible input item from nearby inventories once every 20 game ticks.
     * - Workbench tier reduces the required cook duration; cooking advances while fuel is available and is reset when smelting is not possible.
     * - Consumes fuel items to refill internal fuel time, decrements fuel time each tick, increments cook progress, and invokes smeltItem(...) when a cycle finishes.
     * - Marks the block entity changed if any inventory or internal state is modified.</p>
     *
     * @param level the world in which the workbench exists
     * @param pos   the block position of the workbench
     * @param state the current block state of the workbench
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        boolean changed = false;
        ItemStack fuel = inventory.getItem(FUEL_SLOT);
        ItemStack input = !inventory.getItem(INPUT_1).isEmpty()
            ? inventory.getItem(INPUT_1)
            : inventory.getItem(INPUT_2);

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

    /**
     * Determines whether the provided stack is a valid smelting input (ore or log).
     *
     * @param input the item stack to test
     * @return `true` if the stack represents an ore or a log, `false` otherwise
     */
    private boolean canSmelt(ItemStack input) {
        if (input.isEmpty()) return false;
        // Logic: Check if it's an ore (Copper to Adamantite) or Logs for Charcoal
        return isOre(input) || isWood(input);
    }

    /**
     * Consume one unit of the provided fuel item and set the internal fuel timer when the item is an accepted fuel.
     *
     * Accepted fuels: sticks, string (fibres), or any item recognized as wood.
     *
     * @param fuel the ItemStack to attempt to consume; one item will be removed if accepted
     * @return `true` if a fuel unit was consumed and the internal fuel time was set to 100, `false` otherwise
     */
    private boolean consumeFuel(ItemStack fuel) {
        // TRAIT: Use fibres (string), sticks, or logs
        if (fuel.is(Items.STICK) || fuel.is(Items.STRING) || isWood(fuel)) {
            this.fuelTime = 100; // Assign burn time
            fuel.shrink(1);
            return true;
        }
        return false;
    }

    /**
     * Smelts a single input item into its output and deposits the result into an available output slot.
     *
     * <p>If the input is wood, produces charcoal; otherwise produces a copper ingot (placeholder for ore-to-ingot mapping).
     * The method finds a suitable output slot and either places the result there or increases the existing stack; if no
     * output slot is available the method does nothing. The input stack is reduced by one on successful smelting.
     *
     * @param input the ItemStack to smelt; one item will be consumed from this stack when smelting occurs
     */
    private void smeltItem(ItemStack input) {
        ItemStack result;
        // TRAIT: Logs yield Charcoal
        if (isWood(input)) {
            result = new ItemStack(Items.CHARCOAL);
        } else {
            // Placeholder: Replace with your actual Ore-to-Ingot logic
            result = new ItemStack(Items.COPPER_INGOT); 
        }

        int outputSlot = findOutputSlot(result);
        if (outputSlot == -1) return;
        ItemStack output = inventory.getItem(outputSlot);

        if (output.isEmpty()) {
            inventory.setItem(2, result.copy());
        } else if (ItemStack.isSameItem(output, result)) {
            output.grow(result.getCount());
        }
        input.shrink(1);
    }

    /**
     * Attempts to move a single ore or wood item from nearby inventories into this entity's input slots.
     *
     * If INPUT_1 is empty that slot is filled first; otherwise INPUT_2 is used. If neither input slot is
     * available or no matching item is found, the method does nothing. When an item is moved, the source
     * container is marked changed.
     */
    private void pullFromNearbyChests() {
        List<Container> nearby = this.getNearbyInventories();
        for (Container chest : nearby) {
            for (int i = 0; i < chest.getContainerSize(); i++) {
                ItemStack stack = chest.getItem(i);
                if (isOre(stack) || isWood(stack)) {
                    int inputSlot = inventory.getItem(INPUT_1).isEmpty() ? INPUT_1 : (inventory.getItem(INPUT_2).isEmpty() ? INPUT_2 : -1);
                    if (inputSlot == -1) return;
                    inventory.setItem(inputSlot, stack.split(1));
                    chest.setChanged();
                    return;
                }
            }
        }
    }

    /**
     * Finds the first output slot that can accept the given result.
     *
     * @param result the item stack to place into an output slot
     * @return the index of the first suitable output slot between OUTPUT_START and OUTPUT_END, or -1 if none is available
     */
    private int findOutputSlot(ItemStack result) {
        for (int i = OUTPUT_START; i <= OUTPUT_END; i++) {
            ItemStack out = inventory.getItem(i);
            if (out.isEmpty() || (ItemStack.isSameItem(out, result)
                && out.getCount() + result.getCount() <= out.getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

    /**
 * Determines whether the provided item stack is a supported ore.
 *
 * @param stack the item stack to test
 * @return `true` if the stack is a supported ore (currently `Items.RAW_COPPER`), `false` otherwise
 */
    private boolean isOre(ItemStack stack) { return stack.is(Items.RAW_COPPER); /* Add more ores */ }
    /**
 * Determines whether the given item stack represents a wood log item.
 *
 * @param stack the item stack to inspect
 * @return `true` if the stack's item is a wood log, `false` otherwise
 */
private boolean isWood(ItemStack stack) { return stack.getItem().toString().contains("log"); }

    /**
     * Persist entity-specific state into the provided ValueOutput.
     *
     * Stores the workbench's tier as "WorkbenchTier" and its scan radius as "ScanRadius"
     * using type-safe Codecs.
     *
     * @param valueOutput the output writer used to serialize this entity's fields
     */
    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        // store() uses Codecs for type safety
        valueOutput.store("WorkbenchTier", Codec.INT, this.tier);
        valueOutput.store("ScanRadius", Codec.DOUBLE, this.scanRadius);
    }

    /**
     * Restores workbench-specific state from persistent storage and applies defaults when keys are absent.
     *
     * Delegates to the superclass load logic, then reads:
     * - "WorkbenchTier" (int) into {@code tier}, defaulting to {@code 1} if missing.
     * - "ScanRadius" (double) into {@code scanRadius}, defaulting to {@code 5.0} if missing.
     */
    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        // read() returns an Optional
        this.tier = valueInput.read("WorkbenchTier", Codec.INT).orElse(1);
        this.scanRadius = valueInput.read("ScanRadius", Codec.DOUBLE).orElse(5.0);
    }

    /**
     * Create the player-facing menu for this Furnace Workbench block entity.
     *
     * @param syncId         the window id used to synchronize client and server for this menu
     * @param playerInventory the player's inventory view passed to the menu
     * @param player         the player opening the menu
     * @return               the FurnaceWorkbenchMenu instance for this block entity, or `null` if a menu cannot be created
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FurnaceWorkbenchMenu(syncId, playerInventory, this.inventory, this.data);
    }
}