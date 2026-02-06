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
import net.minecraft.resources.Identifier;
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
	 * Creates a RecipeProvider that registers a campfire cooking recipe for porkchop.
	 *
	 * The produced provider builds a single recipe that cooks a porkchop into a cooked porkchop,
	 * requires 10 time units, is unlocked when the player has a porkchop, and is saved under
	 * the mod namespace with path "campfire_pork_cooking".
	 *
	 * @param registryLookup provider for looking up game registries used when building recipes
	 * @param exporter       destination used to write the generated recipe JSON
	 * @return               a RecipeProvider that produces the described campfire cooking recipe
	 */
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				new WorkbenchRecipeBuilder(ModRecipes.CAMPFIRE_TYPE, ModRecipes.CAMPFIRE_SERIALIZER)
					.input(Ingredient.of(Items.PORKCHOP))
					.output(new ItemStack(Items.COOKED_PORKCHOP))
					.time(10)
					.unlockedBy("has_porkchop", has(Items.PORKCHOP))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.CAMPFIRE_SEARCH)
					.save(exporter, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "campfire_pork_cooking"));

				new WorkbenchRecipeBuilder(ModRecipes.FURNACE_T1_TYPE, ModRecipes.FURNACE_SERIALIZER)
					.input(Ingredient.of(Items.PORKCHOP))
					.output(new ItemStack(Items.ACACIA_BOAT))
					.time(10)
					.unlockedBy("has_porkchop", has(Items.PORKCHOP))
					.category(CraftingBookCategory.MISC)
					.bookCategory(ModRecipeDisplay.FURNACE_T1_SEARCH)
					.save(exporter, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "furnace_pork_cooking"));
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