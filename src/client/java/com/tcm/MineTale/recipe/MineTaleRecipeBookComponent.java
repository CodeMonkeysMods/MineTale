package com.tcm.MineTale.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.tcm.MineTale.mixin.client.RecipeBookComponentAccessor;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;
import com.tcm.MineTale.util.Constants;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

public class MineTaleRecipeBookComponent extends RecipeBookComponent<RecipeBookMenu> {
    private final RecipeType<?> filterType; // The specific machine type

    // Standard button sprites (the "Filter" checkmark button)
    protected static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
        Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
        Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
        Identifier.withDefaultNamespace("recipe_book/filter_enabled_focused"),
        Identifier.withDefaultNamespace("recipe_book/filter_disabled_focused")
    );

    /**
     * Creates a MineTaleRecipeBookComponent bound to a specific machine recipe type.
     *
     * @param recipeBookMenu the recipe book menu instance this component is attached to
     * @param list           the list of tab information to display in the recipe book
     * @param filterType     the RecipeType used to filter which recipes are shown in this component
     */
    public MineTaleRecipeBookComponent(RecipeBookMenu recipeBookMenu, List<TabInfo> list, RecipeType<?> filterType) {
        super(recipeBookMenu, list);
        this.filterType = filterType;
    }

    /**
     * Get the ID of the last recipe clicked in the current recipe book page.
     *
     * @return the `RecipeDisplayId` of the last clicked recipe, or `null` if there is no open page or no recipe has been clicked
     */
    public @Nullable RecipeDisplayId getSelectedRecipeId() {
        // Cast 'this' to the Accessor interface to call the generated getter
        RecipeBookPage page = ((RecipeBookComponentAccessor)this).getRecipeBookPage();
        
        if (page != null) {
            return page.getLastClickedRecipe();
        }
        return null;
    }

    /**
     * Filters the provided recipe collection to include only workbench displays that match this component's filterType.
     *
     * Uses the provided StackedItemContents to perform matching and retains only recipe displays whose type is
     * ModRecipeDisplay.WORKBENCH_TYPE and whose WorkbenchRecipeDisplay.getRecipeType() equals this.filterType.
     *
     * @param recipeCollection the collection whose selectable recipes will be updated
     * @param stackedItemContents the available stacked item contents used for recipe matching
     */
    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
        // Force everything to be "selected"
        // recipeCollection.selectRecipes(stackedItemContents, (recipeDisplay) -> true);

        // recipeCollection.selectRecipes(stackedItemContents, (recipeDisplay) -> {
        // // Only allow recipes that use your custom Workbench display type
        // // This effectively filters out vanilla CraftingRecipeDisplays (the boats)
        //     return recipeDisplay.type() == ModRecipeDisplay.WORKBENCH_TYPE;
        // });

        recipeCollection.selectRecipes(stackedItemContents, (recipeDisplay) -> {
            // 1. Check if the display matches your custom type
            if (recipeDisplay.type() != ModRecipeDisplay.WORKBENCH_TYPE) return false;

            // 2. We need to verify if the underlying recipe matches the current block's type
            // Note: In 1.21+, you may need to cast the display or check the recipe's origin
            // Here is the logic to ensure we only show recipes meant for THIS specific machine:
            return recipeDisplay instanceof WorkbenchRecipeDisplay wbDisplay && 
                   wbDisplay.getRecipeType() == this.filterType;
        });
    }

    /**
     * Handle mouse clicks inside the recipe book and select MineTale workbench recipes when clicked.
     *
     * @param mouseButtonEvent the mouse event to process
     * @param bl               a pass-through boolean flag forwarded to the underlying recipe page click handler
     * @return                 `true` if the click was handled by selecting a MineTale workbench recipe, `false` otherwise
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (!this.isVisible() || this.minecraft.player.isSpectator()) {
            return false;
        }

        RecipeBookComponentAccessor accessor = (RecipeBookComponentAccessor) this;
        int xOrigin = accessor.invokeGetXOrigin();
        int yOrigin = accessor.invokeGetYOrigin();
        RecipeBookPage page = accessor.getRecipeBookPage();

        if (page.mouseClicked(mouseButtonEvent, xOrigin, yOrigin, 147, 166, bl)) {
            RecipeDisplayId recipeDisplayId = page.getLastClickedRecipe();
            RecipeCollection recipeCollection = page.getLastClickedRecipeCollection();

            if (recipeDisplayId != null && recipeCollection != null) {
                // Check if ANY recipe in this collection belongs to your workbench category
                boolean isMineTaleRecipe = recipeCollection.getRecipes().stream()
                    .anyMatch(entry -> entry.category().equals(ModRecipeDisplay.WORKBENCH_SEARCH));

                if (isMineTaleRecipe) {
                    // Logic for your Workbench: Just select, don't "place"
                    accessor.setLastRecipeCollection(recipeCollection);
                    accessor.setLastRecipe(recipeDisplayId);
                    
                    this.minecraft.getSoundManager().play(net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                        net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    
                    return true; 
                }
            }
        }

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    /**
     * Provide the sprite set used by the recipe book's filter toggle button.
     *
     * @return the WidgetSprites used for the filter toggle (enabled/disabled and focused states)
     */
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