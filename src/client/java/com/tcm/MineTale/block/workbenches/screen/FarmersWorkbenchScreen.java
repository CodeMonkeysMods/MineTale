package com.tcm.MineTale.block.workbenches.screen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.AbstractWorkbenchContainerMenu;
import com.tcm.MineTale.block.workbenches.menu.FarmersWorkbenchMenu;
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
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.network.chat.Component;

public class FarmersWorkbenchScreen extends AbstractRecipeBookScreen<FarmersWorkbenchMenu> {
    private static final Identifier TEXTURE = 
        Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "textures/gui/container/workbench_workbench.png");

    private final MineTaleRecipeBookComponent mineTaleRecipeBook;

    private RecipeDisplayId lastKnownSelectedId = null;

    private Button craftOneBtn;
    private Button craftTenBtn;
    private Button craftAllBtn;

    /**
     * Creates a FarmersWorkbenchScreen configured with the provided container menu, player inventory, and title.
     *
     * @param menu      the menu supplying slots and synchronized state for this screen
     * @param inventory the player's inventory to display and interact with
     * @param title     the title component shown at the top of the screen
     */
    public FarmersWorkbenchScreen(FarmersWorkbenchMenu menu, Inventory inventory, Component title) {
        this(menu, inventory, title, createRecipeBookComponent(menu));
    }

    /**
     * Creates a FarmersWorkbenchScreen bound to the given menu, player inventory, title, and recipe book component.
     *
     * @param menu        the menu backing this screen
     * @param inventory   the player's inventory shown in the screen
     * @param title       the screen title component
     * @param recipeBook  the MineTaleRecipeBookComponent used to display and manage recipes in this screen
     */
    private FarmersWorkbenchScreen(FarmersWorkbenchMenu menu, Inventory inventory, Component title, MineTaleRecipeBookComponent recipeBook) {
        super(menu, recipeBook, inventory, title);
        this.mineTaleRecipeBook = recipeBook;
    }

    /**
     * Creates a MineTaleRecipeBookComponent configured for the farmers workbench screen.
     *
     * @param menu the workbench menu used to initialize the recipe book component
     * @return the recipe book component containing the workbench tab and the FARMERS recipe category
     */
    private static MineTaleRecipeBookComponent createRecipeBookComponent(FarmersWorkbenchMenu menu) {
        ItemStack tabIcon = new ItemStack(ModBlocks.FARMERS_WORKBENCH_BLOCK.asItem());
        
        List<RecipeBookComponent.TabInfo> tabs = List.of(
            new RecipeBookComponent.TabInfo(tabIcon.getItem(), ModRecipeDisplay.FARMERS_SEARCH)
        );

        return new MineTaleRecipeBookComponent(menu, tabs, ModRecipes.FARMERS_TYPE);
    }

    /**
     * Initialises the screen size and adds three craft buttons to the GUI.
     *
     * Configures the GUI dimensions to 176×166 and creates three buttons wired to craft requests:
     * - the first button requests 1 item;
     * - the second button requests 10 items;
     * - the third button requests all items (represented by -1).
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
         * Requests the server to craft the currently selected recipe from the integrated recipe book.
         *
         * If a previously selected recipe is remembered and has at least one result item, sends a
         * CraftRequestPayload containing the recipe's primary result and the requested amount. If no
         * remembered selection or no result exists, no request is sent.
         *
         * @param amount the quantity to craft; use -1 to request crafting the maximum available amount ("All")
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
     * Renders the screen, preserves the last selected recipe, and updates craft button states.
     *
     * Remembers the recipe book's current selection (so selection persists when the book is closed),
     * resolves the corresponding known RecipeDisplayEntry from the player's recipe book when available,
     * enables or disables the craft buttons based on whether the player can craft 1, more-than-one, or 10
     * of the selected recipe, and renders the background and tooltips.
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
     * Checks whether the given player has the required ingredients to craft the specified recipe the provided number of times.
     *
     * @param player     the player whose inventory and nearby networked items are considered; may be null
     * @param entry      the recipe entry supplying crafting requirements; may be null
     * @param craftCount the number of times to craft the recipe (multiplies each requirement)
     * @return `true` if the player has at least the required quantity of each ingredient multiplied by `craftCount`, `false` otherwise (also returns `false` if `player` or `entry` is null or the recipe has no requirements)
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
     * Determine whether the player inventory together with the workbench's networked nearby items
     * contains at least the specified quantity of items matching the given ingredient.
     *
     * @param inventory     the player's inventory to check
     * @param ingredient    the ingredient matcher used to test item stacks
     * @param totalRequired the total number of matching items required
     * @return true if the combined sources contain at least totalRequired matching items, false otherwise
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
         * Get the screen position for the recipe book toggle button.
         *
         * @return the ScreenPosition located 5 pixels from the GUI's left edge and 49 pixels above the GUI's vertical centre
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