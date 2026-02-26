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
    /**
     * Register workbench recipes for the builders mod used by the data generator.
     *
     * Creates and saves two recipes (rope and diagonal rope), each requiring plant fibre,
     * taking 3 ticks, unlocked when the player has the builders workbench, and assigned
     * to the builders recipe book category.
     *
     * @param provider the recipe provider used to form unlock conditions
     * @param exporter the recipe output to which generated recipes are written
     * @param lookup   registry holder lookup provider used for resolving required holders
     */
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
