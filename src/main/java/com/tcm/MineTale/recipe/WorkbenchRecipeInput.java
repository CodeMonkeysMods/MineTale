package com.tcm.MineTale.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record WorkbenchRecipeInput(ItemStack inputA, ItemStack inputB) implements RecipeInput {
    
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> inputA;
            case 1 -> inputB;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2; // Your machine has 2 functional input slots
    }
}