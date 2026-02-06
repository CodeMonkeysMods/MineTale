package com.tcm.MineTale.mixin.client;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeBookComponent.class)
public interface RecipeBookComponentAccessor {
    // GETTERS for the Screen to read what is selected
    @Accessor("lastRecipe")
    RecipeDisplayId getLastRecipe();

    @Accessor("lastRecipeCollection")
    RecipeCollection getLastRecipeCollection();

    // SETTERS for the Component to update the selection
    @Accessor("lastRecipe")
    void setLastRecipe(RecipeDisplayId id);

    @Accessor("lastRecipeCollection")
    void setLastRecipeCollection(RecipeCollection collection);

    // OTHER NECESSITIES
    @Accessor("recipeBookPage")
    RecipeBookPage getRecipeBookPage();

    @Invoker("getXOrigin")
    int invokeGetXOrigin();

    @Invoker("getYOrigin")
    int invokeGetYOrigin();
}