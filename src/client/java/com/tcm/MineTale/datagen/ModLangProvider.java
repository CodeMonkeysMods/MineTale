package com.tcm.MineTale.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {
    /**
     * Creates a ModLangProvider configured to generate the mod's language translations.
     *
     * @param dataOutput     the FabricDataOutput used to write generated language files
     * @param registryLookup a CompletableFuture supplying a HolderLookup.Provider for registry lookups during data generation
     */
    public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    /**
     * Populates the translation builder with English language entries for MineTale (creative tab title and block names).
     *
     * @param translationBuilder the builder used to register translation keys and their English values
     */
    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("minetale.creative_tab.title", "MineTale Stuffs");


        translationBuilder.add("block.minetale.furnace_workbench_block_t1", "Furnace Workbench - Tier One");
        translationBuilder.add("block.minetale.furnace_workbench_block_t2", "Furnace Workbench - Tier Two");
        translationBuilder.add("block.minetale.campfire_workbench_block", "Campfire Workbench");

        translationBuilder.add("block.minetale.amber_log", "Amber Log");
        translationBuilder.add("block.minetale.ash_log", "Ash Log");
        translationBuilder.add("block.minetale.aspen_log", "Aspen Log");
        translationBuilder.add("block.minetale.azure_log", "Azure Log");
        translationBuilder.add("block.minetale.bamboo_log", "Bamboo Log");
        translationBuilder.add("block.minetale.bamboo_log_deco", "Bamboo Log Deco");
        translationBuilder.add("block.minetale.banyan_log", "Banyan Log");
        translationBuilder.add("block.minetale.beech_log", "Beech Log");
        translationBuilder.add("block.minetale.bottletree_log", "Bottletree Log");
        translationBuilder.add("block.minetale.burnt_log", "Burnt Log");
        translationBuilder.add("block.minetale.camphor_log", "Camphor Log");
        translationBuilder.add("block.minetale.cedar_log", "Cedar Log");
        translationBuilder.add("block.minetale.crystalwood_log", "Crystalwood Log");
        translationBuilder.add("block.minetale.dry_log", "Dry Log");
        translationBuilder.add("block.minetale.blue_fig_log", "Blue Fig Log");
        translationBuilder.add("block.minetale.fire_log", "Fire Log");
        translationBuilder.add("block.minetale.gumboab_log", "Gumboab Log");
        translationBuilder.add("block.minetale.ice_log", "Ice Log");
        translationBuilder.add("block.minetale.maple_log", "Maple Log");
        translationBuilder.add("block.minetale.palm_tree_log", "Palm Tree Log");
        translationBuilder.add("block.minetale.palo_log", "Palo Log");
        translationBuilder.add("block.minetale.petrified_log", "Petrified Log");
        translationBuilder.add("block.minetale.poisoned_log", "Poisoned Log");
        translationBuilder.add("block.minetale.redwood_log", "Redwood Log");
        translationBuilder.add("block.minetale.sallow_log", "Sallow Log");
        translationBuilder.add("block.minetale.spiral_log", "Spiral Log");
        translationBuilder.add("block.minetale.stormbark_log", "Stormbark Log");
        translationBuilder.add("block.minetale.stripped_log", "Stripped Log");
        translationBuilder.add("block.minetale.windwillow_log", "Windwillow Log");
        translationBuilder.add("block.minetale.wild_wisteria_log", "Wild Wisteria Log");
        translationBuilder.add("block.minetale.wild_wisteria_wood", "Wild Wisteria Wood");
    }
}