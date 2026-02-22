package com.tcm.MineTale.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {
    
    public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("minetale.creative_tab.title", "MineTale Stuffs");

        // --- BLOCKS ---
        translationBuilder.add("block.minetale.workbench_workbench_block", "Workbench");
        translationBuilder.add("block.minetale.armorers_workbench_block", "Armorer's Workbench");
        translationBuilder.add("block.minetale.furnace_workbench_block_t1", "Furnace Workbench - Tier One");
        translationBuilder.add("block.minetale.furnace_workbench_block_t2", "Furnace Workbench - Tier Two");
        translationBuilder.add("block.minetale.campfire_workbench_block", "Campfire Workbench");

        translationBuilder.add("block.minetale.amber_log", "Amber Log");
        translationBuilder.add("block.minetale.bamboo_log", "Bamboo Log");
        translationBuilder.add("block.minetale.bamboo_log_deco", "Bamboo Log Deco");
        translationBuilder.add("block.minetale.banyan_log", "Banyan Log");
        translationBuilder.add("block.minetale.beech_log", "Beech Log");
        translationBuilder.add("block.minetale.bottletree_log", "Bottletree Log");
        translationBuilder.add("block.minetale.burnt_log", "Burnt Log");
        translationBuilder.add("block.minetale.camphor_log", "Camphor Log");
        translationBuilder.add("block.minetale.fire_log", "Fire Log");
        translationBuilder.add("block.minetale.gumboab_log", "Gumboab Log");
        translationBuilder.add("block.minetale.ice_log", "Ice Log");
        translationBuilder.add("block.minetale.maple_log", "Maple Log");
        translationBuilder.add("block.minetale.palm_tree_log", "Palm Tree Log");
        translationBuilder.add("block.minetale.palo_log", "Palo Log");
        translationBuilder.add("block.minetale.poisoned_log", "Poisoned Log");
        translationBuilder.add("block.minetale.redwood_log", "Redwood Log");
        translationBuilder.add("block.minetale.sallow_log", "Sallow Log");
        translationBuilder.add("block.minetale.spiral_log", "Spiral Log");
        translationBuilder.add("block.minetale.stormbark_log", "Stormbark Log");
        translationBuilder.add("block.minetale.stripped_log", "Stripped Log");
        translationBuilder.add("block.minetale.windwillow_log", "Windwillow Log");
        translationBuilder.add("block.minetale.wild_wisteria_log", "Wild Wisteria Log");
        translationBuilder.add("block.minetale.wild_wisteria_wood", "Wild Wisteria Wood");

        translationBuilder.add("block.minetale.copper_ore_basalt", "Basalt Copper Ore");
        translationBuilder.add("block.minetale.copper_ore_volcanic", "Volcanic Copper Ore");
        translationBuilder.add("block.minetale.copper_ore_shale", "Shale Copper Ore");
        translationBuilder.add("block.minetale.copper_ore_sandstone", "Sandstone Copper Ore");
        translationBuilder.add("block.minetale.onyxium_ore_basalt", "Basalt Onyxium Ore");
        translationBuilder.add("block.minetale.onyxium_ore_volcanic", "Volcanic Onyxium Ore");
        translationBuilder.add("block.minetale.onyxium_ore_shale", "Shale Onyxium Ore");
        translationBuilder.add("block.minetale.onyxium_ore_stone", "Onyxium Ore");
        translationBuilder.add("block.minetale.onyxium_ore_sandstone", "Sandstone Onyxium Ore");
        translationBuilder.add("block.minetale.thorium_ore_basalt", "Basalt Thorium Ore");
        translationBuilder.add("block.minetale.thorium_ore_volcanic", "Volcanic Thorium Ore");
        translationBuilder.add("block.minetale.thorium_ore_shale", "Shale Thorium Ore");
        translationBuilder.add("block.minetale.thorium_ore_stone", "Thorium Ore");
        translationBuilder.add("block.minetale.thorium_ore_sandstone", "Sandstone Thorium Ore");
        translationBuilder.add("block.minetale.thorium_ore_dry_mud", "Dry Mud Thorium Ore");
        translationBuilder.add("block.minetale.adamantite_ore_basalt", "Basalt Adamantite Ore");
        translationBuilder.add("block.minetale.adamantite_ore_volcanic", "Volcanic Adamantite Ore");
        translationBuilder.add("block.minetale.adamantite_ore_shale", "Shale Adamantite Ore");
        translationBuilder.add("block.minetale.adamantite_ore_stone", "Adamantite Ore");
        translationBuilder.add("block.minetale.adamantite_ore_slate", "Slate Adamantite Ore");
        translationBuilder.add("block.minetale.adamantite_ore_magma", "Magma Adamantite Ore");
        translationBuilder.add("block.minetale.mithril_ore_basalt", "Basalt Mithril Ore");
        translationBuilder.add("block.minetale.mithril_ore_volcanic", "Volcanic Mithril Ore");
        translationBuilder.add("block.minetale.mithril_ore_slate", "Slate Mithril Ore");
        translationBuilder.add("block.minetale.mithril_ore_stone", "Mithril Ore");
        translationBuilder.add("block.minetale.mithril_ore_magma", "Magma Mithril Ore");
        translationBuilder.add("block.minetale.cobalt_ore_basalt", "Basalt Cobalt Ore");
        translationBuilder.add("block.minetale.cobalt_ore_volcanic", "Volcanic Cobalt Ore");
        translationBuilder.add("block.minetale.cobalt_ore_shale", "Shale Cobalt Ore");
        translationBuilder.add("block.minetale.cobalt_ore_stone", "Cobalt Ore");
        translationBuilder.add("block.minetale.cobalt_ore_sandstone", "Sandstone Cobalt Ore");
        translationBuilder.add("block.minetale.cobalt_ore_slate", "Slate Cobalt Ore");
        translationBuilder.add("block.minetale.iron_ore_basalt", "Basalt Iron Ore");
        translationBuilder.add("block.minetale.iron_ore_volcanic", "Volcanic Iron Ore");
        translationBuilder.add("block.minetale.iron_ore_shale", "Shale Iron Ore");
        translationBuilder.add("block.minetale.iron_ore_sandstone", "Sandstone Iron Ore");
        translationBuilder.add("block.minetale.iron_ore_slate", "Slate Iron Ore");
        translationBuilder.add("block.minetale.gold_ore_basalt", "Basalt Gold Ore");
        translationBuilder.add("block.minetale.gold_ore_volcanic", "Volcanic Gold Ore");
        translationBuilder.add("block.minetale.gold_ore_shale", "Shale Gold Ore");
        translationBuilder.add("block.minetale.gold_ore_sandstone", "Sandstone Gold Ore");
        translationBuilder.add("block.minetale.gold_ore_calcite", "Calcite Gold Ore");
        translationBuilder.add("block.minetale.silver_ore_basalt", "Basalt Silver Ore");
        translationBuilder.add("block.minetale.silver_ore_volcanic", "Volcanic Silver Ore");
        translationBuilder.add("block.minetale.silver_ore_shale", "Shale Silver Ore");
        translationBuilder.add("block.minetale.silver_ore_stone", "Silver Ore");
        translationBuilder.add("block.minetale.silver_ore_sandstone", "Sandstone Silver Ore");
        translationBuilder.add("block.minetale.silver_ore_slate", "Slate Silver Ore");

        // --- NATURAL MATERIALS & GATHERABLES ---
        translationBuilder.add("item.minetale.plant_fiber", "Plant Fiber");
        translationBuilder.add("item.minetale.tree_sap", "Tree Sap");
        translationBuilder.add("item.minetale.sap_glob", "Sap Glob");
        translationBuilder.add("item.minetale.rubble", "Rubble");
        translationBuilder.add("item.minetale.tree_bark", "Tree Bark");
        translationBuilder.add("item.minetale.moss", "Moss");
        translationBuilder.add("item.minetale.blue_crystal_shards", "Blue Crystal Shards");
        translationBuilder.add("item.minetale.green_crystal_shards", "Green Crystal Shards");
        translationBuilder.add("item.minetale.yellow_crystal_shards", "Yellow Crystal Shards");

        // --- MINERALS & REFINED METALS ---
        translationBuilder.add("item.minetale.thorium_ingot", "Thorium Ingot");
        translationBuilder.add("item.minetale.cobalt_ingot", "Cobalt Ingot");
        translationBuilder.add("item.minetale.adamantite_ingot", "Adamantite Ingot");
        translationBuilder.add("item.minetale.mithril_ingot", "Mithril Ingot");
        translationBuilder.add("item.minetale.bronze_ingot", "Bronze Ingot");
        translationBuilder.add("item.minetale.steel_ingot", "Steel Ingot");

        translationBuilder.add("item.minetale.thorium_ore", "Thorium Ore");
        translationBuilder.add("item.minetale.cobalt_ore", "Cobalt Ore");
        translationBuilder.add("item.minetale.adamantite_ore", "Adamantite Ore");
        translationBuilder.add("item.minetale.mithril_ore", "Mithril Ore");

        // --- MOB DROPS, HIDES & LEATHERS ---
        translationBuilder.add("item.minetale.light_hide", "Light Hide");
        translationBuilder.add("item.minetale.medium_hide", "Medium Hide");
        translationBuilder.add("item.minetale.heavy_hide", "Heavy Hide");
        translationBuilder.add("item.minetale.soft_hide", "Soft Hide");
        translationBuilder.add("item.minetale.prismatic_hide", "Prismatic Hide");
        translationBuilder.add("item.minetale.light_leather", "Light Leather");
        translationBuilder.add("item.minetale.medium_leather", "Medium Leather");
        translationBuilder.add("item.minetale.heavy_leather", "Heavy Leather");
        translationBuilder.add("item.minetale.storm_leather", "Storm Leather");
        translationBuilder.add("item.minetale.prismatic_leather", "Prismatic Leather");
        translationBuilder.add("item.minetale.feran_rib", "Feran Rib");
        translationBuilder.add("item.minetale.sturdy_chitin", "Sturdy Chitin");
        translationBuilder.add("item.minetale.venom_sac", "Venom Sac");
        translationBuilder.add("item.minetale.bone_fragment", "Bone Fragment");

        // --- FABRICS & TEXTILES ---
        translationBuilder.add("item.minetale.linen_scraps", "Linen Scraps");
        translationBuilder.add("item.minetale.bolt_of_linen", "Bolt of Linen");
        translationBuilder.add("item.minetale.shadoweave_scraps", "Shadoweave Scraps");
        translationBuilder.add("item.minetale.cindercloth_scraps", "Cindercloth Scraps");
        translationBuilder.add("item.minetale.bolt_of_wool", "Bolt of Wool");
        translationBuilder.add("item.minetale.yellow_cloth", "Yellow Cloth");

        // --- SEEDS & FARMING ---
        translationBuilder.add("item.minetale.lettuce", "Lettuce");
        translationBuilder.add("item.minetale.chilli_seed_bag", "Chilli Seed Bag");
        translationBuilder.add("item.minetale.chilli_seed_bag_eternal", "Eternal Chilli Seed Bag");
        translationBuilder.add("item.minetale.sunflower_seed_bag", "Sunflower Seed Bag");
        translationBuilder.add("item.minetale.corn_seed_bag", "Corn Seed Bag");
        translationBuilder.add("item.minetale.cotton_seed_bag", "Cotton Seed Bag");
        translationBuilder.add("item.minetale.rice_seed_bag", "Rice Seed Bag");
        translationBuilder.add("item.minetale.onion_bulb", "Onion Bulb");

        // --- MAGICAL & ALCHEMICAL ---
        translationBuilder.add("item.minetale.essence_of_life", "Essence of Life");
        translationBuilder.add("item.minetale.essence_of_fire", "Essence of Fire");
        translationBuilder.add("item.minetale.essence_of_ice", "Essence of Ice");
        translationBuilder.add("item.minetale.essence_of_the_void", "Essence of the Void");
        translationBuilder.add("item.minetale.void_heart", "Void Heart");

        // --- FLORA COMPONENTS ---
        translationBuilder.add("item.minetale.red_petals", "Red Petals");
        translationBuilder.add("item.minetale.yellow_petals", "Yellow Petals");
        translationBuilder.add("item.minetale.green_petals", "Green Petals");
        translationBuilder.add("item.minetale.white_petals", "White Petals");
        translationBuilder.add("item.minetale.azure_petals", "Azure Petals");
        translationBuilder.add("item.minetale.storm_petals", "Storm Petals");
        translationBuilder.add("item.minetale.blood_petals", "Blood Petals");
        translationBuilder.add("item.minetale.cyan_petals", "Cyan Petals");
    }
}