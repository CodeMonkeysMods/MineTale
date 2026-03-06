package com.tcm.MineTale.datagen;

import com.tcm.MineTale.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    /**
     * Creates a ModBlockTagProvider used to generate block tag data for the mod (e.g., assigning mod log blocks to BlockTags.LOGS).
     *
     * @param output the Fabric data output target used to write generated data
     * @param registriesFuture a future that supplies registry lookups required during data generation
     */
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Populate block tags with this mod's blocks.
     *
     * Adds mod-defined blocks to relevant vanilla block tags (for example
     * MINEABLE_WITH_PICKAXE, MINEABLE_WITH_AXE, and LOGS) so they are included
     * in the game's tag mappings.
     *
     * @param provider registry lookup provider used to resolve holders during tag population
     */
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1)
                .add(ModBlocks.FURNACE_WORKBENCH_BLOCK_T2)
                .add(ModBlocks.ARMORERS_WORKBENCH_BLOCK)
                .add(ModBlocks.BUILDERS_WORKBENCH_BLOCK)
                .add(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK)
                .add(ModBlocks.ALCHEMISTS_WORKBENCH_BLOCK);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK)
                .add(ModBlocks.WORKBENCH_WORKBENCH_BLOCK)
                .add(ModBlocks.FARMERS_WORKBENCH_BLOCK)
                .add(ModBlocks.FURNITURE_WORKBENCH_BLOCK)
                .add(ModBlocks.WOODCUTTERS_BLOCK)

                .add(ModBlocks.AMBER_LOG)
                .add(ModBlocks.BAMBOO_LOG)
                .add(ModBlocks.BAMBOO_LOG_DECO)
                .add(ModBlocks.BANYAN_LOG)
                .add(ModBlocks.BEECH_LOG)
                .add(ModBlocks.BOTTLETREE_LOG)
                .add(ModBlocks.BURNT_LOG)
                .add(ModBlocks.CAMPHOR_LOG)
                .add(ModBlocks.CRYSTALWOOD_LOG)
                .add(ModBlocks.FIRE_LOG)
                .add(ModBlocks.GUMBOAB_LOG)
                .add(ModBlocks.ICE_LOG)
                .add(ModBlocks.MAPLE_LOG)
                .add(ModBlocks.PALM_TREE_LOG)
                .add(ModBlocks.PALO_LOG)
                .add(ModBlocks.POISONED_LOG)
                .add(ModBlocks.REDWOOD_LOG)
                .add(ModBlocks.SALLOW_LOG)
                .add(ModBlocks.SPIRAL_LOG)
                .add(ModBlocks.STORMBARK_LOG)
                .add(ModBlocks.STRIPPED_LOG)
                .add(ModBlocks.WINDWILLOW_LOG)
                .add(ModBlocks.WILD_WISTERIA_LOG);

        valueLookupBuilder(BlockTags.LOGS)
                .add(ModBlocks.AMBER_LOG)
                .add(ModBlocks.BAMBOO_LOG)
                .add(ModBlocks.BAMBOO_LOG_DECO)
                .add(ModBlocks.BANYAN_LOG)
                .add(ModBlocks.BEECH_LOG)
                .add(ModBlocks.BOTTLETREE_LOG)
                .add(ModBlocks.BURNT_LOG)
                .add(ModBlocks.CAMPHOR_LOG)
                .add(ModBlocks.CRYSTALWOOD_LOG)
                .add(ModBlocks.FIRE_LOG)
                .add(ModBlocks.GUMBOAB_LOG)
                .add(ModBlocks.ICE_LOG)
                .add(ModBlocks.MAPLE_LOG)
                .add(ModBlocks.PALM_TREE_LOG)
                .add(ModBlocks.PALO_LOG)
                .add(ModBlocks.POISONED_LOG)
                .add(ModBlocks.REDWOOD_LOG)
                .add(ModBlocks.SALLOW_LOG)
                .add(ModBlocks.SPIRAL_LOG)
                .add(ModBlocks.STORMBARK_LOG)
                .add(ModBlocks.STRIPPED_LOG)
                .add(ModBlocks.WINDWILLOW_LOG)
                .add(ModBlocks.WILD_WISTERIA_LOG);
    }
}