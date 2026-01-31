package com.tcm.MineTale.block.workbenches.screen;

import com.tcm.MineTale.block.workbenches.menu.CampfireWorkbenchMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class CampfireWorkbenchScreen extends AbstractContainerScreen<CampfireWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        // Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/furnace_workbench.png");
        Identifier.withDefaultNamespace("textures/gui/container/furnace.png");

    /**
     * Creates a campfire workbench screen for the provided menu, player inventory, and title.
     *
     * @param menu      the container menu that provides slots and synchronizes state for this screen
     * @param inventory the player's inventory to display and interact with
     * @param title     the title component shown at the top of the screen
     */
    public CampfireWorkbenchScreen(CampfireWorkbenchMenu menu, Inventory inventory, Component title) {
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
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }
}