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
import net.minecraft.world.level.block.Blocks;

public class FarmerRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        // new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
        //     .input(ItemTags.PLANKS, lookup, 20)
        //     .input(ModItems.ESSENCE_OF_LIFE, 50)
        //     .input(ModItems.PLANT_FIBER, 6)
        //     .output(ModBlocks.CHICKEN_COOP)
        //     .time(2)
        //     .unlockedBy("has_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
        //     .save(exporter, "CHICKEN_COOP");

        // TODO: LOTS Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
        //     .input(ModItems.GREATER_ESSENCE_OF_LIFE, 100)
        //     .input(ModItems.WILD_BERRY, 100)
        //     .input(Items.WHEAT, 100)
        //     .input(ModItems.LETTUCE, 100)
        //     .input(Items.CARROT, 100)
        //     .input(ModItems.CORN, 100)
        //     .input(ModItems.CAULIFLOWER, 100)
        //     .input(ModItems.TURNIP, 100)
        //     .input(ModItems.AUBERGINE, 100)
        //     .input(Items.PUMPKIN)
        //     .input(ModItems.TOMATO, 100)
        //     .input(ModItems.CHILLI, 100)
        //     .input(ModItems.COTTON, 100)
        //     .input(ModItems.RICE, 100)
        //     .input(ModItems.ONION, 100)
        //     .input(Items.POTATO, 100)
        //     .input(Items.APPLE, 100)
        //     .input(Items.EGG, 100)
        //     .input(ModItems.WOOL_SCRAPS, 100)
        //     .input(ItemTags.FISHES, lookup, 100)
        //     .input(ItemTags.MOSS, lookup, 100)
        //     .input(ItemTags.LOGS, lookup, 100)
        //     .input(ModItems.POOP, 100)
        //     .input(Items.FEATHER, 100)
        //     .input(ModItems.TREE_SAP, 100)
        //     .input(ModItems.PLANT_FIBER, 100)
        //     .input(ItemTags.MILK_BUCKET, 8)
        //     .input(ItemTags.MOSS_HORN_MILK_BUCKET, 8)
        //     .output(ModBlocks.HARVEST_TROPHY)
        //     .time(10)
        //     .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
        //     .save(exporter, "farmers_workbench_harvest_trophy");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.PLANT_FIBER, 20)
            .input(ItemTags.DIRT, lookup, 2)
            .input(ModItems.ESSENCE_OF_LIFE, 10)
            .input(ModBlocks.BAMBOO_LOG)
            .output(ModBlocks.BAMBOO_PLANTER)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "bamboo_planter");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.BLUE_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.BLUE_MOSS_BLOCK)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "blue_moss_block");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.DARK_GREEN_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.DARK_GREEN_MOSS_BLOCK)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dark_green_moss_block");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.GREEN_MOSS_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "green_moss_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.RED_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.RED_MOSS_BLOCK)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "red_moss_block");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.BLUE_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.BLUE_MOSS_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "blue_moss_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.RED_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.RED_HANGING_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "red_hanging_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.DARK_GREEN_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.DARK_GREEN_HANGING_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dark_green_hanging_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.BLUE_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.BLUE_HANGING_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "blue_hanging_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.YELLOW_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.YELLOW_MOSS_BLOCK)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "yellow_moss_block");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.YELLOW_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.YELLOW_HANGING_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "yellow_hanging_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.GREEN_MOSS_BLOCK)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "green_moss_block");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.GREEN_HANGING_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "green_hanging_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.DARK_GREEN_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.DARK_GREEN_MOSS_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dark_green_moss_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.YELLOW_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.YELLOW_MOSS_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "yellow_moss_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.SORREL_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "sorrel_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.RED_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.RED_MOSS_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "red_moss_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.RED_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.SHORT_RED_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "short_red_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.YELLOW_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.SHORT_YELLOW_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "short_yellow_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.BLUE_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.SHORT_BLUE_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "short_blue_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.DARK_GREEN_MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.SHORT_DARK_GREEN_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "short_dark_green_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModBlocks.MOSS, 4)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.SHORT_MOSS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "short_moss");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.LIANA)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "liana");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.VINE)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "vine");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.DRY_VINE)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dry_vine");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.POISONED_IVY)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "poisoned_ivy");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.WALL_IVY)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "wall_ivy");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 2)
            .output(ModBlocks.IVY)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "ivy");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ModItems.ESSENCE_OF_LIFE)
            .input(ModItems.PLANT_FIBER, 4)
            .output(ModBlocks.VINE_RUG)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "vine_rug");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.COLD_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "cold_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.BURNT_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "burnt_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.POISONED_DIRT)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "poisoned_dirt");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.DEEP_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "deep_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.DRY_DIRT)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dry_dirt");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.DRY_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dry_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.WET_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "wet_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.SUMMER_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "summer_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(Blocks.GRASS_BLOCK)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "grass_block");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.COLD_DIRT)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "cold_dirt");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.FULL_GRASS)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "full_grass");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.NEEDLED_SOIL)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "needled_soil");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.SOIL_PATHWAY)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "soil_pathway");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(Blocks.MUD)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "mud");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.LEAFY_SOIL)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "leafy_soil");

        new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
            .input(ItemTags.DIRT, lookup)
            .input(ModItems.PLANT_FIBER)
            .output(ModBlocks.DRY_MUD)
            .time(2)
            .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
            .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
            .save(exporter, "dry_mud");

        // new WorkbenchRecipeBuilder(ModRecipes.FARMERS_TYPE, ModRecipes.FARMERS_SERIALIZER)
        //     .input(ItemTags.LOGS, lookup, 10)
        //     .input(ModItems.ESSENCE_OF_LIFE, 50)
        //     .input(ModItems.PLANT_FIBER, 20)
        //     .input(Items.IRON_INGOT)
        //     .output(ModBlocks.FISHING_TRAP)
        //     .time(10)
        //     .unlockedBy("has_farmers_workbench", provider.has(ModBlocks.FARMERS_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FARMERS_SEARCH)
        //     .save(exporter, "FISHING_TRAP");
    }
}
