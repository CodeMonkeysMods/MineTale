package com.tcm.MineTale.block.workbenches.screen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.menu.AbstractWorkbenchContainerMenu;
import com.tcm.MineTale.block.workbenches.menu.BuildersWorkbenchMenu;
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

public class BuildersWorkbenchScreen extends AbstractRecipeBookScreen<BuildersWorkbenchMenu> {
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
    public BuildersWorkbenchScreen(BuildersWorkbenchMenu menu, Inventory inventory, Component title) {
        this(menu, inventory, title, createRecipeBookComponent(menu));
    }

    /**
     * Creates a BuildersWorkbenchScreen bound to the given menu, player inventory, title, and recipe book component.
     *
     * @param menu        the menu backing this screen
     * @param inventory   the player's inventory shown in the screen
     * @param title       the screen title component
     * @param recipeBook  the MineTaleRecipeBookComponent used to display and manage recipes in this screen
     */
    private BuildersWorkbenchScreen(BuildersWorkbenchMenu menu, Inventory inventory, Component title, MineTaleRecipeBookComponent recipeBook) {
        super(menu, recipeBook, inventory, title);
        this.mineTaleRecipeBook = recipeBook;
    }

    /**
     * Create a MineTaleRecipeBookComponent configured for the workbench screen.
     *
     * @param menu the workbench menu used to initialize the recipe book component
     * @return a MineTaleRecipeBookComponent containing the workbench tab and associated recipe category
     */
    private static MineTaleRecipeBookComponent createRecipeBookComponent(BuildersWorkbenchMenu menu) {
        ItemStack tabIcon = new ItemStack(ModBlocks.BUILDERS_WORKBENCH_BLOCK.asItem());
        
        List<RecipeBookComponent.TabInfo> tabs = List.of(
            new RecipeBookComponent.TabInfo(tabIcon.getItem(), ModRecipeDisplay.BUILDERS_SEARCH)
        );

        return new MineTaleRecipeBookComponent(menu, tabs, ModRecipes.BUILDERS_TYPE);
    }

    /**
         * Configure the screen's GUI dimensions and initialize widgets.
         *
         * Sets the layout size (imageWidth = 176, imageHeight = 166), delegates remaining
         * layout initialization to the superclass, and creates the three craft buttons
         * ("1", "10", "All") wired to their respective handlers.
         */
    @Override
    protected void init() {
        // Important: Set your GUI size before super.init()
        this.imageWidth = 176;
        this.imageHeight = 166;
        
        super.init();

        int defaultLeft = this.leftPos + 90;
        int defaultTop = this.topPos + 25;

        this.craftOneBtn = addRenderableWidget(Button.builder(Component.literal("Craft"), (button) -> {
            handleCraftRequest(1);
        }).bounds(defaultLeft, defaultTop, 75, 20).build());

        this.craftTenBtn = addRenderableWidget(Button.builder(Component.literal("x10"), (button) -> {
            handleCraftRequest(10);
        }).bounds(defaultLeft, defaultTop + 22, 35, 20).build());

        this.craftAllBtn = addRenderableWidget(Button.builder(Component.literal("All"), (button) -> {
            handleCraftRequest(-1); // -1 represents "All" logic
        }).bounds(defaultLeft + 40, defaultTop + 22, 35, 20).build());
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
    * Render the workbench background texture at the screen's top-left corner.
    *
    * @param guiGraphics the graphics context used to draw GUI elements
    * @param f           partial tick time used for animation interpolation
    * @param i           current mouse x coordinate relative to the window
    * @param j           current mouse y coordinate relative to the window
    */
   protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
      int k = this.leftPos;
      int l = this.topPos;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
   }

        /**
     * Renders the builders workbench screen, updates recipe selection state, updates craft button availability
     * and, when a recipe is selected, renders its ingredient list and hover tooltips.
     *
     * The method:
     * - draws the background and base screen,
     * - updates {@code lastKnownSelectedId} from the recipe book component,
     * - looks up the corresponding {@code RecipeDisplayEntry} from the client's known recipes,
     * - enables or disables the craft buttons according to available ingredients for 1, 2 and 10 crafts,
     * - renders the aggregated ingredient list for the selected recipe (including hover tooltips) when present,
     * - renders the standard screen tooltip layer.
     *
     * @param graphics the GUI graphics context used for all rendering operations
     * @param mouseX   current mouse X coordinate (screen space)
     * @param mouseY   current mouse Y coordinate (screen space)
     * @param delta    frame partial tick time used for animated displays
     */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);

        RecipeDisplayId currentId = this.mineTaleRecipeBook.getSelectedRecipeId();
        if (currentId != null) {
            this.lastKnownSelectedId = currentId;
        }

        RecipeDisplayEntry selectedEntry = null;
        if (this.lastKnownSelectedId != null && this.minecraft.level != null) {
            ClientRecipeBook book = this.minecraft.player.getRecipeBook();
            selectedEntry = ((ClientRecipeBookAccessor) book).getKnown().get(this.lastKnownSelectedId);
        }

        if (selectedEntry != null) {
            // Existing Button Logic
            boolean canCraftOne = canCraft(this.minecraft.player, selectedEntry, 1);
            boolean canCraftMoreThanOne = canCraft(this.minecraft.player, selectedEntry, 2);
            boolean canCraftTen = canCraft(this.minecraft.player, selectedEntry, 10);

            this.craftOneBtn.active = canCraftOne;
            this.craftTenBtn.active = canCraftTen;
            this.craftAllBtn.active = canCraftMoreThanOne;

            // NEW: Render the Ingredients List
            renderIngredientList(graphics, selectedEntry, mouseX, mouseY);
        } else {
            this.craftOneBtn.active = false;
            this.craftTenBtn.active = false;
            this.craftAllBtn.active = false;
        }

        renderTooltip(graphics, mouseX, mouseY);
    }

    /**
     * Render the aggregated ingredient list for a recipe, showing icons, availability and tooltips.
     *
     * <p>For the given recipe this draws one row per unique ingredient set (duplicates aggregated),
     * cycles through item variants for the icon, renders an availability string "available/needed"
     * (red when insufficient, white when sufficient) and shows an item tooltip when the mouse
     * hovers over the icon.
     *
     * @param graphics the GUI graphics context used for rendering
     * @param entry the recipe entry whose crafting requirements will be displayed
     * @param mouseX the current mouse X coordinate (used to detect tooltip hover)
     * @param mouseY the current mouse Y coordinate (used to detect tooltip hover)
     */
    private void renderIngredientList(GuiGraphics graphics, RecipeDisplayEntry entry, int mouseX, int mouseY) {
        Optional<List<Ingredient>> reqs = entry.craftingRequirements();
        if (reqs.isEmpty()) return;

        // Group requirements to avoid duplicate rows for the same item type
        Map<List<Holder<Item>>, Integer> aggregated = new HashMap<>();
        Map<List<Holder<Item>>, Ingredient> holderToIng = new HashMap<>();

        for (Ingredient ing : reqs.get()) {
            List<Holder<Item>> key = ing.items().toList();
            aggregated.put(key, aggregated.getOrDefault(key, 0) + 1);
            holderToIng.putIfAbsent(key, ing);
        }

        int startX = this.leftPos + 8; // Adjust to fit your texture's empty space
        int startY = this.topPos + 20;
        int rowHeight = 20;
        int index = 0;

        for (Map.Entry<List<Holder<Item>>, Integer> reqEntry : aggregated.entrySet()) {
            Ingredient ing = holderToIng.get(reqEntry.getKey());
            int amountNeeded = reqEntry.getValue();
            int currentY = startY + (index * rowHeight);

            // Calculate total available (Inv + Nearby)
            int available = getAvailableCount(ing);

            // Draw Item Icon
            ItemStack[] variants = ing.items().toArray(ItemStack[]::new);
            if (variants.length > 0) {
                // Cycle through variants every second
                ItemStack displayStack = variants[(int) (System.currentTimeMillis() / 1000 % variants.length)];
                graphics.renderFakeItem(displayStack, startX, currentY);

                // Draw Text (Red if lacking, White if okay)
                int color = (available < amountNeeded) ? 0xFF5555 : 0xFFFFFF;
                String progress = available + "/" + amountNeeded;
                graphics.drawString(this.font, progress, startX + 20, currentY + 4, color, true);

                // Tooltip logic
                if (mouseX >= startX && mouseX < startX + 16 && mouseY >= currentY && mouseY < currentY + 16) {
                    graphics.renderItemTooltip(this.font, displayStack, mouseX, mouseY);
                }
            }
            index++;
        }
    }

    /**
     * Count the total quantity of items that satisfy the given ingredient from the player inventory and nearby networked storage.
     *
     * @param ingredient the ingredient used to test ItemStacks
     * @return the sum of counts of all ItemStacks matching `ingredient` from the player inventory and any networked nearby items
     */
    private int getAvailableCount(Ingredient ingredient) {
        int found = 0;
        // Check Player Inventory
        // Use getContainerSize() and getItem(i) for safe access
        Inventory inv = this.minecraft.player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (ingredient.test(stack)) {
              found += stack.getCount();
            }
         }
        
        // Check Networked Nearby Items
        if (this.menu instanceof AbstractWorkbenchContainerMenu workbenchMenu) {
            for (ItemStack stack : workbenchMenu.getNetworkedNearbyItems()) {
                if (ingredient.test(stack)) found += stack.getCount();
            }
        }
        return found;
    }

    /**
     * Determines whether the player has enough ingredients to craft the given recipe the specified number of times.
     *
     * @param player     the player whose inventory (and networked nearby items) will be checked; may be null
     * @param entry      the recipe display entry providing crafting requirements; may be null
     * @param craftCount the multiplier for required ingredient quantities (e.g., 1, 10, or -1 is not specially handled here)
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
     * Compute the on-screen position for the recipe-book toggle button for this GUI.
     *
     * @return the screen position located 5 pixels from the GUI's left edge and 49 pixels above the GUI's vertical centre
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
