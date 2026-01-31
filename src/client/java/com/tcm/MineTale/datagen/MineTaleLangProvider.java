package com.tcm.MineTale.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class MineTaleLangProvider extends FabricLanguageProvider {
    protected MineTaleLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("minetale.creative_tab.title", "MineTale Stuffs");

        translationBuilder.add("block.minetale.furnace_workbench_block", "Furnace Workbench");
        translationBuilder.add("block.minetale.campfire_workbench_block", "Campfire Workbench");
    }
}
