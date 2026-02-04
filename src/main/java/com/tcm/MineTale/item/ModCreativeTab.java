package com.tcm.MineTale.item;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.registry.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final ResourceKey<CreativeModeTab> MINETALE_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "creative_tab"));
    public static final CreativeModeTab MINETALE_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.FURNACE_WORKBENCH_BLOCK))
            .title(Component.translatable("minetale.creative_tab.title"))
            .displayItems((params, output) -> {
                output.accept(ModBlocks.FURNACE_WORKBENCH_BLOCK);
                output.accept(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK);
                output.accept(ModBlocks.AMBER_LOG);
                output.accept(ModBlocks.ASH_LOG);
                output.accept(ModBlocks.ASPEN_LOG);
                output.accept(ModBlocks.AZURE_LOG);
                output.accept(ModBlocks.BAMBOO_LOG);
                output.accept(ModBlocks.BAMBOO_LOG_DECO);
                output.accept(ModBlocks.BANYAN_LOG);
                output.accept(ModBlocks.BEECH_LOG);
                output.accept(ModBlocks.BOTTLETREE_LOG);
                output.accept(ModBlocks.BURNT_LOG);
                output.accept(ModBlocks.CAMPHOR_LOG);
                output.accept(ModBlocks.CEDAR_LOG);
                output.accept(ModBlocks.CRYSTALWOOD_LOG);
                output.accept(ModBlocks.DRY_LOG);
                output.accept(ModBlocks.BLUE_FIG_LOG);
                output.accept(ModBlocks.FIRE_LOG);
                output.accept(ModBlocks.GUMBOAB_LOG);
                output.accept(ModBlocks.ICE_LOG);
                output.accept(ModBlocks.MAPLE_LOG);
                output.accept(ModBlocks.PALM_TREE_LOG);
                output.accept(ModBlocks.PALO_LOG);
                output.accept(ModBlocks.PETRIFIED_LOG);
                output.accept(ModBlocks.POISONED_LOG);
                output.accept(ModBlocks.REDWOOD_LOG);
                output.accept(ModBlocks.SALLOW_LOG);
                output.accept(ModBlocks.SPIRAL_LOG);
                output.accept(ModBlocks.STORMBARK_LOG);
                output.accept(ModBlocks.STRIPPED_LOG);
                output.accept(ModBlocks.WINDWILLOW_LOG);
                output.accept(ModBlocks.WILD_WISTERIA_LOG);
            })
            .build();
}

