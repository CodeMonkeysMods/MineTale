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
            })
            .build();
}

