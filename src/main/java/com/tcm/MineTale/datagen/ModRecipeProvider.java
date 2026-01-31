package com.tcm.MineTale.datagen;

import java.util.concurrent.CompletableFuture;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModRecipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

    @Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

				new WorkbenchRecipeBuilder(ModRecipes.CAMPFIRE_TYPE, ModRecipes.CAMPFIRE_SERIALIZER)
					.input(Ingredient.of(Items.PORKCHOP))
					// Note: Slot 1 is optional in our logic, so we just don't add a second input
					.output(new ItemStack(Items.COOKED_PORKCHOP))
					.time(10) // Campfires usually take longer (30 seconds)
					.unlockedBy("has_porkchop", has(Items.PORKCHOP))
					.save(exporter, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "campfire_pork_cooking"));
			}
		};
	}

	@Override
	public String getName() {
		return MineTale.MOD_ID + "ModRecipeProvider";
	}
    
}
