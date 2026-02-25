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

public class FurnitureRecipes {
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        // TODO: WOODCUTTERS_BLOCK & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ItemTags.LOGS, lookup)
        //     .input(ModItems.RUBBLE, 2)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(Items.STICK, 2)
        //     .output(ModItems.WOODCUTTERS_BLOCK)
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "woodcutters_block");

        // TODO: LARGE_PILE_OF_BOOKS & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 12)
        //     .input(ModItems.LIGHT_LEATHER, 4)
        //     .output(ModBlocks.LARGE_PILE_OF_BOOKS.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "LARGE_PILE_OF_BOOKS");

        // TODO: SMALL_PILE_OF_BOOKS & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 6)
        //     .input(ModItems.LIGHT_LEATHER, 2)
        //     .output(ModBlocks.SMALL_PILE_OF_BOOKS.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_PILE_OF_BOOKS");

        // TODO: KWEEBEC_PLUSHIE & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModBlocks.CYAN_CLOTH, 2)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .output(ModBlocks.KWEEBEC_PLUSHIE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "KWEEBEC_PLUSHIE");

        // TODO: OLD_SCROLL & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .input(Items.STICK, 2)
        //     .output(ModBlocks.OLD_SCROLL.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "OLD_SCROLL");

        // TODO: ANCIENT_SCROLL & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.TREE_SAP, 2)
        //     .output(ModBlocks.ANCIENT_CANDLE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "ANCIENT_CANDLE");

        // TODO: SMALL_RED_DOTTED_CHRISTMAS_PACKET & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.LINEN_SCRAPS)
        //     .input(ModItems.YELLOW_PETALS)
        //     .input(ModItems.RED_PETALS, 2)
        //     .output(ModBlocks.SMALL_RED_DOTTED_CHRISTMAS_PACKET.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_RED_DOTTED_CHRISTMAS_PACKET");

        // TODO: SMALL_RED_CHRISTMAS_PACKET & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.LINEN_SCRAPS)
        //     .input(ModItems.YELLOW_PETALS, 2)
        //     .output(ModBlocks.SMALL_RED_CHRISTMAS_PACKET.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_RED_CHRISTMAS_PACKET");

        // TODO: SMALL_CHRISTMAS_PACKET & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.LINEN_SCRAPS)
        //     .input(ModItems.YELLOW_PETALS, 3)
        //     .output(ModBlocks.SMALL_CHRISTMAS_PACKET.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_CHRISTMAS_PACKET");

        // TODO: SMALL_GREEN_CHRISTMAS_PACKET & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.LINEN_SCRAPS)
        //     .input(ModItems.GREEN_PETALS, 3)
        //     .output(ModBlocks.SMALL_GREEN_CHRISTMAS_PACKET.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_GREEN_CHRISTMAS_PACKET");

        // TODO: SMALL_WHITE_CHRISTMAS_PACKET & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.LINEN_SCRAPS)
        //     .input(ModItems.WHITE_PETALS, 3)
        //     .output(ModBlocks.SMALL_WHITE_CHRISTMAS_PACKET.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_WHITE_CHRISTMAS_PACKET");

        // TODO: FERAN_CANDLE & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.TREE_SAP, 2)
        //     .input(Items.BONE, 4)
        //     .output(ModBlocks.FERAN_CANDLE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "FERAN_CANDLE");

        // TODO: FERAN_TORCH & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.TREE_SAP, 2)
        //     .input(Items.BONE, 4)
        //     .output(ModBlocks.FERAN_TORCH.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "FERAN_TORCH");

        // TODO: FERAN_BED & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModBlocks.DRYWOOD_PLANKS, 3)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .input(ModItems.MEDIUM_LEATHER, 2)
        //     .input(Items.BONE, 2)
        //     .output(ModBlocks.FERAN_BED.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "FERAN_BED");

        // TODO: BAMBOO_CANDLE & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModBlocks.BAMBOO_LOG)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(ModItems.TREE_SAP, 2)
        //     .output(ModBlocks.BAMBOO_CANDLE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "BAMBOO_CANDLE");

        // TODO: BAMBOO_BED & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModBlocks.BAMBOO_LOG, 2)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .input(ModBlocks.CYAN_CLOTH, 2)
        //     .input(ModBlocks.ORANGE_CLOTH)
        //     .input(ModBlocks.WHITE_CLOTH)
        //     .output(ModBlocks.BAMBOO_BED.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "BAMBOO_BED");

        // TODO: SMALL_KWEEBEC_CHEST & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.ESSENCE_OF_LIFE, 2)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .input(Items.STICK)
        //     .output(ModBlocks.SMALL_KWEEBEC_CHEST.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "SMALL_KWEEBEC_CHEST");

        // TODO: LUMBERJACK_BED & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModBlocks.HARDWOOD_PLANKS, 3)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .input(ModItems.HEAVY_HIDE, 2)
        //     .input(ModItems.WHITE_CLOTH)
        //     .output(ModBlocks.LUMBERJACK_BED.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "LUMBERJACK_BED");

        // TODO: KWEEBEC_CANDLE & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.TREE_SAP, 2)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .output(ModBlocks.KWEEBEC_CANDLE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "KWEEBEC_CANDLE");

        // TODO: KWEEBEC_BED & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ItemTags.LOGS, lookup, 3)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .output(ModBlocks.KWEEBEC_BED.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "KWEEBEC_BED");

        // TODO: TAVERN_CANDLE & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.TREE_SAP, 2)
        //     .input(ModItems.PLANT_FIBER, 2)
        //     .output(ModBlocks.TAVERN_CANDLE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "TAVERN_CANDLE");

        // TODO: TAVERN_BED & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModBlocks.DARKWOOD_PLANKS, 3)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .input(ModBlocks.RED_CLOTH, 2)
        //     .input(ModBlocks.WHITE_CLOTH)
        //     .output(ModBlocks.TAVERN_BED.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "TAVERN_BED");

        // TODO: WINTER_ROLL & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 4)
        //     .input(ModItems.YELLOW_PETALS)
        //     .output(ModBlocks.WINTER_ROLL.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "WINTER_ROLL");

        // TODO: WINTER_BAUBLE & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER)
        //     .input(ModItems.YELLOW_CRYSTAL_SHARDS)
        //     .input(ModItems.RED_CRYSTAL_SHARDS)
        //     .output(ModBlocks.WINTER_BAUBLE.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "WINTER_BAUBLE");

        // TODO: WINTER_WREATH & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 6)
        //     .input(ModItems.PINECONE, 4)
        //     .input(ModItems.RED_PETALS, 2)
        //     .output(ModBlocks.WINTER_WREATH.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "WINTER_WREATH");

        // TODO: WINTER_GARLAND & FURNITURE_WORKBENCH_BLOCK Not Implemented
        // new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
        //     .input(ModItems.PLANT_FIBER, 3)
        //     .input(ModItems.PINECONE, 3)
        //     .input(ModItems.RED_PETALS, 2)
        //     .output(ModBlocks.WINTER_GARLAND.asItem())
        //     .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
        //     .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
        //     .save(exporter, "WINTER_GARLAND");
    }
}
