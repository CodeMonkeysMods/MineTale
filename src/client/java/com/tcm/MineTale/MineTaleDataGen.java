package com.tcm.MineTale;

import com.tcm.MineTale.datagen.*;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MineTaleDataGen implements DataGeneratorEntrypoint {

    /**
     * Initialize a data pack and register the mod's data providers for data generation.
     *
     * Registers language, model, recipe, block tag, and loot table providers so they
     * will run as part of the Fabric data generation pack created from the given generator.
     *
     * @param fabricDataGenerator the Fabric data generator used to create the data pack
     */
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLangProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModLootTableProvider::new);
    }
}