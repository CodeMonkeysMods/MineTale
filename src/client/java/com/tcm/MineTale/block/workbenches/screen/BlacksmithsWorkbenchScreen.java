package com.tcm.MineTale.block.workbenches.screen;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.AbstractWorkbenchContainerMenu;
import com.tcm.MineTale.block.workbenches.menu.BlacksmithsWorkbenchMenu;
import com.tcm.MineTale.mixin.client.ClientRecipeBookAccessor;
import com.tcm.MineTale.network.CraftRequestPayload;
import com.tcm.MineTale.recipe.MineTaleRecipeBookComponent;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlacksmithsWorkbenchScreen extends AbstractRecipeBookScreen<BlacksmithsWorkbenchMenu> {
    private static final Identifier TEXTURE =
        Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/workbench_workbench.png");

    private final MineTaleRecipeBookComponent mineTaleRecipeBook;

    private RecipeDisplayId lastKnownSelectedId = null;

    private Button craftOneBtn;
    private Button craftTenBtn;
    private Button craftAllBtn;

    /**
     * Initialize a workbench GUI screen using the provided container menu, player inventory, and title.
     *
     * @param menu      the menu supplying slots and synchronized state for this screen
     * @param inventory the player's inventory to display and interact with
     * @param title     the title component shown at the top of the screen
     */
    public BlacksmithsWorkbenchScreen(BlacksmithsWorkbenchMenu menu, Inventory inventory, Component title) {
        this(menu, inventory, title, createRecipeBookComponent(menu));
    }

    /**
     * Initialise a BlacksmithsWorkbenchScreen bound to the provided menu, player inventory, title and recipe book.
     *
     * @param menu       the workbench menu backing this screen
     * @param inventory  the player's inventory to display
     * @param title      the screen title component
     * @param recipeBook the MineTaleRecipeBookComponent used to display and manage recipes for this screen
     */
    private BlacksmithsWorkbenchScreen(BlacksmithsWorkbenchMenu menu, Inventory inventory, Component title, MineTaleRecipeBookComponent recipeBook) {
        super(menu, recipeBook, inventory, title);
        this.mineTaleRecipeBook = recipeBook;
    }

    /**
     * Create a MineTaleRecipeBookComponent configured for the workbench screen.
     *
     * @param menu the workbench menu used to initialize the recipe book component
     * @return a MineTaleRecipeBookComponent containing the workbench tab and associated recipe category
     */
    private static MineTaleRecipeBookComponent createRecipeBookComponent(BlacksmithsWorkbenchMenu menu) {
        ItemStack tabIcon = new ItemStack(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem());
        
        List<RecipeBookComponent.TabInfo> tabs = List.of(
            new RecipeBookComponent.TabInfo(tabIcon.getItem(), ModRecipeDisplay.BLACKSMITHS_SEARCH)
        );

        return new MineTaleRecipeBookComponent(menu, tabs, ModRecipes.BLACKSMITHS_TYPE);
    }

    /**
     * Initialises the screen's GUI size and adds the craft control buttons.
     *
     * Sets the screen image dimensions before delegating to the superclass, computes
     * default positions relative to the current GUI origin and adds three buttons:
     * "Craft" requests a single item, "x10" requests ten items and "All" requests
     * the full possible amount (represented by -1).
     */
    @Override
    protected void init() {
        // Important: Set your GUI size before super.init()
        this.imageWidth = 176;
        this.imageHeight = 166;
        
        super.init();

        int defaultLeft = this.leftPos + 90;
        int defaultTop = this.topPos + 25;

        this.craftOneBtn = addRenderableWidget(Button.builder(Component.translatable("gui.minetale.craftbtn"), (button) -> {
            handleCraftRequest(1);
        }).bounds(defaultLeft, defaultTop, 75, 20).build());

        this.craftTenBtn = addRenderableWidget(Button.builder(Component.literal("x10"), (button) -> {
            handleCraftRequest(10);
        }).bounds(defaultLeft, defaultTop + 22, 35, 20).build());

        this.craftAllBtn = addRenderableWidget(Button.builder(Component.translatable("gui.minetale.allbtn"), (button) -> {
            handleCraftRequest(-1); // -1 represents "All" logic
        }).bounds(defaultLeft + 40, defaultTop + 22, 35, 20).build());
    }

    /**
     * Request crafting for the currently selected recipe from the integrated recipe book.
     *
     * If a recipe is selected, sends a CraftRequestPayload to the server for that recipe and the
     * specified quantity. If no recipe is selected, no request is sent.
     *
     * @param amount the quantity to craft; use -1 to request crafting of the full available stack ("All")
     */
    private void handleCraftRequest(int amount) {
        // Look at our "Memory" instead of the component
        if (this.lastKnownSelectedId != null) {
            ClientRecipeBook book = this.minecraft.player.getRecipeBook();
            RecipeDisplayEntry entry = ((ClientRecipeBookAccessor) book).getKnown().get(this.lastKnownSelectedId);

            if (entry != null) {
                List<ItemStack> results = entry.resultItems(SlotDisplayContext.fromLevel(this.minecraft.level));
                if (!results.isEmpty()) {
                    System.out.println("Persistent Selection Success: " + results.get(0));
                    ClientPlayNetworking.send(new CraftRequestPayload(results.get(0), amount));
                    return;
                }
            }
        }
        System.out.println("Request failed: No recipe was ever selected!");
    }

    /**
    * Draws the workbench background texture at the screen's current GUI origin.
    *
    * @param guiGraphics the graphics context used to draw GUI elements
    * @param f           partial tick time for interpolation
    * @param i           current mouse x coordinate
    * @param j           current mouse y coordinate
    */
   protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
      int k = this.leftPos;
      int l = this.topPos;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
   }

    /**
     * Render the screen, preserve the recipe book selection and update craft-button enabled state
     * according to whether the player has sufficient ingredients for different craft counts.
     *
     * The method draws the screen background and base UI, remembers the currently selected recipe
     * from the recipe book for later resolution, enables or disables the craft buttons for
     * counts of 1, 2 and 10 based on ingredient availability, and renders any tooltips.
     *
     * @param graphics the graphics context to render with
     * @param mouseX   the current mouse X coordinate relative to the window
     * @param mouseY   the current mouse Y coordinate relative to the window
     * @param delta    the frame delta time (partial tick) for the current render frame
     */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);

        // 1. Get the current selection from the book
        RecipeDisplayId currentId = this.mineTaleRecipeBook.getSelectedRecipeId();
        
        // 2. If it's NOT null, remember it!
        if (currentId != null) {
            this.lastKnownSelectedId = currentId;
        }

        // 3. Use the remembered ID to find the entry for button activation
        RecipeDisplayEntry selectedEntry = null;
        if (this.lastKnownSelectedId != null && this.minecraft.level != null) {
            ClientRecipeBook book = this.minecraft.player.getRecipeBook();
            selectedEntry = ((ClientRecipeBookAccessor) book).getKnown().get(this.lastKnownSelectedId);
        }

        // 2. Button Activation Logic
        if (selectedEntry != null) {
            // We use the entry directly. It contains the 15 ingredients needed!
            boolean canCraftOne = canCraft(this.minecraft.player, selectedEntry, 1);
            boolean canCraftMoreThanOne = canCraft(this.minecraft.player, selectedEntry, 2);
            boolean canCraftTen = canCraft(this.minecraft.player, selectedEntry, 10);

            this.craftOneBtn.active = canCraftOne;
            this.craftTenBtn.active = canCraftTen;
            this.craftAllBtn.active = canCraftMoreThanOne;
        } else {
            this.craftOneBtn.active = false;
            this.craftTenBtn.active = false;
            this.craftAllBtn.active = false;
        }

        renderTooltip(graphics, mouseX, mouseY);
    }

    /**
         * Determine whether the player has sufficient ingredients to craft the given recipe the specified number of times.
         *
         * Returns `false` if `player` or `entry` is null or if the recipe has no crafting requirements.
         *
         * @param craftCount the number of times to craft the recipe; ingredient requirements are multiplied by this value
         * @return `true` if the player has at least the required quantity of each ingredient multiplied by `craftCount`, `false` otherwise
         */
    private boolean canCraft(Player player, RecipeDisplayEntry entry, int craftCount) {
        if (player == null || entry == null) return false;

        Optional<List<Ingredient>> reqs = entry.craftingRequirements();
        if (reqs.isEmpty()) return false;

        // 1. Group ingredients by their underlying Item Holders.
        // Using List<Holder<Item>> as the key ensures structural equality (content-based hashing).
        Map<List<Holder<Item>>, Integer> aggregatedRequirements = new HashMap<>();
        Map<List<Holder<Item>>, Ingredient> holderToIngredient = new HashMap<>();

        for (Ingredient ing : reqs.get()) {
            // Collect holders into a List to get a stable hashCode() and equals()
            @SuppressWarnings("deprecation")
            List<Holder<Item>> key = ing.items().toList(); 

            // Aggregate the counts (how many of this specific ingredient set are required)
            aggregatedRequirements.put(key, aggregatedRequirements.getOrDefault(key, 0) + 1);

            // Map the list back to the original ingredient for use in hasIngredientAmount
            holderToIngredient.putIfAbsent(key, ing);
        }

        // 2. Check the player's inventory against the aggregated totals
        Inventory inv = player.getInventory();
        for (Map.Entry<List<Holder<Item>>, Integer> entryReq : aggregatedRequirements.entrySet()) {
            List<Holder<Item>> key = entryReq.getKey();
            int totalNeeded = entryReq.getValue() * craftCount;
            
            // Retrieve the original Ingredient object associated with this list of holders
            Ingredient originalIng = holderToIngredient.get(key);
            
            if (!hasIngredientAmount(inv, originalIng, totalNeeded)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Checks whether the player inventory combined with the workbench's networked nearby items
     * contains at least the specified total quantity of the given ingredient.
     *
     * @param inventory     the inventory to search (player inventory)
     * @param ingredient    the ingredient matcher used to test ItemStacks
     * @param totalRequired the total number of matching items required
     * @return              `true` if the combined count from inventory and nearby networked items
     *                      is greater than or equal to `totalRequired`, `false` otherwise
     */
    private boolean hasIngredientAmount(Inventory inventory, Ingredient ingredient, int totalRequired) {
        System.out.println("DEBUG: Searching inventory + nearby for " + totalRequired + "...");
        if (totalRequired <= 0) return true;
        
        int found = 0;

        // 1. Check Player Inventory
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                found += stack.getCount();
            }
        }

        // 2. CHECK THE NETWORKED ITEMS FROM CHESTS
        // This is the list we sent via the packet!
        if (this.menu instanceof AbstractWorkbenchContainerMenu workbenchMenu) {
            for (ItemStack stack : workbenchMenu.getNetworkedNearbyItems()) {
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    found += stack.getCount();
                    System.out.println("DEBUG: Found " + stack.getCount() + " in nearby networked list. Total: " + found);
                }
            }
        }
        
        if (found >= totalRequired) {
            System.out.println("DEBUG: Requirement MET with " + found + "/" + totalRequired);
            return true;
        }
        
        System.out.println("DEBUG: FAILED. Only found: " + found + "/" + totalRequired);
        return false;
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