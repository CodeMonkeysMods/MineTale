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

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
    @Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
    private static void minetale$addCustomCategory(RecipeHolder<?> recipe, CallbackInfoReturnable<RecipeBookCategory> cir) {
        if (recipe.value().getType() == ModRecipes.FURNACE_SERIALIZER) {
            // This tells the search engine to put your recipes into your custom tab
            cir.setReturnValue(ModRecipeDisplay.CAMPFIRE_ALLOYING_SEARCH);
        }
    }
}