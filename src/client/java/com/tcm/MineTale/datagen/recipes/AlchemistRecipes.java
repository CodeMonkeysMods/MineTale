package com.tcm.MineTale.datagen.recipes;

import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModItems;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class AlchemistRecipes {
    /**
     * Registers alchemist workbench recipes and writes them to the provided exporter.
     *
     * Uses the provider to determine unlock conditions and the lookup to resolve holders required
     * during recipe generation.
     *
     * @param provider the RecipeProvider used to query registered items/blocks for unlock conditions
     * @param exporter the RecipeOutput target to which generated recipes will be saved
     * @param lookup   the HolderLookup.Provider used to resolve registry holders during generation
     */
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
         new WorkbenchRecipeBuilder(ModRecipes.ALCHEMISTS_TYPE, ModRecipes.ALCHEMISTS_SERIALIZER)
             .input(ModItems.EMPTY_POTION_BOTTLE)
             .input(ModItems.PLANT_FIBER, 5)
             .input(ModItems.ESSENCE_OF_LIFE, 2)
             .input(ModItems.VENOM_SAC, 1)
             .output(ModItems.ANTIDOTE)
             .time(1)
             .unlockedBy("has_alchemists_workbench", provider.has(ModBlocks.ALCHEMISTS_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.ALCHEMISTS_SEARCH)
             .save(exporter, "antidote");

         new WorkbenchRecipeBuilder(ModRecipes.ALCHEMISTS_TYPE, ModRecipes.ALCHEMISTS_SERIALIZER)
             .input(ModItems.WILD_BERRY, 6)
             .input(ModItems.BOOM_POWDER, 2)
             .input(ModItems.PLANT_FIBER, 4)
             .output(ModItems.POPBERRY_BOMB)
             .time(0.5f)
             .unlockedBy("has_alchemists_workbench", provider.has(ModBlocks.ALCHEMISTS_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.ALCHEMISTS_SEARCH)
             .save(exporter, "popberry_bomb");
    }
}
