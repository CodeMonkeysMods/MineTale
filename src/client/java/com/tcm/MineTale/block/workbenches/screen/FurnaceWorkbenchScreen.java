package com.tcm.MineTale.block.workbenches.screen;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;
import com.tcm.MineTale.recipe.FurnaceRecipeBookComponent;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class FurnaceWorkbenchScreen extends AbstractContainerScreen<FurnaceWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/furnace_workbench.png");

    private FurnaceRecipeBookComponent recipeBookComponent;

    /**
     * Creates a new furnace workbench screen for the given menu, player inventory, and title.
     *
     * @param menu      the container menu that provides slots and syncs state for this screen
     * @param inventory the player's inventory to display and interact with
     * @param title     the title component shown at the top of the screen
     */
    public FurnaceWorkbenchScreen(FurnaceWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    /**
     * Initializes the screen and centers the title horizontally by setting {@code titleLabelX}.
     */
    @Override
    protected void init() {
        super.init();

        // 2. Initialize it here where 'this.menu' is available
        if (this.recipeBookComponent == null) {
            this.recipeBookComponent = new FurnaceRecipeBookComponent(this.menu);
        }

        // Standard init and visibility logic
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, false);
        if (!this.recipeBookComponent.isVisible()) {
            this.recipeBookComponent.toggleVisibility();
        }
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        // 1. Let the search bar in the recipe book use the key first
        // If your RecipeBookComponent doesn't take KeyEvent, you'll need to map 
        // the keyEvent.getKeyCode() to the standard int keyCode version.
        if (this.recipeBookComponent.keyPressed(keyEvent)) {
            return true;
        }

        // 2. Identify the Recipe Book toggle key (Standard 'B')
        // We check the code to see if it matches the vanilla keybind
        // int keyCode = keyEvent.getKeyCode();
        // this.minecraft.options.ke
        // if (this.minecraft.options.keyRecipeBook.matches(keyCode, 0)) {
        //     return true; // Return true to "consume" the press and do nothing
        // }

        return super.keyPressed(keyEvent);
    }

    /**
    * Draws the furnace workbench background texture onto the screen.
    *
    * @param guiGraphics the graphics context used for drawing
    * @param f           partial tick time used for interpolation
    * @param i           current mouse x position
    * @param j           current mouse y position
    */
   protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
      int k = this.leftPos;
      int l = this.topPos;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
   }

    /**
     * Renders the furnace workbench screen, drawing its background, contents, and tooltips.
     *
     * @param graphics the graphics context used for rendering
     * @param mouseX   the current mouse X coordinate
     * @param mouseY   the current mouse Y coordinate
     * @param delta    the frame time delta (partial tick) used for animated rendering
     */
    // @Override
    // public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    //     // Render the dark background tint
    //     renderBackground(graphics, mouseX, mouseY, delta);

    //     // Always render the book and the main GUI together
    //     this.recipeBookComponent.render(graphics, mouseX, mouseY, delta);
    //     super.render(graphics, mouseX, mouseY, delta);
    //     this.recipeBookComponent.renderGhostRecipe(graphics, true);

    //     renderTooltip(graphics, mouseX, mouseY);
    //     // this.recipeBookComponent.renderTooltip(graphics, this.leftPos, this.topPos, mouseX, mouseY);
    // }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // 1. Always render the dark background tint first
        renderBackground(graphics, mouseX, mouseY, delta);

        // 2. Add a null check before calling ANY methods on the component
        if (this.recipeBookComponent != null) {
            this.recipeBookComponent.render(graphics, mouseX, mouseY, delta);
        }

        // 3. Call super (this draws your slots and items)
        super.render(graphics, mouseX, mouseY, delta);

        // 4. Ghost recipes and Tooltips also need the null check
        if (this.recipeBookComponent != null) {
            this.recipeBookComponent.renderGhostRecipe(graphics, true);
            this.recipeBookComponent.renderTooltip(graphics, this.leftPos, this.topPos, this.hoveredSlot);
        }

        renderTooltip(graphics, mouseX, mouseY);
    }
}