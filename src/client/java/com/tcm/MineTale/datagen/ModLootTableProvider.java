package com.tcm.MineTale.datagen;

import com.tcm.MineTale.registry.ModBlocks;
import com.tcm.MineTale.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
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
        //Bug!! Workbenches currently drop items based on the amount of blocks
        //the multiblock is. For example the Furnaces currently drop 4 furnaces.
        dropSelf(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1);
        dropSelf(ModBlocks.FURNACE_WORKBENCH_BLOCK_T1);
        dropSelf(ModBlocks.ARMORERS_WORKBENCH_BLOCK);
        dropSelf(ModBlocks.CAMPFIRE_WORKBENCH_BLOCK);
        dropSelf(ModBlocks.WORKBENCH_WORKBENCH_BLOCK);
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
