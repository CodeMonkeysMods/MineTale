package com.tcm.MineTale.datagen;

import com.tcm.MineTale.registry.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.woodProvider(ModBlocks.AMBER_LOG).logWithHorizontal(ModBlocks.AMBER_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.ASH_LOG).logWithHorizontal(ModBlocks.ASH_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.ASPEN_LOG).logWithHorizontal(ModBlocks.ASPEN_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.AZURE_LOG).logWithHorizontal(ModBlocks.AZURE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BAMBOO_LOG).logWithHorizontal(ModBlocks.BAMBOO_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BAMBOO_LOG_DECO).logWithHorizontal(ModBlocks.BAMBOO_LOG_DECO);
        blockStateModelGenerator.woodProvider(ModBlocks.BANYAN_LOG).logWithHorizontal(ModBlocks.BANYAN_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BEECH_LOG).logWithHorizontal(ModBlocks.BEECH_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BOTTLETREE_LOG).logWithHorizontal(ModBlocks.BOTTLETREE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BURNT_LOG).logWithHorizontal(ModBlocks.BURNT_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.CAMPHOR_LOG).logWithHorizontal(ModBlocks.CAMPHOR_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.CEDAR_LOG).logWithHorizontal(ModBlocks.CEDAR_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.CRYSTALWOOD_LOG).logWithHorizontal(ModBlocks.CRYSTALWOOD_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.DRY_LOG).logWithHorizontal(ModBlocks.DRY_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.BLUE_FIG_LOG).logWithHorizontal(ModBlocks.BLUE_FIG_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.FIRE_LOG).logWithHorizontal(ModBlocks.FIRE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.GUMBOAB_LOG).logWithHorizontal(ModBlocks.GUMBOAB_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.ICE_LOG).logWithHorizontal(ModBlocks.ICE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.MAPLE_LOG).logWithHorizontal(ModBlocks.MAPLE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.PALM_TREE_LOG).logWithHorizontal(ModBlocks.PALM_TREE_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.PALO_LOG).logWithHorizontal(ModBlocks.PALO_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.PETRIFIED_LOG).logWithHorizontal(ModBlocks.PETRIFIED_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.POISONED_LOG).logWithHorizontal(ModBlocks.POISONED_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.REDWOOD_LOG).logWithHorizontal(ModBlocks.REDWOOD_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.SALLOW_LOG).logWithHorizontal(ModBlocks.SALLOW_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.SPIRAL_LOG).logWithHorizontal(ModBlocks.SPIRAL_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.STORMBARK_LOG).logWithHorizontal(ModBlocks.STORMBARK_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.STRIPPED_LOG).logWithHorizontal(ModBlocks.STRIPPED_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.WINDWILLOW_LOG).logWithHorizontal(ModBlocks.WINDWILLOW_LOG);
        blockStateModelGenerator.woodProvider(ModBlocks.WILD_WISTERIA_LOG).logWithHorizontal(ModBlocks.WILD_WISTERIA_LOG).wood(ModBlocks.WILD_WISTERIA_WOOD);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

    }
}
