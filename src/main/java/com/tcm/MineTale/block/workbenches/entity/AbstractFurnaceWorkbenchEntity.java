package com.tcm.MineTale.block.workbenches.entity;

import java.util.List;

import com.mojang.serialization.Codec;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.registry.ModRecipes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class AbstractFurnaceWorkbenchEntity extends AbstractWorkbenchEntity {
    private int cookTime;
    private int cookTimeTotal = 200; 
    private int fuelTime;

    public AbstractFurnaceWorkbenchEntity(BlockEntityType<FurnaceWorkbenchEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getFuelTime() {
        return this.fuelTime;
    }

    public void setFuelTime(int t) {
        this.fuelTime = t;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public void setCookTime(int t) {
        this.cookTime = t;
    }

    public int getCookTimeTotal() {
        return this.cookTimeTotal;
    }

    public void setCookTimeTotal(int t) {
        this.cookTimeTotal = t;
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
        ItemStack fuel = inventory.getItem(Constants.FUEL_SLOT);
        ItemStack input = !inventory.getItem(Constants.INPUT_1).isEmpty()
            ? inventory.getItem(Constants.INPUT_1)
            : inventory.getItem(Constants.INPUT_2);

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

        int outputSlot = this.findOutputSlot(result, inventory, Constants.OUTPUT_START, Constants.OUTPUT_END);
        if (outputSlot == -1) return;
        ItemStack output = inventory.getItem(outputSlot);

        if (output.isEmpty()) {
            inventory.setItem(outputSlot, result.copy());
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
                    int inputSlot = -1;
                    if (inventory.getItem(Constants.INPUT_1).isEmpty()) {
                        inputSlot = Constants.INPUT_1;
                    } else if (inventory.getItem(Constants.INPUT_2).isEmpty()) {
                        inputSlot = Constants.INPUT_2;
                    }
                    if (inputSlot == -1) return;
                    inventory.setItem(inputSlot, stack.split(1));
                    chest.setChanged();
                    return;
                }
            }
        }
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
    private boolean isWood(ItemStack stack) { return stack.is(ItemTags.LOGS_THAT_BURN); }

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
     * Identifies the recipe type used by this furnace-style workbench.
     *
     * @return the RecipeType for furnace workbench recipes (ModRecipes.FURNACE_TYPE)
     */
    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.FURNACE_TYPE;
    }

    /**
     * Checks whether the workbench is lit and has a fuel item available.
     *
     * Returns false if the block entity is not attached to a level.
     *
     * @return `true` if the block's `LIT` property is present and true and the configured fuel slot is non-empty, `false` otherwise.
     */
    @Override
    protected boolean hasFuel() {
        if (this.level == null) return false;
        
        // Check if block is lit
        BlockState state = this.level.getBlockState(this.worldPosition);
        boolean isLit = state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT);
        
        boolean hasFuelItem = !this.getItem(Constants.FUEL_SLOT).isEmpty();
        
        return isLit && hasFuelItem;
    }
}
