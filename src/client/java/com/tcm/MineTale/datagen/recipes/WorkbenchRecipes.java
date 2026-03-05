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

public class WorkbenchRecipes {
    /**
     * Registers workbench crafting recipes and saves them to the provided exporter.
     *
     * Builds and configures multiple WorkbenchRecipeBuilder instances (inputs, outputs, craft time,
     * unlock conditions, and book category) and persists each recipe using the exporter with a
     * unique identifier.
     *
     * @param provider a RecipeProvider used to query existing items/blocks for unlock conditions and tags
     * @param exporter the RecipeOutput that receives and writes the generated recipe data
     * @param lookup   a HolderLookup.Provider used to resolve tag holders (e.g., ItemTags) when specifying inputs
     */
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(Items.COPPER_INGOT, 2)
            .input(ItemTags.LOGS, lookup, 10)
            .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 5)
            .output(ModBlocks.ARMORERS_WORKBENCH_BLOCK)
            .time(3)
            .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "workbench_armorers_workbench");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(ItemTags.LOGS, lookup, 6)
            .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 6)
            .output(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1)
            .time(3)
            .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "workbench_furnace_workbench_t1");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(ItemTags.LOGS, lookup, 6)
            .input(ModItems.PLANT_FIBER, 20)
            .output(ModBlocks.FARMERS_WORKBENCH_BLOCK)
            .time(3)
            .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "workbench_farmers_workbench");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
            .input(ItemTags.LOGS, lookup, 6)
            .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 3)
            .output(ModBlocks.BUILDERS_WORKBENCH_BLOCK)
            .time(2)
            .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
            .save(exporter, "workbench_builders_workbench");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.PLANT_FIBER)
             .output(ModBlocks.HAY_TARGET.asItem())
             .time(1)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "hay_target");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 3)
             .input(ModItems.LIGHT_HIDE, 2)
             .output(ModBlocks.CRUDE_BEDROLL.asItem())
             .time(1)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_bedroll");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.PLANT_FIBER)
             .input(ModItems.TREE_SAP)
             .input(Items.STICK)
             .time(0.5F)
                //TODO: Need to adjust the output to allow count
             .output(ModBlocks.CRUDE_TORCH.asItem())
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_torch");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.RUBBLE, 2)
             .input(ModItems.PLANT_FIBER, 3)
             .input(Items.STICK, 3)
             .time(3)
             .output(ModItems.CRUDE_BUILDERS_HAMMER)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_builders_hammer");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.PLANT_FIBER)
             .input(ItemTags.LOGS, lookup, 6)
             .input(Items.COPPER_INGOT, 3)
             .time(3)
             .output(Items.COPPER_PICKAXE)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "copper_pickaxe");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.RUBBLE, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .input(Items.STICK, 2)
             .time(3)
             .output(Items.STONE_PICKAXE)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "stone_pickaxe");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.RUBBLE, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .input(Items.STICK, 2)
             .time(3)
             .output(ModItems.CRUDE_HATCHET)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_hatchet");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 3)
             .input(ItemTags.LOGS, lookup, 6)
             .input(Items.COPPER_INGOT, 3)
             .time(3)
             .output(ModItems.COPPER_HATCHET)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "copper_hatchet");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ItemTags.LOGS, lookup, 3)
             .input(ModItems.PLANT_FIBER, 4)
             .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 6)
             .time(3)
             .output(ModItems.CRUDE_BATTLEAXE)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_battleaxe");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ItemTags.LOGS, lookup, 3)
             .input(ModItems.PLANT_FIBER, 4)
             .input(ItemTags.STONE_TOOL_MATERIALS, lookup, 6)
             .time(3)
             .output(ModItems.CRUDE_MACE)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_mace");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.RUBBLE, 3)
             .input(ModItems.PLANT_FIBER, 2)
             .input(Items.STICK, 2)
             .time(3)
             .output(ModItems.CRUDE_DAGGERS)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_daggers");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 6)
             .input(Items.STICK, 4)
             .time(3)
             .output(ModItems.CRUDE_SHORTBOW)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_shortbow");

        new WorkbenchRecipeBuilder(ModRecipes.WORKBENCH_TYPE, ModRecipes.WORKBENCH_SERIALIZER)
             .input(ModItems.RUBBLE, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .input(Items.STICK, 2)
             .time(3)
             .output(ModItems.CRUDE_SWORD)
             .unlockedBy("has_workbench", provider.has(ModBlocks.WORKBENCH_WORKBENCH_BLOCK.asItem()))
             .bookCategory(ModRecipeDisplay.WORKBENCH_SEARCH)
             .save(exporter, "crude_sword");
    }
}
