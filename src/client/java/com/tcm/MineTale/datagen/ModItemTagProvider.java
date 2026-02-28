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
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

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
