package com.tcm.MineTale.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {

    /**
     * Initializes data generation by creating a new data pack and registering recipe providers.
     *
     * Creates a FabricDataGenerator.Pack from the provided generator and registers
     * ModRecipeProvider as a provider for that pack.
     *
     * @param fabricDataGenerator the FabricDataGenerator used to create packs and register providers
     */
    @Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
    }
    
}