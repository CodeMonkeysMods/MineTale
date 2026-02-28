package com.tcm.MineTale.datagen.recipes;

import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModItems;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class BlacksmithRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
             .input(Items.COPPER_INGOT, 4)
             .input(ItemTags.LOGS, lookup, 2)
             .input(ModItems.PLANT_FIBER, 4)
             .output(Items.COPPER_AXE)
             .time(3)
             .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
             .save(exporter, "copper_axe");

        //new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 6)
        //     .input(ItemTags.LOGS, lookup, 6)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .output(ModItems.COPPER_BATTLEAXE)
        //     .time(3)
        //     .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
        //     .save(exporter, "copper_battleaxe");

        // new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 4)
        //     .input(ItemTags.LOGS, lookup, 3)
        //     .input(ModItems.PLANT_FIBER, 3)
        //     .output(ModItems.COPPER_DAGGERS)
        //     .time(3)
        //     .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
        //     .save(exporter, "copper_daggers");

        // new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
        //     .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 6)
        //     .input(ItemTags.LOGS, lookup, 2)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .output(ModItems.CRUDE_LONGSWORD)
        //     .time(3)
        //     .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
        //     .save(exporter, "crude_longsword");

        // new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 6)
        //     .input(ItemTags.LOGS, lookup, 2)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .output(ModItems.COPPER_LONGSWORD)
        //     .time(3)
        //     .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
        //     .save(exporter, "copper_longsword");

        new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
             .input(Items.COPPER_INGOT, 6)
             .input(ItemTags.LOGS, lookup, 10)
             .input(ModItems.PLANT_FIBER, 2)
             .output(ModItems.COPPER_MACE)
             .time(3)
             .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
             .save(exporter, "copper_mace");

        // new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
        //     .input(Items.COPPER_INGOT, 4)
        //     .input(ItemTags.LOGS, lookup, 4)
        //     .input(ModItems.PLANT_FIBER, 6)
        //     .output(ModItems.COPPER_SHORTBOW)
        //     .time(3)
        //     .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
        //     .save(exporter, "copper_shortbow");

        new WorkbenchRecipeBuilder(ModRecipes.BLACKSMITHS_TYPE, ModRecipes.BLACKSMITHS_SERIALIZER)
             .input(Items.COPPER_INGOT, 4)
             .input(ItemTags.LOGS, lookup, 4)
             .input(ModItems.PLANT_FIBER, 3)
             .output(Items.COPPER_SWORD)
             .time(3)
             .unlockedBy("has_blacksmiths_workbench", provider.has(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.BLACKSMITHS_SEARCH)
             .save(exporter, "copper_sword");
    }
}
