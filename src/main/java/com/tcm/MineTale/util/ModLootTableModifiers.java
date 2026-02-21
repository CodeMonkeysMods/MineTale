package com.tcm.MineTale.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModLootTableModifiers {
    private static final Identifier SHORT_GRASS_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/short_grass");
    private static final Identifier TALL_GRASS_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/tall_grass");

    /**
     * Registers a listener that modifies the loot tables for short and tall grass.
     *
     * <p>When the listener sees the short grass loot table, it replaces its pools with a single-roll
     * pool that can produce AIR (count exactly 1) and STICK (count between 1 and 3). When the listener
     * sees the tall grass loot table, it replaces its pools with a single-roll pool that can produce
     * STICK (weight 1, count between 1 and 4).
     *
     * <p>This method registers the modification via LootTableEvents.MODIFY.
     */
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, sources, registry) -> {
            if (SHORT_GRASS_ID.equals(key.identifier())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.AIR)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 1f))))
                        .add(LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 3f))));
                tableBuilder.pool(poolBuilder.build());
            }

            if (TALL_GRASS_ID.equals(key.identifier())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.STICK).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 4f))));
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}