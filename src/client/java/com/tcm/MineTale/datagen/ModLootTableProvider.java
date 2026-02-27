package com.tcm.MineTale.datagen;

import com.tcm.MineTale.block.workbenches.AbstractWorkbench;
import com.tcm.MineTale.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    /**
     * Creates a ModLootTableProvider used to generate the mod's block loot tables.
     *
     * Initializes the provider with the data output target and a future registry lookup used to resolve game registries during loot table generation.
     *
     * @param dataOutput     the data output target for generated data
     * @param registryLookup a future that provides a HolderLookup.Provider for resolving registries needed while generating loot tables
     */
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    /**
     * Registers loot tables for the mod's workbench blocks.
     *
     * Each added loot table causes the block to drop itself (one item) only when the block's state
     * matches the required DoubleBlockHalf (LOWER) and ChestType (LEFT or SINGLE) for that block,
     * and the drop is subject to explosion survival/decay.
     */
    @Override
    public void generate() {
        ///Block Drops Itself
        this.add(ModBlocks.ARMORERS_WORKBENCH_BLOCK,
            LootTable.lootTable() // Use the static factory method to start the builder
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ModBlocks.ARMORERS_WORKBENCH_BLOCK))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ARMORERS_WORKBENCH_BLOCK)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                            .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                        )
                    )
                    .when(ExplosionCondition.survivesExplosion())
                )
        );

        this.add(ModBlocks.BUILDERS_WORKBENCH_BLOCK,
                LootTable.lootTable() // Use the static factory method to start the builder
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModBlocks.BUILDERS_WORKBENCH_BLOCK))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BUILDERS_WORKBENCH_BLOCK)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                                                .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                                        )
                                )
                                .when(ExplosionCondition.survivesExplosion())
                        )
        );

        this.add(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK,
                LootTable.lootTable() // Use the static factory method to start the builder
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BLACKSMITHS_WORKBENCH_BLOCK)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                                                .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                                        )
                                )
                                .when(ExplosionCondition.survivesExplosion())
                        )
        );

        this.add(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK, 
            LootTable.lootTable() // Use the static factory method to start the builder
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                            .hasProperty(AbstractWorkbench.TYPE, ChestType.SINGLE)
                        )
                    )
                    .when(ExplosionCondition.survivesExplosion())
                )
        );

        this.add(ModBlocks.WORKBENCH_WORKBENCH_BLOCK, 
            LootTable.lootTable() // Use the static factory method to start the builder
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ModBlocks.WORKBENCH_WORKBENCH_BLOCK))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.WORKBENCH_WORKBENCH_BLOCK)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                            .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                        )
                    )
                    .when(ExplosionCondition.survivesExplosion())
                )
        );

        this.add(ModBlocks.FARMERS_WORKBENCH_BLOCK,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModBlocks.FARMERS_WORKBENCH_BLOCK))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.FARMERS_WORKBENCH_BLOCK)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                                                .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                                        )
                                )
                                .when(ExplosionCondition.survivesExplosion())
                        )
        );

        this.add(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1, 
            LootTable.lootTable() // Use the static factory method to start the builder
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                            .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                        )
                    )
                    .when(ExplosionCondition.survivesExplosion())
                )
        );

        this.add(ModBlocks.FURNACE_WORKBENCH_BLOCK_T2, 
            LootTable.lootTable() // Use the static factory method to start the builder
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ModBlocks.FURNACE_WORKBENCH_BLOCK_T2))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.FURNACE_WORKBENCH_BLOCK_T2)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AbstractWorkbench.HALF, DoubleBlockHalf.LOWER)
                            .hasProperty(AbstractWorkbench.TYPE, ChestType.LEFT)
                        )
                    )
                    .when(ExplosionCondition.survivesExplosion())
                )
        );
    }


/// For Ore Drops
/**
     * Create a loot table builder that drops the specified item in multiple quantities with Silk Touch, Fortune bonus, and explosion decay applied.
     *
     * @param drop  the source block used for Silk Touch dispatch and explosion-decay context
     * @param item  the item to drop from the ore
     * @return      a LootTable.Builder that drops `item` in a base count between 2 and 5, augmented by the Fortune enchantment, with Silk Touch handling and explosion decay applied
     */
    public LootTable.Builder AverageOreDrops(Block drop, Item item) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(drop, this.applyExplosionDecay(drop, ((LootPoolSingletonContainer.Builder<?>)
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))));
    }

    /**
     * Creates a loot table builder for an ore that yields the specified item with Silk Touch and Fortune handling.
     *
     * The table gives a base drop count of exactly 1 (before Fortune), increases the count with Fortune, returns the
     * ore block when mined with Silk Touch, and applies explosion decay to the drop.
     *
     * @param drop  the ore block (returned when Silk Touch is used)
     * @param item  the item to drop when the ore is mined without Silk Touch
     * @return      a LootTable.Builder configured to drop the specified item with Fortune bonuses, Silk Touch dispatch,
     *              and explosion decay
     */
    public LootTable.Builder SingleOreDrops(Block drop, Item item) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(drop, this.applyExplosionDecay(drop, ((LootPoolSingletonContainer.Builder<?>)
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))));
    }

    /**
     * Builds a loot table for a "light" ore block that supports Silk Touch, Fortune bonuses, and explosion decay.
     *
     * @param drop  the ore block whose loot table is being created
     * @param item  the item to drop from the ore when not Silk Touched
     * @return      a LootTable.Builder that drops {@code item} in quantities of 1–2 (before Fortune), applies Fortune bonus,
     *              dispatches to Silk Touch drops when applicable, and respects explosion decay
     */
    public LootTable.Builder LightOreDrops(Block drop, Item item) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(drop, this.applyExplosionDecay(drop, ((LootPoolSingletonContainer.Builder<?>)
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}