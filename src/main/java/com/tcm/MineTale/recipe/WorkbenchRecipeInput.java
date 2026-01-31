package com.tcm.MineTale.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record WorkbenchRecipeInput(ItemStack inputA, ItemStack inputB) implements RecipeInput {
    
    /**
     * Retrieve the input ItemStack for the given slot index.
     *
     * @param index slot index (0 for inputA, 1 for inputB)
     * @return `inputA` if index is 0, `inputB` if index is 1, `ItemStack.EMPTY` for any other index
     */
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> inputA;
            case 1 -> inputB;
            default -> ItemStack.EMPTY;
        };
    }

    /**
     * The number of functional input slots for this recipe input.
     *
     * @return the number of input slots (2)
     */
    @Override
    public int size() {
        return 2; // Your machine has 2 functional input slots
    }
}