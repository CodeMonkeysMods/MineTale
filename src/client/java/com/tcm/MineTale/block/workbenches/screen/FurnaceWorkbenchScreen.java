package com.tcm.MineTale.block.workbenches.screen;

import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class FurnaceWorkbenchScreen extends AbstractContainerScreen<FurnaceWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        // Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/furnace_workbench.png");
        Identifier.withDefaultNamespace("textures/gui/container/furnace.png");

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
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
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
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }
}