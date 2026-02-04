package com.tcm.MineTale.block.workbenches.screen;

import java.util.List;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.CampfireWorkbenchMenu;
import com.tcm.MineTale.recipe.MineTaleRecipeBookComponent;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.network.chat.Component;

// FurnaceScreen

public class CampfireWorkbenchScreen extends AbstractRecipeBookScreen<CampfireWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/furnace_workbench.png");

    /**
     * Creates a campfire workbench screen for the provided menu, player inventory, and title.
     *
     * @param menu      the container menu that provides slots and synchronizes state for this screen
     * @param inventory the player's inventory to display and interact with
     * @param title     the title component shown at the top of the screen
     */
    public CampfireWorkbenchScreen(CampfireWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, createRecipeBookComponent(menu), inventory, title);
    }

    /**
     * Static helper to build the component with the custom MineTale tabs 
     * before the super constructor is called.
     */
    private static MineTaleRecipeBookComponent createRecipeBookComponent(CampfireWorkbenchMenu menu) {
        ItemStack tabIcon = new ItemStack(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK.asItem());
        
        // CHANGE THIS: Replace CRAFTING_MISC with your custom category
        List<RecipeBookComponent.TabInfo> tabs = List.of(
            new RecipeBookComponent.TabInfo(tabIcon.getItem(), ModRecipeDisplay.CAMPFIRE_ALLOYING_SEARCH)
        );

        return new MineTaleRecipeBookComponent(menu, tabs);
    }

    /**
     * Initializes the screen and centers the title horizontally by setting {@code titleLabelX}.
     */
    @Override
    protected void init() {
        // Important: Set your GUI size before super.init()
        this.imageWidth = 176;
        this.imageHeight = 166;
        
        super.init();

        // // The component is already created by the constructor, just initialize its UI state
        // this.recipeBookComponent.init(this.width, this.height, this.minecraft, false);
        // this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);

        // // Add the toggle button
        // this.addRenderableWidget(new ImageButton(
        //     this.leftPos + 5, 
        //     this.height / 2 - 49, 
        //     20, 18, 
        //     RecipeBookComponent.RECIPE_BUTTON_SPRITES, 
        //     (button) -> {
        //         this.recipeBookComponent.toggleVisibility();
        //         this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        //         button.setPosition(this.leftPos + 5, this.height / 2 - 49);
        //     }
        // ));

        // this.addWidget(this.recipeBookComponent);
    }

    /**
    * Renders the campfire workbench background texture at the screen's top-left position.
    *
    * @param guiGraphics the graphics context used for drawing
    * @param f           partial ticks for interpolation
    * @param i           current mouse x position
    * @param j           current mouse y position
    */
   protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
      int k = this.leftPos;
      int l = this.topPos;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
   }

    /**
         * Renders the campfire workbench screen, drawing its background, contents, and tooltips.
         *
         * @param graphics the graphics context used for rendering
         * @param mouseX   the current mouse X coordinate
         * @param mouseY   the current mouse Y coordinate
         * @param delta    the frame time delta (partial tick) used for animated rendering
         */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // 1. Always render the dark background tint first
        renderBackground(graphics, mouseX, mouseY, delta);

        // 2. Add a null check before calling ANY methods on the component
        // if (this.recipeBookComponent != null) {
        //     this.recipeBookComponent.render(graphics, mouseX, mouseY, delta);
        // }

        // 3. Call super (this draws your slots and items)
        super.render(graphics, mouseX, mouseY, delta);

        // 4. Ghost recipes and Tooltips also need the null check
        // if (this.recipeBookComponent != null) {
        //     this.recipeBookComponent.renderGhostRecipe(graphics, true);
        //     this.recipeBookComponent.renderTooltip(graphics, this.leftPos, this.topPos, this.hoveredSlot);
        // }

        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        // 1. Calculate the start (left) of your workbench GUI
        int guiLeft = (this.width - this.imageWidth) / 2;
        
        // 2. Calculate the top of your workbench GUI
        int guiTop = (this.height - this.imageHeight) / 2;

        // 3. Standard Vanilla positioning: 
        // Usually 5 pixels in from the left and 49 pixels up from the center
        return new ScreenPosition(guiLeft + 5, guiTop + this.imageHeight / 2 - 49);
    }
}