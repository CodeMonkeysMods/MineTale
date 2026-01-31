package com.tcm.MineTale;

import net.fabricmc.api.ModInitializer;

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
import com.tcm.MineTale.registry.ModRecipes;

import static com.tcm.MineTale.item.ModCreativeTab.MINETALE_CREATIVE_TAB;
import static com.tcm.MineTale.item.ModCreativeTab.MINETALE_CREATIVE_TAB_KEY;

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
		ModBlocks.initialize();
		ModRecipes.initialize();
		ModBlockEntities.initialize();
		ModMenuTypes.initialize();
		ModEntities.initialize();
		ModItems.initialize();

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MINETALE_CREATIVE_TAB_KEY, MINETALE_CREATIVE_TAB);

		ModEntityDataSerializers.initialize();

		LOGGER.info("Hello Fabric world!");
	}
}