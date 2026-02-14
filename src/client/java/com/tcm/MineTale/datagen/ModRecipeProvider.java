package com.tcm.MineTale.datagen;

import java.util.concurrent.CompletableFuture;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

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
	 * Creates a RecipeProvider that registers the mod's recipe set: a campfire cooking recipe
	 * that cooks a porkchop into a cooked porkchop, a furnace cooking recipe that converts a
	 * porkchop into an acacia boat, and a workbench crafting recipe that assembles a chest from
	 * logs and sticks.
	 *
	 * Each recipe includes its unlock condition, crafting category, book category, processing time,
	 * and is saved to the provided exporter under the mod-specific paths:
	 * "campfire_pork_cooking", "furnace_pork_cooking", and "workbench_wood_chest".
	 *
	 * @param registryLookup provider for looking up game registries and tags used when building recipes
	 * @param exporter       destination used to write the generated recipe JSON files
	 * @return               a RecipeProvider that produces and saves the described recipes to the exporter
	 */
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			/**
			 * Registers three mod-specific recipes with the recipe exporter:
			 * a campfire pork cooking recipe producing cooked porkchop (unlocked by having a porkchop, saved as "campfire_pork_cooking"), a furnace pork cooking recipe producing an acacia boat (unlocked by having a porkchop, saved as "furnace_pork_cooking"), and a workbench recipe that crafts a chest from 5 logs and 10 sticks (unlocked by having logs, saved as "workbench_wood_chest").
			 */
			@Override
			public void buildRecipes() {
				new WorkbenchRecipeBuilder(ModRecipes.CAMPFIRE_TYPE, ModRecipes.CAMPFIRE_SERIALIZER)
					.input(Ingredient.of(Items.PORKCHOP))
					.output(new ItemStack(Items.COOKED_PORKCHOP))
					.time(10)
					.unlockedBy("has_porkchop", has(Items.PORKCHOP))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.CAMPFIRE_SEARCH)
					.save(exporter, "campfire_pork_cooking");

				new WorkbenchRecipeBuilder(ModRecipes.FURNACE_T1_TYPE, ModRecipes.FURNACE_SERIALIZER)
					.input(Ingredient.of(Items.PORKCHOP))
					.output(new ItemStack(Items.ACACIA_BOAT))
					.time(10)
					.unlockedBy("has_porkchop", has(Items.PORKCHOP))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.FURNACE_T1_SEARCH)
					.save(exporter, "furnace_pork_cooking");

				new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
					.input(ItemTags.LOGS, registryLookup, 5)
					.input(Items.STICK, 10)
					.output(new ItemStack(Items.CHEST)) 
					.time(50)
					.unlockedBy("has_logs", has(ItemTags.LOGS))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
					.save(exporter, "workbench_wood_chest");
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