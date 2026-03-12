package com.tcm.MineTale.block.workbenches.screen;

import java.util.List;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.FurnaceWorkbenchMenu;
import com.tcm.MineTale.recipe.MineTaleRecipeBookComponent;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public class    FurnaceWorkbenchScreen extends AbstractRecipeBookScreen<FurnaceWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/furnace_workbench.png");

    /**
     * Creates a new furnace workbench screen.
     * Note: recipeBookComponent is inherited from AbstractRecipeBookScreen.
     */
    public FurnaceWorkbenchScreen(FurnaceWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, createRecipeBookComponent(menu), inventory, title);
    }

    /**
     * Static helper to build the component with the custom MineTale tabs.
     * This uses the FURNACE_WORKBENCH icon for this specific screen's tab.
     */
    private static MineTaleRecipeBookComponent createRecipeBookComponent(FurnaceWorkbenchMenu menu) {
        ItemStack tabIcon = new ItemStack(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1.asItem());
        
        List<RecipeBookComponent.TabInfo> tabs = List.of(
            new RecipeBookComponent.TabInfo(tabIcon.getItem(), ModRecipeDisplay.FURNACE_T1_SEARCH)
        );

        return new MineTaleRecipeBookComponent(menu, tabs, ModRecipes.FURNACE_T1_TYPE);
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = 166;
        
        super.init();

        // // Initialize the inherited recipeBookComponent UI state
        // this.recipeBookComponent.init(this.width, this.height, this.minecraft, false);
        // this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);

        // // The toggle button is managed via getRecipeBookButtonPosition() in 1.21.1 
        // // but we add the ImageButton manually to match your CampfireWorkbenchScreen logic exactly.
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
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = this.leftPos;
        int l = this.topPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);

        super.render(graphics, mouseX, mouseY, delta);

        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        return new ScreenPosition(guiLeft + 5, guiTop + this.imageHeight / 2 - 49);
    }
}