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
    /**
     * Gets the currently selected recipe display identifier from the recipe book component.
     *
     * @return the currently selected RecipeDisplayId
     */
    @Accessor("lastRecipe")
    RecipeDisplayId getLastRecipe();

    /**
     * Access the last-selected recipe collection from the recipe book component.
     *
     * @return the last-selected {@link RecipeCollection}
     */
    @Accessor("lastRecipeCollection")
    RecipeCollection getLastRecipeCollection();

    /**
     * Set the currently selected recipe display identifier on the component.
     *
     * @param id the RecipeDisplayId to set as the current selection
     */
    @Accessor("lastRecipe")
    void setLastRecipe(RecipeDisplayId id);

    /**
     * Sets the currently selected recipe collection in the recipe book component.
     *
     * @param collection the recipe collection to set as the selected collection
     */
    @Accessor("lastRecipeCollection")
    void setLastRecipeCollection(RecipeCollection collection);

    /**
     * Accesses the component's current recipe book page.
     *
     * @return the current RecipeBookPage instance
     */
    @Accessor("recipeBookPage")
    RecipeBookPage getRecipeBookPage();

    /**
     * Obtain the X origin coordinate of the recipe book component.
     *
     * @return the X origin coordinate of the component
     */
    @Invoker("getXOrigin")
    int invokeGetXOrigin();

    /**
     * Obtain the Y origin (vertical coordinate) of the recipe book component.
     *
     * @return the Y origin (vertical coordinate) of the component
     */
    @Invoker("getYOrigin")
    int invokeGetYOrigin();
}