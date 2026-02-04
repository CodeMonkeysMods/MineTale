package com.tcm.MineTale.recipe;

import java.util.List;

import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.util.Constants;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class MineTaleRecipeBookComponent extends RecipeBookComponent<RecipeBookMenu> {

    // Standard button sprites (the "Filter" checkmark button)
    protected static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
        Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
        Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
        Identifier.withDefaultNamespace("recipe_book/filter_enabled_focused"),
        Identifier.withDefaultNamespace("recipe_book/filter_disabled_focused")
    );

    public MineTaleRecipeBookComponent(RecipeBookMenu recipeBookMenu, List<TabInfo> list) {
        super(recipeBookMenu, list);
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
        // Force everything to be "selected"
        // recipeCollection.selectRecipes(stackedItemContents, (recipeDisplay) -> true);

        recipeCollection.selectRecipes(stackedItemContents, (recipeDisplay) -> {
        // Only allow recipes that use your custom Workbench display type
        // This effectively filters out vanilla CraftingRecipeDisplays (the boats)
            return recipeDisplay.type() == ModRecipeDisplay.WORKBENCH_TYPE;
        });
    }

    

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        // Returns the textures for the "Toggle craftable" button
        return FILTER_BUTTON_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index == Constants.INPUT_START || slot.index == Constants.INPUT_START + 1;
    }

    @Override
    protected Component getRecipeFilterName() {
        // The text shown when hovering over the filter button
        return Component.translatable("gui.recipebook.toggleRecipes.all");
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
        // This places the faint "ghost" items in the workbench slots when hovering a recipe
        // We use SlotDisplayContext.fromLevel(this.minecraft.level) to handle dynamic displays
        // ghostSlots.setRecipe(recipeDisplay);
        
        // // We assume the first two slots of your menu are the inputs
        // // Your AbstractWorkbenchContainerMenu adds input slots first (index 0 and 1)
        // ghostSlots.addSlot(this.menu.slots.get(0), this.minecraft.level.registryAccess(), recipeDisplay.result());
        
        // // If your custom recipe has specific inputs, you'd map them here. 
        // // For a generic implementation, we use the display's suggested placement:
        // recipeDisplay.setupGhostSlots(ghostSlots, SlotDisplayContext.fromLevel(this.minecraft.level));
    }

}