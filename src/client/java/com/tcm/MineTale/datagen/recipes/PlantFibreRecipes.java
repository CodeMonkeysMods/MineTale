package com.tcm.MineTale.datagen.recipes;

import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModItems;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

public class PlantFibreRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        // Use the static helper from RecipeProvider (or InventoryChangeTrigger)
        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(ModItems.PLANT_FIBER, 3)
            .output(Items.STRING)
            .unlockedBy("has_plant_fibre", provider.has(ModItems.PLANT_FIBER.asItem())) // Call the static version
            .save(exporter, "plant_fibre_to_string");
    }
}