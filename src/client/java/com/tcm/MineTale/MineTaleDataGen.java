package com.tcm.MineTale;

import com.tcm.MineTale.datagen.ModBlockTagProvider;
import com.tcm.MineTale.datagen.ModLangProvider;
import com.tcm.MineTale.datagen.ModModelProvider;
import com.tcm.MineTale.datagen.ModRecipeProvider;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MineTaleDataGen implements DataGeneratorEntrypoint {

    /**
     * Initialize a data pack and register the mod's data providers.
     *
     * Creates a data pack from the given Fabric data generator and adds the language
     * and model providers so they will run during data generation.
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
    }
}