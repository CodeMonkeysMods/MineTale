package com.tcm.MineTale.block.workbenches.screen;

import com.tcm.MineTale.block.workbenches.menu.AbstractWorkbenchContainerMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class ModAbstractContainerScreen<T extends AbstractWorkbenchContainerMenu> extends AbstractRecipeBookScreen<T> {

    public ModAbstractContainerScreen(T recipeBookMenu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(recipeBookMenu, recipeBookComponent, inventory, component);
    }

    protected void renderIngredientList(GuiGraphics graphics, RecipeDisplayEntry entry, int mouseX, int mouseY) {
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
            // Inside your loop
            ItemStack[] variants = ing.items().map(ItemStack::new).toArray(ItemStack[]::new);
            if (variants.length > 0) {
                long time = System.currentTimeMillis() / 1000;
                ItemStack displayStack = variants[(int) (time % variants.length)];

                graphics.renderFakeItem(displayStack, startX, currentY);

                int color = (available < amountNeeded) ? 0xFFFF5555 : 0xFFFFFFFF; // Added alpha channel
                String progress = available + "/" + amountNeeded;
                graphics.drawString(this.font, progress, startX + 22, currentY + 4, color);

                if (mouseX >= startX && mouseX <= startX + 16 && mouseY >= currentY && mouseY <= currentY + 16) {
                    graphics.setTooltipForNextFrame(this.font, displayStack, mouseX, mouseY);
                }
            }
            index++;
        }
    }

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
}