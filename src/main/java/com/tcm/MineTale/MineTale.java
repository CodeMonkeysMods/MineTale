package com.tcm.MineTale;

import com.tcm.MineTale.util.ModLootTableModifiers;
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

import com.tcm.MineTale.block.workbenches.entity.AbstractWorkbenchEntity;
import com.tcm.MineTale.block.workbenches.menu.AbstractWorkbenchContainerMenu;
import com.tcm.MineTale.network.ClientboundNearbyInventorySyncPacket;
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

import java.util.Optional;

public class MineTale implements ModInitializer {
	public static final String MOD_ID = "minetale";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * Initialise the mod: register game content, networking codecs and runtime subsystems.
	 *
	 * Performs startup registration in dependency order (blocks, items, block entities, entities,
	 * menus, recipes and recipe displays), registers the creative tab and entity data serializers,
	 * synchronises the furnace recipe serializer, applies loot-table modifiers, registers client↔server
	 * payload codecs and a global server receiver that processes craft requests from workbench-like menus.
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

		ModLootTableModifiers.modifyLootTables();

		// Register the payload type and codec so the game knows how to handle it
		PayloadTypeRegistry.playC2S().register(CraftRequestPayload.TYPE, CraftRequestPayload.CODEC);

		PayloadTypeRegistry.playS2C().register(ClientboundNearbyInventorySyncPacket.TYPE, ClientboundNearbyInventorySyncPacket.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(CraftRequestPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				ServerPlayer player = context.player();
				
				// --- SELECTIVE SECURITY GUARD ---
				// Only proceed if the menu is one of the two specific workbenches
				boolean isWorkbench = player.containerMenu instanceof AbstractWorkbenchContainerMenu;

				if (!isWorkbench) {
					return; // Reject packets from Campfires, Furnaces, or other menus
				}

				AbstractWorkbenchContainerMenu instanceContainerMenu = (AbstractWorkbenchContainerMenu) player.containerMenu;

				ItemStack requestedResult = payload.resultItem();
				int amount = payload.amount();
				RecipeManager recipeManager = player.level().recipeAccess();

				// 2. Find the recipe within that specific type
				Optional<RecipeHolder<WorkbenchRecipe>> recipeOpt = recipeManager.getAllOfType(instanceContainerMenu.getRecipeType()).stream()
					.filter(holder -> {
						if (holder.value().results().isEmpty()) return false;
						
						ItemStack result = holder.value().results().get(0);
						return ItemStack.isSameItem(result, requestedResult);
					})
					.findFirst();

				if (recipeOpt.isPresent()) {
					WorkbenchRecipe recipe = recipeOpt.get().value();
					
					// 3. Logic for crafting amount
					int limit = (amount == -1) ? 64 : Math.min(Math.max(amount, 0), 64);

					for (int i = 0; i < limit; i++) {
						// IMPORTANT: Ensure these methods check nearby items if your benches use them!
						if (hasIngredients(player, recipe)) {
							consumeIngredients(player, recipe);
							
							// Give the item to the player
							ItemStack output = recipe.results().get(0).copy();
							if (!player.getInventory().add(output)) {
								player.drop(output, false); // Drop on floor if inventory is full
							}
						} else {
							break; 
						}
					}
					
					player.containerMenu.broadcastChanges();
				}
			});
		});

		LOGGER.info("MineTale Loaded!");
	}

	/**
     * Determines whether the player (and nearby pullable chests, if the workbench allows) collectively contain all item ingredients required by the given workbench recipe.
     *
     * This check requires the player's open container to be an AbstractWorkbenchContainerMenu; if it is not, the method returns `false`. It examines the player's non-equipment inventory and, when the workbench permits pulling, the contents of nearby inventories. Each recipe ingredient must be satisfied by a distinct matching item instance from those inventories.
     *
     * @param player the server player whose inventories are checked
     * @param recipe the workbench recipe whose ingredient requirements are being validated
     * @return `true` if all ingredients of the recipe can be satisfied from the player and allowed nearby inventories, `false` otherwise
     */
    private boolean hasIngredients(ServerPlayer player, WorkbenchRecipe recipe) {
        if (!(player.containerMenu instanceof AbstractWorkbenchContainerMenu menu)) return false;
        AbstractWorkbenchEntity be = menu.getBlockEntity();

        // 1. Create a "Mental Map" of what we found in Player + Chests
        // We use a Map to track how many of each item we have available to "spend"
        java.util.Map<net.minecraft.world.item.Item, Integer> available = new java.util.HashMap<>();
        
        // Add Player Inventory
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (!stack.isEmpty()) available.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }
        
        // Add Nearby Chests
        if (be != null && be.isCanPullFromNearby()) {
            for (net.minecraft.world.Container chest : be.getNearbyInventories()) {
                for (int i = 0; i < chest.getContainerSize(); i++) {
                    ItemStack stack = chest.getItem(i);
                    if (!stack.isEmpty()) available.merge(stack.getItem(), stack.getCount(), Integer::sum);
                }
            }
        }

        // 2. Try to "spend" each ingredient from the JSON list
        for (Ingredient ingredient : recipe.ingredients()) {
            boolean matched = false;
            for (net.minecraft.world.item.Item item : available.keySet()) {
                if (ingredient.test(item.getDefaultInstance())) {
                    int count = available.get(item);
                    if (count > 0) {
                        available.put(item, count - 1);
                        matched = true;
                        break;
                    }
                }
            }
            if (!matched) return false; // Ran out of a specific log or stick!
        }
        return true;
    }

	private void consumeIngredients(ServerPlayer player, WorkbenchRecipe recipe) {
		if (!(player.containerMenu instanceof AbstractWorkbenchContainerMenu menu)) return;
		AbstractWorkbenchEntity be = menu.getBlockEntity();

		for (Ingredient ingredient : recipe.ingredients()) {
			boolean consumed = false;

			// 1. Try Player first
			for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
				if (!stack.isEmpty() && ingredient.test(stack)) {
					stack.shrink(1);
					consumed = true;
					break;
				}
			}

			// 2. Try Chests second
			if (!consumed && be != null && be.isCanPullFromNearby()) {
				for (net.minecraft.world.Container chest : be.getNearbyInventories()) {
					for (int i = 0; i < chest.getContainerSize(); i++) {
						ItemStack stack = chest.getItem(i);
						if (!stack.isEmpty() && ingredient.test(stack)) {
							stack.shrink(1);
							chest.setChanged();
							consumed = true;
							break;
						}
					}
					if (consumed) break;
				}
			}
		}
	}
}