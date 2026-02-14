package com.tcm.MineTale;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class MineTale implements ModInitializer {
	public static final String MOD_ID = "minetale";
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
		// This helps the search bar "see" items in your custom categories

		LOGGER.info("Hello Fabric world!");
	}
}