package com.tcm.MineTale.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {

    /**
     * Sets up data generation by creating a data pack and registering providers for recipes and block tags.
     *
     * Registers ModRecipeProvider and ModBlockTagProvider with the created pack.
     *
     * @param fabricDataGenerator the FabricDataGenerator used to create data packs and register providers
     */
    @Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
    }
    
}