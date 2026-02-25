package com.tcm.MineTale.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class AlchemistRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        // new WorkbenchRecipeBuilder(ModRecipes.ALCHEMIST_TYPE, ModRecipes.ALCHEMIST_SERIALIZER)
        //     .input(ModItems.EMPTY_POTION_BOTTLE)
        //     .input(ModItems.PLANT_FIBER, 5)
        //     .input(ModItems.ESSENCE_OF_LIFE, 2)
        //     .input(ModItems.VENOM_SACK, 1)
        //     .output(ModItems.ANTIDOTE)
        //     .time(1)
        //     .unlockedBy("has_alchemist_workbench", provider.has(ModBlocks.ALCHEMIST_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.ALCHEMIST_SEARCH)
        //     .save(exporter, "ANTIDOTE");

        // new WorkbenchRecipeBuilder(ModRecipes.ALCHEMIST_TYPE, ModRecipes.ALCHEMIST_SERIALIZER)
        //     .input(ModItems.WILD_BERRY, 6)
        //     .input(ModItems.BOOM_POWDER, 2)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .output(ModItems.POPBERRY_BOMB, 2)
        //     .time(0.5)
        //     .unlockedBy("has_alchemist_workbench", provider.has(ModBlocks.ALCHEMIST_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.ALCHEMIST_SEARCH)
        //     .save(exporter, "POPBERRY_BOMB");
    }
}
