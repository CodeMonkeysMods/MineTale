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
import net.minecraft.world.level.block.Blocks;

public class FurnitureRecipes {
    /**
     * Register a set of furniture workbench crafting recipes and save them to the provided exporter.
     *
     * Defines multiple recipes using ModRecipes.FURNITURE_TYPE and ModRecipes.FURNITURE_SERIALIZER, each
     * unlocked by possession of the furniture workbench and categorised under the furniture search display.
     *
     * @param provider the recipe provider used to build unlock criteria
     * @param exporter the destination to which each constructed recipe is saved
     * @param lookup   a holder lookup used for tag-based or lookup-dependent recipe inputs
     */
    public static void buildRecipes(RecipeProvider provider, RecipeOutput exporter, HolderLookup.Provider lookup) {
        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ItemTags.LOGS, lookup)
             .input(ModItems.RUBBLE, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .input(Items.STICK, 2)
             .output(ModBlocks.WOODCUTTERS_BLOCK)
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "woodcutters_block");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 12)
             .input(ModItems.LIGHT_LEATHER, 4)
             .output(ModBlocks.LARGE_PILE_OF_BOOKS.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "large_pile_of_books");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 6)
             .input(ModItems.LIGHT_LEATHER, 2)
             .output(ModBlocks.SMALL_PILE_OF_BOOKS.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_pile_of_books");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(Blocks.CYAN_WOOL, 2)
             .input(ModItems.PLANT_FIBER, 4)
             .output(ModBlocks.KWEEBEC_PLUSHIE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "kweebec_plushie");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 4)
             .input(Items.STICK, 2)
             .output(ModBlocks.OLD_SCROLL.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "old_scroll");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.TREE_SAP, 2)
             .output(ModBlocks.ANCIENT_CANDLE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "ancient_candle");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.LINEN_SCRAPS)
             .input(ModItems.YELLOW_PETALS)
             .input(ModItems.RED_PETALS, 2)
             .output(ModBlocks.SMALL_RED_DOTTED_CHRISTMAS_PACKET.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_red_dotted_christmas_packet");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.LINEN_SCRAPS)
             .input(ModItems.YELLOW_PETALS, 2)
             .output(ModBlocks.SMALL_RED_CHRISTMAS_PACKET.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_red_christmas_packet");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.LINEN_SCRAPS)
             .input(ModItems.YELLOW_PETALS, 3)
             .output(ModBlocks.SMALL_CHRISTMAS_PACKET.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_christmas_packet");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.LINEN_SCRAPS)
             .input(ModItems.GREEN_PETALS, 3)
             .output(ModBlocks.SMALL_GREEN_CHRISTMAS_PACKET.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_green_christmas_packet");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.LINEN_SCRAPS)
             .input(ModItems.WHITE_PETALS, 3)
             .output(ModBlocks.SMALL_WHITE_CHRISTMAS_PACKET.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_white_christmas_packet");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.TREE_SAP, 2)
             .input(Items.BONE, 4)
             .output(ModBlocks.FERAN_CANDLE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "feran_candle");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.TREE_SAP, 2)
             .input(Items.BONE, 4)
             .output(ModBlocks.FERAN_TORCH.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "feran_torch");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(Blocks.OAK_PLANKS, 3)
             .input(ModItems.PLANT_FIBER, 4)
             .input(ModItems.MEDIUM_LEATHER, 2)
             .input(Items.BONE, 2)
             .output(ModBlocks.FERAN_BED.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "feran_bed");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModBlocks.BAMBOO_LOG)
             .input(ModItems.PLANT_FIBER, 2)
             .input(ModItems.TREE_SAP, 2)
             .output(ModBlocks.BAMBOO_CANDLE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "bamboo_candle");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModBlocks.BAMBOO_LOG, 2)
             .input(ModItems.PLANT_FIBER, 4)
             .input(Blocks.CYAN_WOOL, 2)
             .input(Blocks.ORANGE_WOOL)
             .input(Blocks.WHITE_WOOL)
             .output(ModBlocks.BAMBOO_BED.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "bamboo_bed");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.ESSENCE_OF_LIFE, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .input(Items.STICK)
             .output(ModBlocks.SMALL_KWEEBEC_CHEST.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "small_kweebec_chest");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(Blocks.SPRUCE_PLANKS, 3)
             .input(ModItems.PLANT_FIBER, 4)
             .input(ModItems.HEAVY_HIDE, 2)
             .input(Items.WHITE_WOOL)
             .output(ModBlocks.LUMBERJACK_BED.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "lumberjack_bed");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.TREE_SAP, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .output(ModBlocks.KWEEBEC_CANDLE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "kweebec_candle");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ItemTags.LOGS, lookup, 3)
             .input(ModItems.PLANT_FIBER, 4)
             .output(ModBlocks.KWEEBEC_BED.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "kweebec_bed");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.TREE_SAP, 2)
             .input(ModItems.PLANT_FIBER, 2)
             .output(ModBlocks.TAVERN_CANDLE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "tavern_candle");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(Blocks.DARK_OAK_PLANKS, 3)
             .input(ModItems.PLANT_FIBER, 4)
             .input(Blocks.RED_WOOL, 2)
             .input(Blocks.WHITE_WOOL)
             .output(ModBlocks.TAVERN_BED.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "tavern_bed");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 4)
             .input(ModItems.YELLOW_PETALS)
             .output(ModBlocks.WINTER_ROLL.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "winter_roll");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER)
             .input(ModItems.YELLOW_CRYSTAL_SHARDS)
             .input(ModItems.RED_CRYSTAL_SHARDS)
             .output(ModBlocks.WINTER_BAUBLE.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "winter_bauble");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 6)
             .input(ModItems.PINECONE, 4)
             .input(ModItems.RED_PETALS, 2)
             .output(ModBlocks.WINTER_WREATH.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "winter_wreath");

        new WorkbenchRecipeBuilder(ModRecipes.FURNITURE_TYPE, ModRecipes.FURNITURE_SERIALIZER)
             .input(ModItems.PLANT_FIBER, 3)
             .input(ModItems.PINECONE, 3)
             .input(ModItems.RED_PETALS, 2)
             .output(ModBlocks.WINTER_GARLAND.asItem())
             .unlockedBy("has_furniture_workbench", provider.has(ModBlocks.FURNITURE_WORKBENCH_BLOCK))
             .bookCategory(ModRecipeDisplay.FURNITURE_SEARCH)
             .save(exporter, "winter_garland");
    }
}
