package com.tcm.MineTale;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcm.MineTale.block.workbenches.menu.WorkbenchWorkbenchMenu;
import com.tcm.MineTale.network.CraftRequestPayload;
import com.tcm.MineTale.recipe.WorkbenchRecipe;
import com.tcm.MineTale.registry.ModBlockEntities;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModEntities;
import com.tcm.MineTale.registry.ModEntityDataSerializers;
import com.tcm.MineTale.registry.ModItems;
import com.tcm.MineTale.registry.ModMenuTypes;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import static com.tcm.MineTale.item.ModCreativeTab.MINETALE_CREATIVE_TAB;
import static com.tcm.MineTale.item.ModCreativeTab.MINETALE_CREATIVE_TAB_KEY;

import java.util.List;
import java.util.Optional;

public class MineTale implements ModInitializer {
	public static final String MOD_ID = "minetale";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * Initializes and registers the mod's game content and subsystems during Fabric startup.
	 *
	 * <p>Triggers initialization for blocks, block entities, menu types, entities, items, and entity
	 * data serializers so they are registered with the game before gameplay begins.</p>
	 */
	@Override
	public void onInitialize() {
		// 1. Blocks first - They are the foundation
		ModBlocks.initialize();

		// 2. Items second - Many blocks have associated BlockItems
		ModItems.initialize();

		// 3. Block Entities third - They now have non-null Blocks to reference
		ModBlockEntities.initialize();

		// 4. Entities & Menus - These depend on the objects above
		ModEntities.initialize();
		ModMenuTypes.initialize();

		// 5. Recipes last - These depend on Items, Blocks, and Entities existing
		ModRecipes.initialize();

		// ADD THIS HERE
        ModRecipeDisplay.initialize();

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MINETALE_CREATIVE_TAB_KEY, MINETALE_CREATIVE_TAB);

		ModEntityDataSerializers.initialize();

		RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.FURNACE_SERIALIZER);

		// Register the payload type and codec so the game knows how to handle it
		PayloadTypeRegistry.playC2S().register(CraftRequestPayload.TYPE, CraftRequestPayload.CODEC);

		// Register the server-side receiver
		// ServerPlayNetworking.registerGlobalReceiver(CraftRequestPayload.TYPE, (payload, context) -> {
		// 	context.server().execute(() -> {
		// 		// Your crafting logic here
		// 		System.out.println("Received craft request for: " + payload.resultItem() + " amount: " + payload.amount());
		// 	});
		// });

		// ServerPlayNetworking.registerGlobalReceiver(CraftRequestPayload.TYPE, (payload, context) -> {
		// 	context.server().execute(() -> {
		// 		ServerPlayer player = context.player();
		// 		ItemStack requestedResult = payload.stack();
		// 		int amount = payload.amount();

		// 		// 1. Find the recipe on the server
		// 		Optional<RecipeHolder<WorkbenchRecipe>> recipeOpt = player.level().getRecipeManager()
		// 			.getAllRecipesFor(ModRecipes.WORKBENCH_TYPE)
		// 			.stream()
		// 			.filter(r -> ItemStack.isSameItem(r.value().results().get(0), requestedResult))
		// 			.findFirst();

		// 		if (recipeOpt.isPresent()) {
		// 			WorkbenchRecipe recipe = recipeOpt.get().value();
					
		// 			// 2. Logic for "1", "30", or "All"
		// 			// For now, let's just handle "1" to test
		// 			int limit = (amount == -1) ? 64 : amount; 

		// 			for (int i = 0; i < limit; i++) {
		// 				if (hasIngredients(player, recipe)) {
		// 					consumeIngredients(player, recipe);
		// 					// Give the player the result
		// 					player.getInventory().add(recipe.results().get(0).copy());
		// 				} else {
		// 					break; 
		// 				}
		// 			}
					
		// 			// 3. VERY IMPORTANT: Sync the inventory so the player sees the items change
		// 			player.containerMenu.broadcastChanges();
		// 		}
		// 	});
		// });

		// Register the server-side receiver using .TYPE
		ServerPlayNetworking.registerGlobalReceiver(CraftRequestPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				ServerPlayer player = context.player();

				// --- SECURITY GUARD ---
				// Ensure the player actually has the Workbench UI open before processing the craft
				if (!(player.containerMenu instanceof WorkbenchWorkbenchMenu)) {
					return; 
				}

				ItemStack requestedResult = payload.resultItem();
				int amount = payload.amount();
				
				// 1. Get the RecipeManager from the server level
				RecipeManager recipeManager = player.level().recipeAccess();

				// 2. Find the recipe by matching the output ItemStack
				Optional<RecipeHolder<WorkbenchRecipe>> recipeOpt = recipeManager.getAllOfType(ModRecipes.WORKBENCH_TYPE).stream()
					.filter(holder -> {
						// Guard against recipes with no results before accessing index 0
						if (holder.value().results().isEmpty()) {
							return false;
						}
						
						// Compare the first result of the workbench recipe to the requested item
						ItemStack result = holder.value().results().get(0);
						return ItemStack.isSameItem(result, requestedResult);
					})
					.findFirst();

				if (recipeOpt.isPresent()) {
					WorkbenchRecipe recipe = recipeOpt.get().value();
					
					// 2. Determine craft limit (Handle "All" logic)
					int limit = (amount == -1) ? 64 : Math.min(Math.max(amount, 0), 64);; 

					for (int i = 0; i < limit; i++) {
						if (hasIngredients(player, recipe)) {
							consumeIngredients(player, recipe);
							player.getInventory().add(recipe.results().get(0).copy());
						} else {
							break; 
						}
					}
					
					// 3. Sync inventory changes to the client screen
					player.containerMenu.broadcastChanges();
				}
			});
		});

		LOGGER.info("Hello Fabric world!");
	}

	/**
     * Checks whether the player's inventory contains the necessary ingredients to craft the given recipe without modifying the real inventory.
     *
     * @param player the server player whose inventory will be simulated
     * @param recipe the workbench recipe to validate against the player's inventory
     * @return `true` if all required ingredients can be satisfied from the player's current inventory, `false` otherwise
     */
    private boolean hasIngredients(ServerPlayer player, WorkbenchRecipe recipe) {
        // We simulate the craft using a copy of the inventory
        List<ItemStack> tempInv = new java.util.ArrayList<>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            tempInv.add(player.getInventory().getItem(i).copy());
        }

        for (Ingredient ingredient : recipe.ingredients()) {
            boolean found = false;
            for (ItemStack stack : tempInv) {
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(1);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    /**
     * Consumes one matching item from the player's inventory for each ingredient in the given workbench recipe.
     *
     * @param player the player whose inventory will be modified
     * @param recipe the workbench recipe whose ingredients should be consumed
     */
    private void consumeIngredients(ServerPlayer player, WorkbenchRecipe recipe) {
        for (Ingredient ingredient : recipe.ingredients()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(1);
                    break; 
                }
            }
        }
    }
}