package com.tcm.MineTale.registry;

import java.util.function.Function;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.CampfireWorkbench;
import com.tcm.MineTale.block.workbenches.FurnaceWorkbench;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {

	public static final Block CAMPFIRE_WORKBENCH_BLOCK = register(
		"campfire_workbench_block", 
		CampfireWorkbench::new, 
		BlockBehaviour.Properties.of().sound(SoundType.WOOD), 
		true
	);

	public static final Block FURNACE_WORKBENCH_BLOCK_T1 = register(
		"furnace_workbench_block_t1",
		(props) -> new FurnaceWorkbench(props, ModTiers.TIER_1),
		BlockBehaviour.Properties.of().sound(SoundType.WOOD),
		true
	);

	public static final Block FURNACE_WORKBENCH_BLOCK_T2 = register(
		"furnace_workbench_block_t2",
		(props) -> new FurnaceWorkbench(props, ModTiers.TIER_2),
		BlockBehaviour.Properties.of().sound(SoundType.WOOD),
		true
	);
    
    /**
     * Registers this mod's blocks into the Functional Blocks creative tab and records the registration.
     *
     * Adds CAMPFIRE_WORKBENCH_BLOCK and FURNACE_WORKBENCH_BLOCK to CreativeModeTabs.FUNCTIONAL_BLOCKS and prints a registration message including the mod ID.
     */
    public static void initialize() { 
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
        	entries.accept(CAMPFIRE_WORKBENCH_BLOCK);
			entries.accept(FURNACE_WORKBENCH_BLOCK_T1);
			entries.accept(FURNACE_WORKBENCH_BLOCK_T2);
    	});
		

        System.out.println("Registered Mod Blocks for " + MineTale.MOD_ID);
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
		// Create a registry key for the block
		ResourceKey<Block> blockKey = keyOfBlock(name);
		// Create the block instance
		Block block = blockFactory.apply(settings.setId(blockKey));

		// Sometimes, you may not want to register an item for the block.
		// Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
		if (shouldRegisterItem) {
			// Items need to be registered with a different type of registry key, but the ID
			// can be the same.
			ResourceKey<Item> itemKey = keyOfItem(name);

			BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
			Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
		}

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

    private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
	}

	private static ResourceKey<Item> keyOfItem(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
	}
}