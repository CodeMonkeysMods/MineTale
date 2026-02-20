package com.tcm.MineTale.datagen;

import com.tcm.MineTale.block.workbenches.AbstractWorkbench;
import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModItems;
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
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

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
/** Ex:
 *  add(ModBlocks.IRON_ORE_SHALE, AverageOreDrops(ModBlocks.IRON_ORE_SHALE, Items.RAW_IRON));
 * **/
    public LootTable.Builder AverageOreDrops(Block drop, Item item) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(drop, this.applyExplosionDecay(drop, ((LootPoolSingletonContainer.Builder<?>)
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))));
    }

    public LootTable.Builder SingleOreDrops(Block drop, Item item) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(drop, this.applyExplosionDecay(drop, ((LootPoolSingletonContainer.Builder<?>)
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))));
    }

    public LootTable.Builder LightOreDrops(Block drop, Item item) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(drop, this.applyExplosionDecay(drop, ((LootPoolSingletonContainer.Builder<?>)
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}
