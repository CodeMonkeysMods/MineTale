package com.tcm.MineTale.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class BuilderRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {

        // TODO: BUILDERS_WORKBENCH_BLOCK & ROPE Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.BUILDER_TYPE, ModRecipes.BUILDER_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER)
        //     .output(ModBlocks.ROPE.asItem())
        //     .time(3)
        //     .unlockedBy("has_builders_workbench", provider.has(ModBlocks.BUILDERS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BUILDER_SEARCH)
        //     .save(exporter, "builders_workbench_rope");

        // TODO: BUILDERS_WORKBENCH_BLOCK & ROPE_DIAGONAL Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.BUILDER_TYPE, ModRecipes.BUILDER_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER)
        //     .output(ModBlocks.ROPE_DIAGONAL.asItem())
        //     .time(3)
        //     .unlockedBy("has_builders_workbench", provider.has(ModBlocks.BUILDERS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BUILDER_SEARCH)
        //     .save(exporter, "builders_workbench_rope_diagonal");
    }
}
