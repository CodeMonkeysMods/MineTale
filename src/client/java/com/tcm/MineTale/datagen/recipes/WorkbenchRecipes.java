package com.tcm.MineTale.datagen.recipes;

import com.tcm.MineTale.datagen.builders.WorkbenchRecipeBuilder;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModRecipeDisplay;
import com.tcm.MineTale.registry.ModRecipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class WorkbenchRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(Items.COPPER_INGOT, 2)
            .input(ItemTags.LOGS, lookup, 10)
            .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 5)
            .output(ModBlocks.ARMORERS_WORKBENCH_BLOCK.asItem())
            .time(3)
            .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "workbench_armorers_workbench");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(ItemTags.LOGS, lookup, 6)
            .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 6)
            .output(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1.asItem())
            .time(3)
            .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "workbench_furnace_workbench_t1");

        // TODO: FarmersWorkbench Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
        //     .input(ItemTags.LOGS, lookup, 6)
        //     .input(ModItems.PLANT_FIBER, 20)
        //     .output(ModBlocks.FARMERS_WORKBENCH)
        //     .time(3)
        //     .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
        //     .save(exporter, "workbench_farmers_workbench");

        // TODO: ChickenCoop Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
        //     .input(ItemTags.PLANKS, lookup, 20)
        //     .input(ModItems.ESSENCE_OF_LIFE, 50)
        //     .input(ModItems.PLANT_FIBER, 6)
        //     .output(ModBlocks.CHICKEN_COOP.asItem())
        //     .time(2)
        //     .unlockedBy("has_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH.asItem()))
        //     .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
        //     .save(exporter, "workbench_furnace_workbench_t1");

        // TODO: Builder's Workbench Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
        //     .input(ItemTags.LOGS, lookup, 6)
        //     .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 3)
        //     .output(ModBlocks.BUILDERS_WORKBENCH.asItem())
        //     .time(2)
        //     .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
        //     .save(exporter, "workbench_builders_workbench");

        // TODO: HAY_TARGET Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER)
        //     .output(ModBlocks.HAY_TARGET.asItem())
        //     .time(1)
        //     .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
        //     .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
        //     .save(exporter, "workbench_hay_target");

        // TODO: CRUDE_TORCH Not Implemented
        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(ModItems.PLANT_FIBER)
            .input(ModItems.TREE_SAP)
            .input(Items.STICK)
            .time(0.5)
            .output(ModBlocks.CRUDE_TORCH.asItem(), 4)
            .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "CRUDE_TORCH");
    }
}
