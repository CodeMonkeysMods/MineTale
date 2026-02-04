package com.tcm.MineTale;

import com.tcm.MineTale.datagen.ModLangProvider;
import com.tcm.MineTale.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MineTaleDataGen implements DataGeneratorEntrypoint {

    /**
     * Registers mod data-generation providers with the given Fabric data generator.
     *
     * Creates a data pack and adds the language and model providers used to generate
     * the mod's localization and model assets.
     *
     * @param fabricDataGenerator the Fabric data generator to initialize
     */
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLangProvider::new);
        pack.addProvider(ModModelProvider::new);
    }
}