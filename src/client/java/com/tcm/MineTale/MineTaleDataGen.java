package com.tcm.MineTale;

import com.tcm.MineTale.datagen.ModLangProvider;
import com.tcm.MineTale.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MineTaleDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLangProvider::new);
        pack.addProvider(ModModelProvider::new);
    }
}
