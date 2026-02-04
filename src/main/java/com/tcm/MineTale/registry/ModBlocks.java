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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import static net.minecraft.world.level.block.Blocks.logProperties;

public class ModBlocks {

	//Workbenches
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

	//Logs
	public static final Block AMBER_LOG = register("amber_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_ORANGE, MapColor.PODZOL, SoundType.WOOD), true);
	public static final Block ASH_LOG = register("ash_log", RotatedPillarBlock::new, logProperties(MapColor.WOOD, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block ASPEN_LOG = register("aspen_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.SAND, SoundType.WOOD), true);
	public static final Block AZURE_LOG = register("azure_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BLUE, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block BAMBOO_LOG = register("bamboo_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_GREEN, MapColor.GRASS, SoundType.WOOD), true);
	public static final Block BAMBOO_LOG_DECO = register("bamboo_log_deco", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BROWN, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block BANYAN_LOG = register("banyan_log", RotatedPillarBlock::new, logProperties(MapColor.DIRT, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block BEECH_LOG = register("beech_log", RotatedPillarBlock::new, logProperties(MapColor.DIRT, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block BOTTLETREE_LOG = register("bottletree_log", RotatedPillarBlock::new, logProperties(MapColor.QUARTZ, MapColor.QUARTZ, SoundType.WOOD), true);
	public static final Block BURNT_LOG = register("burnt_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BLACK, MapColor.COLOR_BLACK, SoundType.WOOD), true);
	public static final Block CAMPHOR_LOG = register("camphor_log", RotatedPillarBlock::new, logProperties(MapColor.QUARTZ, MapColor.STONE, SoundType.WOOD), true);
	public static final Block CEDAR_LOG = register("cedar_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BROWN, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block CRYSTALWOOD_LOG = register("crystalwood_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_PINK, MapColor.COLOR_BLACK, SoundType.WOOD), true);
	public static final Block DRY_LOG = register("dry_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.SAND, SoundType.WOOD), true);
	public static final Block BLUE_FIG_LOG = register("blue_fig_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_RED, MapColor.COLOR_RED, SoundType.WOOD), true);
	public static final Block FIRE_LOG = register("fire_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_RED, MapColor.COLOR_RED, SoundType.WOOD), true);
	public static final Block GUMBOAB_LOG = register("gumboab_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.STONE, SoundType.WOOD), true);
	public static final Block ICE_LOG = register("ice_log", RotatedPillarBlock::new, logProperties(MapColor.ICE, MapColor.ICE, SoundType.WOOD), true);
	public static final Block MAPLE_LOG = register("maple_log", RotatedPillarBlock::new, logProperties(MapColor.TERRACOTTA_PINK, MapColor.TERRACOTTA_PINK, SoundType.WOOD), true);
	public static final Block PALM_TREE_LOG = register("palm_tree_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_ORANGE, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block PALO_LOG = register("palo_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.SAND, SoundType.WOOD), true);
	public static final Block PETRIFIED_LOG = register("petrified_log", RotatedPillarBlock::new, logProperties(MapColor.STONE, MapColor.STONE, SoundType.WOOD), true);
	public static final Block POISONED_LOG = register("poisoned_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_GREEN, MapColor.COLOR_BLACK, SoundType.WOOD), true);
	public static final Block REDWOOD_LOG = register("redwood_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BROWN, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block SALLOW_LOG = register("sallow_log", RotatedPillarBlock::new, logProperties(MapColor.DIRT, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block SPIRAL_LOG = register("spiral_log", RotatedPillarBlock::new, logProperties(MapColor.STONE, MapColor.STONE, SoundType.WOOD), true);
	public static final Block STORMBARK_LOG = register("stormbark_log", RotatedPillarBlock::new, logProperties(MapColor.ICE, MapColor.TERRACOTTA_LIGHT_GREEN, SoundType.WOOD), true);
	public static final Block STRIPPED_LOG = register("stripped_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.SAND, SoundType.WOOD), true);
	public static final Block WINDWILLOW_LOG = register("windwillow_log", RotatedPillarBlock::new, logProperties(MapColor.ICE, MapColor.SAND, SoundType.WOOD), true);
	public static final Block WILD_WISTERIA_LOG = register("wild_wisteria_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block WILD_WISTERIA_WOOD = register("wild_wisteria_wood", RotatedPillarBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava(), true);

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