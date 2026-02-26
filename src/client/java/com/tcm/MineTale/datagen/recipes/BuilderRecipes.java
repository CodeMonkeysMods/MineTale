package com.tcm.MineTale.datagen.recipes;

import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModItems;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class BuilderRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {

        new WorkbenchRecipeBuilder(ModRecipes.BUILDERS_TYPE, ModRecipes.BUILDERS_SERIALIZER)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.ROPE)
            .time(3)
            .unlockedBy("has_builders_workbench", provider.has(ModBlocks.BUILDERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.BUILDERS_SEARCH)
            .save(exporter, "builders_workbench_rope");

        new WorkbenchRecipeBuilder(ModRecipes.BUILDERS_TYPE, ModRecipes.BUILDERS_SERIALIZER)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.ROPE_DIAGONAL)
            .time(3)
            .unlockedBy("has_builders_workbench", provider.has(ModBlocks.BUILDERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.BUILDERS_SEARCH)
            .save(exporter, "builders_workbench_rope_diagonal");
    }
}
