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
import net.minecraft.world.item.crafting.Ingredient;
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

    // Defined by the subclass via constructor
    protected final int inputEnd;
    protected final int outputEnd;

    public AbstractFurnaceWorkbenchEntity(BlockEntityType<? extends AbstractFurnaceWorkbenchEntity> type, BlockPos pos, BlockState state, int inputEnd, int outputEnd) {
        super(type, pos, state);
        this.inputEnd = inputEnd;
        this.outputEnd = outputEnd;
    }

    // --- Getters and Setters ---
    public int getFuelTime() { return this.fuelTime; }
    public void setFuelTime(int t) { this.fuelTime = t; }
    public int getCookTime() { return this.cookTime; }
    public void setCookTime(int t) { this.cookTime = t; }
    public int getCookTimeTotal() { return this.cookTimeTotal; }
    public void setCookTimeTotal(int t) { this.cookTimeTotal = t; }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        boolean changed = false;
        
        // 1. Shift queue forward so the next item is ready to smelt
        if (shiftQueueForward()) {
            changed = true;
        }

        ItemStack activeInput = inventory.getItem(Constants.INPUT_START);
        ItemStack fuel = inventory.getItem(Constants.FUEL_SLOT);

        // 2. Processing logic (Pulling from chests removed from here)
        if (canSmelt(activeInput)) {
            int speedBoost = (this.tier - 1) * 50;
            int currentTotal = Math.max(20, this.cookTimeTotal - speedBoost);

            if (this.fuelTime > 0 || !fuel.isEmpty()) {
                if (this.fuelTime <= 0 && consumeFuel(fuel)) {
                    changed = true;
                }

                if (this.fuelTime > 0) {
                    this.fuelTime--;
                    this.cookTime++;
                    if (this.cookTime >= currentTotal) {
                        smeltItem(activeInput);
                        this.cookTime = 0;
                        changed = true;
                    }
                }
            }
        } else {
            this.cookTime = 0;
        }

        if (changed) setChanged();
    }

    /**
     * Iterates through the queue range. If a slot is empty, it pulls the item 
     * from the slot behind it.
     */
    private boolean shiftQueueForward() {
        boolean moved = false;
        // Start from the front and pull from the back
        for (int i = Constants.INPUT_START; i < this.inputEnd; i++) {
            ItemStack current = inventory.getItem(i);
            ItemStack next = inventory.getItem(i + 1);

            if (current.isEmpty() && !next.isEmpty()) {
                inventory.setItem(i, next.copy());
                inventory.setItem(i + 1, ItemStack.EMPTY);
                moved = true;
            }
        }
        return moved;
    }

    private void smeltItem(ItemStack input) {
        ItemStack result = isWood(input) ? new ItemStack(Items.CHARCOAL) : new ItemStack(Items.COPPER_INGOT);

        // Uses your existing findOutputSlot logic
        int outputSlot = this.findOutputSlot(result, inventory, this.inputEnd + 1, this.outputEnd);
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
     * CALLED BY SCREENHANDLER / RECIPE BOOK
     * Searches nearby chests for items matching the selected recipe and 
     * fills any empty slots in the queue.
     */
    public void fulfillRecipeFromNearby(WorkbenchRecipe recipe) {
        if (this.level == null || this.level.isClientSide()) return;

        // Assuming your WorkbenchRecipe has a method to get its input ingredient
        // If it's a standard furnace-style recipe, it likely has one ingredient.
        Ingredient ingredient = recipe.ingredients().get(0); 
        List<Container> nearby = this.getNearbyInventories();

        // Iterate through our queue slots
        for (int slot = Constants.INPUT_START; slot <= this.inputEnd; slot++) {
            // Only try to fill if the slot is currently empty
            if (inventory.getItem(slot).isEmpty()) {
                
                // Look through nearby chests
                for (Container chest : nearby) {
                    for (int i = 0; i < chest.getContainerSize(); i++) {
                        ItemStack stackInChest = chest.getItem(i);
                        
                        if (!stackInChest.isEmpty() && ingredient.test(stackInChest)) {
                            // Take 1 (or a full stack if you prefer) and put it in the queue
                            inventory.setItem(slot, stackInChest.split(1));
                            chest.setChanged();
                            this.setChanged();
                            
                            // Break to next queue slot once this one is filled
                            break; 
                        }
                    }
                    // If we filled the slot, stop looking at other chests for this specific slot
                    if (!inventory.getItem(slot).isEmpty()) break;
                }
            }
        }
    }

    private boolean consumeFuel(ItemStack fuel) {
        if (fuel.is(Items.STICK) || fuel.is(Items.STRING) || isWood(fuel)) {
            this.fuelTime = 100; 
            fuel.shrink(1);
            return true;
        }
        return false;
    }

    private boolean canSmelt(ItemStack input) {
        return !input.isEmpty() && (isOre(input) || isWood(input));
    }

    private boolean isOre(ItemStack stack) { return stack.is(Items.RAW_COPPER); }
    private boolean isWood(ItemStack stack) { return stack.is(ItemTags.LOGS_THAT_BURN); }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.store("CookTime", Codec.INT, this.cookTime);
        valueOutput.store("FuelTime", Codec.INT, this.fuelTime);
        valueOutput.store("WorkbenchTier", Codec.INT, this.tier);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.cookTime = valueInput.read("CookTime", Codec.INT).orElse(0);
        this.fuelTime = valueInput.read("FuelTime", Codec.INT).orElse(0);
        this.tier = valueInput.read("WorkbenchTier", Codec.INT).orElse(1);
    }

    @Override
    public RecipeType<WorkbenchRecipe> getWorkbenchRecipeType() {
        return ModRecipes.FURNACE_TYPE;
    }

    @Override
    protected boolean hasFuel() {
        if (this.level == null) return false;
        BlockState state = this.level.getBlockState(this.worldPosition);
        boolean isLit = state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT);
        return isLit || this.fuelTime > 0;
    }
}
