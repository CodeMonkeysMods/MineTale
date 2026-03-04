package com.tcm.MineTale.datagen;

import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    /**
     * Create a ModItemTagProvider used to generate item tag mappings for the mod during data generation.
     *
     * @param output           the Fabric data output used to write generated data
     * @param registriesFuture a future supplying a {@link HolderLookup.Provider} for resolving registries needed while building tags
     */
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Populate item tag mappings used during data generation.
     *
     * Adds entries for the mod's item tags, including:
     * - `ModTags.Items.WOOD_REPAIR` (adds `Items.STICK`)
     * - `ModTags.Items.MOSS` (adds the moss block item and various moss variant items resolved from `ModBlocks`)
     * - `ModTags.Items.MILK_BUCKETS` (adds `Items.MILK_BUCKET`)
     *
     * @param provider the registry provider used to resolve item lookups when building tags
     */
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(ModTags.Items.WOOD_REPAIR)
                .add(Items.STICK);

        valueLookupBuilder(ModTags.Items.MOSS)
                .add(Items.MOSS_BLOCK)
                .add(Item.BY_BLOCK.get(ModBlocks.MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.SHORT_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.GREEN_HANGING_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.GREEN_MOSS_RUG))
                .add(Item.BY_BLOCK.get(ModBlocks.BLUE_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.SHORT_BLUE_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.BLUE_HANGING_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.BLUE_MOSS_RUG))
                .add(Item.BY_BLOCK.get(ModBlocks.YELLOW_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.SHORT_YELLOW_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.YELLOW_HANGING_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.YELLOW_MOSS_RUG))
                .add(Item.BY_BLOCK.get(ModBlocks.RED_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.SHORT_RED_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.RED_HANGING_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.RED_MOSS_RUG))
                .add(Item.BY_BLOCK.get(ModBlocks.DARK_GREEN_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.SHORT_DARK_GREEN_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.DARK_GREEN_HANGING_MOSS))
                .add(Item.BY_BLOCK.get(ModBlocks.DARK_GREEN_MOSS_RUG));

        valueLookupBuilder(ModTags.Items.MILK_BUCKETS)
                .add(Items.MILK_BUCKET);
    }
}
