package com.tcm.MineTale.block.workbenches.screen;

import java.util.List;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.WorkbenchWorkbenchMenu;
import com.tcm.MineTale.mixin.client.RecipeBookComponentAccessor;
import com.tcm.MineTale.network.CraftRequestPayload;
import com.tcm.MineTale.recipe.MineTaleRecipeBookComponent;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.network.chat.Component;

public class WorkbenchWorkbenchScreen extends AbstractRecipeBookScreen<WorkbenchWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/workbench_workbench.png");

    private final MineTaleRecipeBookComponent mineTaleRecipeBook;

    private Button craftOneBtn;
    private Button craftThirtyBtn;
    private Button craftAllBtn;

    /**
     * Initialize a workbench GUI screen using the provided container menu, player inventory, and title.
     *
     * @param menu      the menu supplying slots and synchronized state for this screen
     * @param inventory the player's inventory to display and interact with
     * @param title     the title component shown at the top of the screen
     */
    public WorkbenchWorkbenchScreen(WorkbenchWorkbenchMenu menu, Inventory inventory, Component title) {
        this(menu, inventory, title, createRecipeBookComponent(menu));
    }

    /**
     * Creates a WorkbenchWorkbenchScreen bound to the given menu, player inventory, title, and recipe book component.
     *
     * @param menu        the menu backing this screen
     * @param inventory   the player's inventory shown in the screen
     * @param title       the screen title component
     * @param recipeBook  the MineTaleRecipeBookComponent used to display and manage recipes in this screen
     */
    private WorkbenchWorkbenchScreen(WorkbenchWorkbenchMenu menu, Inventory inventory, Component title, MineTaleRecipeBookComponent recipeBook) {
        super(menu, recipeBook, inventory, title);
        this.mineTaleRecipeBook = recipeBook;
    }

    /**
     * Create a MineTaleRecipeBookComponent configured for the workbench screen.
     *
     * @param menu the workbench menu used to initialize the recipe book component
     * @return a MineTaleRecipeBookComponent containing the workbench tab and associated recipe category
     */
    private static MineTaleRecipeBookComponent createRecipeBookComponent(WorkbenchWorkbenchMenu menu) {
        ItemStack tabIcon = new ItemStack(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem());
        
        List<RecipeBookComponent.TabInfo> tabs = List.of(
            new RecipeBookComponent.TabInfo(tabIcon.getItem(), ModRecipeDisplay.WORKBENCH_SEARCH)
        );

        return new MineTaleRecipeBookComponent(menu, tabs, ModRecipes.WORKBENCH_TYPE);
    }

    /**
         * Configure the screen's GUI dimensions and initialize widgets.
         *
         * Sets the layout size (imageWidth = 176, imageHeight = 166), delegates remaining
         * layout initialization to the superclass, and creates the three craft buttons
         * ("1", "30", "All") wired to their respective handlers.
         */
    @Override
    protected void init() {
        // Important: Set your GUI size before super.init()
        this.imageWidth = 176;
        this.imageHeight = 166;
        
        super.init();

        this.craftOneBtn = addRenderableWidget(Button.builder(Component.literal("1"), (button) -> {
            handleCraftRequest(1);
        }).bounds(this.leftPos + 80, this.topPos + 20, 30, 20).build());

        this.craftThirtyBtn = addRenderableWidget(Button.builder(Component.literal("30"), (button) -> {
            handleCraftRequest(30);
        }).bounds(this.leftPos + 112, this.topPos + 20, 30, 20).build());

        this.craftAllBtn = addRenderableWidget(Button.builder(Component.literal("All"), (button) -> {
            handleCraftRequest(-1); // -1 represents "All" logic
        }).bounds(this.leftPos + 144, this.topPos + 20, 30, 20).build());
    }

    /**
     * Sends a crafting request for the currently selected recipe in the integrated recipe book.
     *
     * Locates the last recipe collection and last selected recipe ID from the recipe book component,
     * resolves the recipe's result item, and sends a CraftRequestPayload to the server containing that
     * item and the requested amount.
     *
     * @param amount the quantity to craft; use -1 to request crafting of the full available stack ("All")
     */

    private void handleCraftRequest(int amount) {
        // 1. Cast the book component to the Accessor to get the selected data
        RecipeBookComponentAccessor accessor = (RecipeBookComponentAccessor) this.mineTaleRecipeBook;
        
        RecipeCollection collection = accessor.getLastRecipeCollection();
        RecipeDisplayId displayId = accessor.getLastRecipe();

        if (collection != null && displayId != null) {
            // 2. Find the visual entry
            for (RecipeDisplayEntry entry : collection.getSelectedRecipes(RecipeCollection.CraftableStatus.ANY)) {
                if (entry.id().equals(displayId)) {
                    // 3. Resolve result for the packet
                    List<ItemStack> results = entry.resultItems(SlotDisplayContext.fromLevel(this.minecraft.level));
                    
                    if (!results.isEmpty()) {
                        ItemStack resultStack = results.get(0);
                        
                        // 4. LOG FOR DEBUGGING
                        System.out.println("Sending craft request for: " + resultStack + " amount: " + amount);
                        
                        ClientPlayNetworking.send(new CraftRequestPayload(resultStack, amount));
                    }
                    break;
                }
            }
        } else {
            System.out.println("Request failed: Collection or DisplayID is null!");
        }
    }

    /**
    * Draws the workbench GUI background texture at the screen's top-left corner.
    *
    * @param guiGraphics the graphics context used to draw GUI elements
    * @param f           partial tick time for interpolation
    * @param i           current mouse x coordinate relative to the window
    * @param j           current mouse y coordinate relative to the window
    */
   protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
      int k = this.leftPos;
      int l = this.topPos;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
   }

    /**
     * Renders the workbench screen including the background tint, GUI elements, and tooltips.
     *
     * @param graphics the graphics context
     * @param mouseX the current mouse x-coordinate
     * @param mouseY the current mouse y-coordinate
     * @param delta the partial tick delta for frame interpolation
     */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // 1. Always render the dark background tint first
        renderBackground(graphics, mouseX, mouseY, delta);

        // 3. Call super (this draws your slots and items)
        super.render(graphics, mouseX, mouseY, delta);

        boolean hasSelection = this.mineTaleRecipeBook.getSelectedRecipeId() != null;
        this.craftOneBtn.active = hasSelection;
        this.craftThirtyBtn.active = hasSelection;
        this.craftAllBtn.active = hasSelection;

        renderTooltip(graphics, mouseX, mouseY);
    }

    /**
     * Computes the on-screen position for the recipe book toggle button for this GUI.
     *
     * @return the screen position placed 5 pixels from the GUI's left edge and 49 pixels above the GUI's vertical center
     */
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