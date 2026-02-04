package com.tcm.MineTale.recipe;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.GhostSlotsProxy;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

public class FurnaceRecipeBookComponent extends RecipeBookComponent<RecipeBookMenu> {
    
    public FurnaceRecipeBookComponent(RecipeBookMenu menu) {
        // Pass the menu and the list of tabs required by the super constructor
        super(menu, List.of(
            new RecipeBookComponent.TabInfo(new ItemStack(Items.PORKCHOP), Optional.empty(), RecipeBookCategories.FURNACE_FOOD)
        ));
    }

    // Standard Furnace filter button textures (the little flame/ore icon)
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
        Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
        Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
        Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
        Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted")
    );

    @Override
    public void slotClicked(@Nullable Slot slot) {
        // This is often where the 'can I craft this' state is refreshed
        super.slotClicked(slot);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_BUTTON_SPRITES;
    }

    @Override
    protected Component getRecipeFilterName() {
        // This is the tooltip text when hovering over the "show craftable" toggle
        return Component.translatable("gui.recipebook.toggleRecipes.smeltable");
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        // Temporarily allow the book to 'see' all furnace slots (0-3)
        // to see if the pork recipe finally enables.
        return slot.index == 0 || slot.index == 1;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
        // selectRecipes takes the items you have (stackedContents) 
        // and a Predicate to decide if the recipe "fits" this machine.
        recipeCollection.selectRecipes(stackedItemContents, recipeDisplay -> {
            // Option A: Check if it's your custom display type (if you made one)
            // Option B: For now, let's accept everything to see if it lights up
            return true; 
        });
    }

    // @Override
    // protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
    //     GhostSlotsProxy.setResultProxy(ghostSlots, this.menu.getSlot(3), contextMap, recipeDisplay.result());

    //     if (recipeDisplay instanceof FurnaceRecipeDisplay cooking) {
    //         GhostSlotsProxy.setInputProxy(ghostSlots, this.menu.getSlot(1), contextMap, cooking.ingredient());
    //     }
    // }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
        // 1. Set the Result (Index 2 in your Menu)
        // recipeDisplay.result() provides the SlotDisplay for the output
        GhostSlotsProxy.setResultProxy(ghostSlots, this.menu.getSlot(2), contextMap, recipeDisplay.result());

        
        // 2. Map Ingredients to your Inputs (Indices 0 and 1)
        SlotDisplay ingredient = recipeDisplay.result();
        
        if (ingredient != null) {
            GhostSlotsProxy.setInputProxy(ghostSlots, this.menu.getSlot(0), contextMap, ingredient);
        }
    }

    // @Override
    // protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
    //     // 1. Clear any existing ghost items first
    //     ghostSlots.clear();

    //     // 2. Set the Result (Output) Slot Ghost
    //     // Assuming index 2 is your output slot in the Menu
    //     ghostSlots.setResult(this.menu.slots.get(2), contextMap, recipeDisplay.result());

    //     // 3. Set the Input (Ingredient) Slot Ghost
    //     // We check if the recipe has ingredients to avoid IndexOutOfBounds
    //     if (!recipeDisplay.ingredients().isEmpty()) {
    //         // We target the 'Active' processing slot (index 0)
    //         // This is the physical slot at the front of your queue
    //         SlotDisplay inputDisplay = recipeDisplay.ingredients().get(0);
    //         ghostSlots.setInput(this.menu.slots.get(0), contextMap, inputDisplay);
    //     }
    // }
}