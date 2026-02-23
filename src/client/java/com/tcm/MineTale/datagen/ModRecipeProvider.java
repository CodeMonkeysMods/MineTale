package com.tcm.MineTale.datagen;

import java.util.concurrent.CompletableFuture;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.datagen.recipes.ArmorRecipes;
import com.tcm.MineTale.datagen.recipes.BuilderRecipes;
import com.tcm.MineTale.datagen.recipes.FarmerRecipes;
import com.tcm.MineTale.datagen.recipes.FurnitureRecipes;
import com.tcm.MineTale.datagen.recipes.WorkbenchRecipes;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;

public class ModRecipeProvider extends FabricRecipeProvider {
	/**
	 * Creates a ModRecipeProvider that supplies mod-specific recipes to the data generator.
	 *
	 * @param output the FabricDataOutput used to write generated data files
	 * @param registriesFuture a future providing registry lookup access required when building recipes
	 */
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

    /**
	 * Create a RecipeProvider that registers the mod's recipe set to the provided exporter.
	 *
	 * <p>The produced provider registers these recipes:
	 * - Campfire pork cooking: cooks a porkchop into a cooked porkchop and is saved as "campfire_pork_cooking".
	 * - Workbench recipes:
	 *   - Armorers workbench (produces the armorers workbench block) saved as "workbench_armorers_workbench".
	 *   - Furnace workbench T1 (produces the furnace workbench T1 block) saved as "workbench_furnace_workbench_t1".
	 *   - Wood chest (crafts a chest from logs) saved as "workbench_wood_chest".
	 * - Furnace T1 ingot: smelts copper ore into a copper ingot and is saved as "furnace_t1_copper_ingot".
	 *
	 * @param registryLookup provider for looking up game registries and tags used when building recipes
	 * @param exporter       destination used to write the generated recipe JSON files
	 * @return               a RecipeProvider that generates and saves the described recipes to the exporter
	 */
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			/**
			 * Register the mod's recipes with the recipe exporter.
			 *
			 * <p>Registers the following recipes and their unlock conditions, categories, and export names:
			 * - Campfire: porkchop → cooked porkchop (unlock: has porkchop; category: MISC; saved as "campfire_pork_cooking").
			 * - Workbench (Armorers): 2 copper ingots, 10 logs, 5 stone tool materials → armorers workbench (unlock: has workbench; saved as "workbench_armorers_workbench").
			 * - Workbench (Furnace T1): 6 logs, 6 stone tool materials → furnace workbench T1 (unlock: has workbench; saved as "workbench_furnace_workbench_t1").
			 * - Workbench (Chests): 10 logs → chest (unlock: has logs; category: MISC; saved as "workbench_wood_chest").
			 * - Furnace T1 (Ingots): copper ore → copper ingot (time: 10; unlock: has copper ore; saved as "furnace_t1_copper_ingot").
			 */
			@Override
			public void buildRecipes() {
				// Campfire Recipes
				new WorkbenchRecipeBuilder(ModRecipes.CAMPFIRE_TYPE, ModRecipes.CAMPFIRE_SERIALIZER)
					.input(Items.PORKCHOP)
					.output(Items.COOKED_PORKCHOP)
					.time(10)
					.unlockedBy("has_porkchop", has(Items.PORKCHOP))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.CAMPFIRE_SEARCH)
					.save(exporter, "campfire_pork_cooking");

				// 2. Chests
				new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
					.input(ItemTags.LOGS, registryLookup, 10)
					.output(Items.CHEST)
					.unlockedBy("has_logs", has(ItemTags.LOGS))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
					.save(exporter, "workbench_wood_chest");

				// Furnace Recipes

				// 1. Ingots
				new WorkbenchRecipeBuilder(ModRecipes.FURNACE_T1_TYPE, ModRecipes.FURNACE_SERIALIZER)
					.input(Items.COPPER_ORE)
					.output(Items.COPPER_INGOT)
					.time(10)
					.unlockedBy("has_copper_ore", has(Items.COPPER_ORE))
					.bookCategory(ModRecipeDisplay.FURNACE_T1_SEARCH)
					.save(exporter, "furnace_t1_copper_ingot");

				// Workbench Recipes
				WorkbenchRecipes.buildRecipes(this, exporter, registryLookup);

				// Armor Recipes
				ArmorRecipes.buildRecipes(this, exporter, registryLookup);

				// Furniture Recipes
				FurnitureRecipes.buildRecipes(this, exporter, registryLookup);

				// Builder Recipes
				BuilderRecipes.buildRecipes(this, exporter, registryLookup);

				// Farmer Recipes
				FarmerRecipes.buildRecipes(this, exporter, registryLookup);
			}
		};
	}

	/**
	 * Identifier string for this recipe provider.
	 *
	 * @return the provider identifier composed of the mod ID followed by "ModRecipeProvider"
	 */
	@Override
	public String getName() {
		return MineTale.MOD_ID + "ModRecipeProvider";
	}
    
}