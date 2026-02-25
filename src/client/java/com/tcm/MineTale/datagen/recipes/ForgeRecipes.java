package com.tcm.MineTale.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class ForgeRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        // new WorkbenchRecipeBuilder(ModRecipes.FORGE_TYPE, ModRecipes.FORGE_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 4)
        //     .input(ItemTags.LOGS, lookup, 6)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .output(Items.COPPER_CLUB)
        //     .time(3)
        //     .unlockedBy("has_forge_workbench", provider.has(ModBlocks.FORGE_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.FORGE_SEARCH)
        //     .save(exporter, "COPPER_CLUB");
    }
}
