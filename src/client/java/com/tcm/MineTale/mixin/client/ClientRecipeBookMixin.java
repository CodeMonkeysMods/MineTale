package com.tcm.MineTale.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
    /**
     * Maps supported custom recipe types to their corresponding recipe-book category and overrides the original method's result when a match is found.
     *
     * @param recipe the recipe holder to inspect for its recipe type
     * @param cir    callback that will be used to set and return the mapped {@code RecipeBookCategory} for the original method
     */
    @Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
    private static void minetale$addCustomCategory(RecipeHolder<?> recipe, CallbackInfoReturnable<RecipeBookCategory> cir) {
        RecipeType<?> type = recipe.value().getType();
    
        // Using .equals() satisfies the compiler
        if (ModRecipes.CAMPFIRE_TYPE.equals(type)) {
            cir.setReturnValue(ModRecipeDisplay.CAMPFIRE_SEARCH);
        } else if (ModRecipes.FURNACE_T1_TYPE.equals(type)) {
            cir.setReturnValue(ModRecipeDisplay.FURNACE_T1_SEARCH);
        } else if (ModRecipes.WORKBENCH_TYPE.equals(type)) {
            cir.setReturnValue(ModRecipeDisplay.WORKBENCH_SEARCH);
        }
    }
}