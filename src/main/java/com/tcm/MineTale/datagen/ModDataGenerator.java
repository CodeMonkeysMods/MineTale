package com.tcm.MineTale.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {

    /**
     * Initialize data generation by creating a data pack and registering generation providers.
     *
     * Registers ModRecipeProvider and ModBlockTagProvider with a newly created FabricDataGenerator.Pack.
     *
     * @param fabricDataGenerator the FabricDataGenerator used to create the pack and register providers
     */
    @Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
    }
    
}