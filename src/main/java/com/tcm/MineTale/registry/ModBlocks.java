package com.tcm.MineTale.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.ArmorersWorkbench;
import com.tcm.MineTale.block.workbenches.CampfireWorkbench;
import com.tcm.MineTale.block.workbenches.FurnaceWorkbench;
import com.tcm.MineTale.block.workbenches.WorkbenchWorkbench;
import com.tcm.MineTale.item.ModCreativeTab;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.Blocks;

import static net.minecraft.world.level.block.Blocks.logProperties;

public class ModBlocks {

	private static final List<Block> REGISTERED_BLOCKS = new ArrayList<>();

	//Workbenches
	public static final Block CAMPFIRE_WORKBENCH_BLOCK = register(
		"campfire_workbench_block", 
		CampfireWorkbench::new, 
		BlockBehaviour.Properties.of().sound(SoundType.WOOD), 
		true
	);

	public static final Block WORKBENCH_WORKBENCH_BLOCK = register(
		"workbench_workbench_block",
		WorkbenchWorkbench::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOD), 
		true
	);

	public static final Block ARMORERS_WORKBENCH_BLOCK = register(
		"armorers_workbench_block",
		ArmorersWorkbench::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOD),
		true
	);

	public static final Block FURNACE_WORKBENCH_BLOCK_T1 = register(
		"furnace_workbench_block_t1",
		(props) -> new FurnaceWorkbench(props, ModTiers.TIER_1),
		BlockBehaviour.Properties.of().sound(SoundType.STONE).noOcclusion(),
		true
	);

	public static final Block FURNACE_WORKBENCH_BLOCK_T2 = register(
		"furnace_workbench_block_t2",
		(props) -> new FurnaceWorkbench(props, ModTiers.TIER_2),
		BlockBehaviour.Properties.of().sound(SoundType.STONE).noOcclusion(),
		true
	);

	//Logs
	public static final Block AMBER_LOG = register("amber_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_ORANGE, MapColor.PODZOL, SoundType.WOOD), true);
	public static final Block BAMBOO_LOG = register("bamboo_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_GREEN, MapColor.GRASS, SoundType.WOOD), true);
	public static final Block BAMBOO_LOG_DECO = register("bamboo_log_deco", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BROWN, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block BANYAN_LOG = register("banyan_log", RotatedPillarBlock::new, logProperties(MapColor.DIRT, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block BEECH_LOG = register("beech_log", RotatedPillarBlock::new, logProperties(MapColor.DIRT, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block BOTTLETREE_LOG = register("bottletree_log", RotatedPillarBlock::new, logProperties(MapColor.QUARTZ, MapColor.QUARTZ, SoundType.WOOD), true);
	public static final Block BURNT_LOG = register("burnt_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BLACK, MapColor.COLOR_BLACK, SoundType.WOOD), true);
	public static final Block CAMPHOR_LOG = register("camphor_log", RotatedPillarBlock::new, logProperties(MapColor.QUARTZ, MapColor.STONE, SoundType.WOOD), true);
	public static final Block CRYSTALWOOD_LOG = register("crystalwood_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_PINK, MapColor.COLOR_BLACK, SoundType.WOOD), true);
	public static final Block FIRE_LOG = register("fire_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_RED, MapColor.COLOR_RED, SoundType.WOOD), true);
	public static final Block GUMBOAB_LOG = register("gumboab_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.STONE, SoundType.WOOD), true);
	public static final Block ICE_LOG = register("ice_log", RotatedPillarBlock::new, logProperties(MapColor.ICE, MapColor.ICE, SoundType.WOOD), true);
	public static final Block MAPLE_LOG = register("maple_log", RotatedPillarBlock::new, logProperties(MapColor.TERRACOTTA_PINK, MapColor.TERRACOTTA_PINK, SoundType.WOOD), true);
	public static final Block PALM_TREE_LOG = register("palm_tree_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_ORANGE, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block PALO_LOG = register("palo_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.SAND, SoundType.WOOD), true);
	public static final Block POISONED_LOG = register("poisoned_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_GREEN, MapColor.COLOR_BLACK, SoundType.WOOD), true);
	public static final Block REDWOOD_LOG = register("redwood_log", RotatedPillarBlock::new, logProperties(MapColor.COLOR_BROWN, MapColor.COLOR_BROWN, SoundType.WOOD), true);
	public static final Block SALLOW_LOG = register("sallow_log", RotatedPillarBlock::new, logProperties(MapColor.DIRT, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block SPIRAL_LOG = register("spiral_log", RotatedPillarBlock::new, logProperties(MapColor.STONE, MapColor.STONE, SoundType.WOOD), true);
	public static final Block STORMBARK_LOG = register("stormbark_log", RotatedPillarBlock::new, logProperties(MapColor.ICE, MapColor.TERRACOTTA_LIGHT_GREEN, SoundType.WOOD), true);
	public static final Block STRIPPED_LOG = register("stripped_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.SAND, SoundType.WOOD), true);
	public static final Block WINDWILLOW_LOG = register("windwillow_log", RotatedPillarBlock::new, logProperties(MapColor.ICE, MapColor.SAND, SoundType.WOOD), true);
	public static final Block WILD_WISTERIA_LOG = register("wild_wisteria_log", RotatedPillarBlock::new, logProperties(MapColor.SAND, MapColor.DIRT, SoundType.WOOD), true);
	public static final Block WILD_WISTERIA_WOOD = register("wild_wisteria_wood", RotatedPillarBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava(), true);

	// Ores

	/// 1. COPPER
	public static final Block COPPER_ORE_BASALT = registerOreBlock("copper_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block COPPER_ORE_VOLCANIC = registerOreBlock("copper_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block COPPER_ORE_SHALE = registerOreBlock("copper_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block COPPER_ORE_SANDSTONE = registerOreBlock("copper_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 2. ONYXIUM
	public static final Block ONYXIUM_ORE_BASALT = registerOreBlock("onyxium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block ONYXIUM_ORE_VOLCANIC = registerOreBlock("onyxium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block ONYXIUM_ORE_SHALE = registerOreBlock("onyxium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block ONYXIUM_ORE_STONE = registerOreBlock("onyxium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	public static final Block ONYXIUM_ORE_SANDSTONE = registerOreBlock("onyxium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 3. THORIUM
	public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);
	public static final Block THORIUM_ORE_DRY_MUD = registerOreBlock("thorium_ore_dry_mud", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 4. ADAMANTITE
	public static final Block ADAMANTITE_ORE_BASALT = registerOreBlock("adamantite_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block ADAMANTITE_ORE_VOLCANIC = registerOreBlock("adamantite_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block ADAMANTITE_ORE_SHALE = registerOreBlock("adamantite_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block ADAMANTITE_ORE_STONE = registerOreBlock("adamantite_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	public static final Block ADAMANTITE_ORE_SLATE = registerOreBlock("adamantite_ore_slate", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);
	public static final Block ADAMANTITE_ORE_MAGMA = registerOreBlock("adamantite_ore_magma", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 5. MITHRIL
	public static final Block MITHRIL_ORE_BASALT = registerOreBlock("mithril_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block MITHRIL_ORE_VOLCANIC = registerOreBlock("mithril_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block MITHRIL_ORE_SLATE = registerOreBlock("mithril_ore_slate", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block MITHRIL_ORE_STONE = registerOreBlock("mithril_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	public static final Block MITHRIL_ORE_MAGMA = registerOreBlock("mithril_ore_magma", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 6. COBALT
	public static final Block COBALT_ORE_BASALT = registerOreBlock("cobalt_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block COBALT_ORE_VOLCANIC = registerOreBlock("cobalt_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block COBALT_ORE_SHALE = registerOreBlock("cobalt_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block COBALT_ORE_STONE = registerOreBlock("cobalt_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	public static final Block COBALT_ORE_SANDSTONE = registerOreBlock("cobalt_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);
	public static final Block COBALT_ORE_SLATE = registerOreBlock("cobalt_ore_slate", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 7. IRON
	public static final Block IRON_ORE_BASALT = registerOreBlock("iron_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block IRON_ORE_VOLCANIC = registerOreBlock("iron_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block IRON_ORE_SHALE = registerOreBlock("iron_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block IRON_ORE_SANDSTONE = registerOreBlock("iron_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);
	public static final Block IRON_ORE_SLATE = registerOreBlock("iron_ore_slate", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 8. GOLD
	public static final Block GOLD_ORE_BASALT = registerOreBlock("gold_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block GOLD_ORE_VOLCANIC = registerOreBlock("gold_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block GOLD_ORE_SHALE = registerOreBlock("gold_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block GOLD_ORE_SANDSTONE = registerOreBlock("gold_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);
	public static final Block GOLD_ORE_CALCITE = registerOreBlock("gold_ore_calcite", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 9. RUBY - not implemented in hytale
	// public static final Block RUBY_ORE_BASALT = registerOreBlock("ruby_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block RUBY_ORE_VOLCANIC = registerOreBlock("ruby_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block RUBY_ORE_SHALE = registerOreBlock("ruby_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block RUBY_ORE_STONE = registerOreBlock("ruby_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block RUBY_ORE_SANDSTONE = registerOreBlock("ruby_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 10. PRISMA - no implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 11. SILVER
	public static final Block SILVER_ORE_BASALT = registerOreBlock("silver_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block SILVER_ORE_VOLCANIC = registerOreBlock("silver_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block SILVER_ORE_SHALE = registerOreBlock("silver_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	public static final Block SILVER_ORE_STONE = registerOreBlock("silver_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	public static final Block SILVER_ORE_SANDSTONE = registerOreBlock("silver_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);
	public static final Block SILVER_ORE_SLATE = registerOreBlock("silver_ore_slate", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 12. SAPPHIRE  - not implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 13. TOPAZ - not implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 14. ZEPHYR - not implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 15. DIAMOND - not implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 16. VOIDSTONE - not implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

	/// 17. EMERALD - not implemented in hytale
	// public static final Block THORIUM_ORE_BASALT = registerOreBlock("thorium_ore_basalt", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_VOLCANIC = registerOreBlock("thorium_ore_volcanic", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SHALE = registerOreBlock("thorium_ore_shale", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.BASALT, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_STONE = registerOreBlock("thorium_ore_stone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.STONE, Items.COPPER_ORE, 1);
	// public static final Block THORIUM_ORE_SANDSTONE = registerOreBlock("thorium_ore_sandstone", Block::new, BlockBehaviour.Properties.of().strength(2).requiresCorrectToolForDrops(), Blocks.SANDSTONE, Items.COPPER_ORE, 1);

    /**
     * Adds the mod's workbench and furnace blocks to the Functional Blocks creative tab.
     *
     * Registers CAMPFIRE_WORKBENCH_BLOCK, WORKBENCH_WORKBENCH_BLOCK, FURNACE_WORKBENCH_BLOCK_T1,
     * and FURNACE_WORKBENCH_BLOCK_T2 to CreativeModeTabs.FUNCTIONAL_BLOCKS and prints a registration
     * message containing the mod ID.
     */
    public static void initialize() { 
		ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.MINETALE_CREATIVE_TAB_KEY).register(entries -> {
            REGISTERED_BLOCKS.forEach(entries::accept);
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

		REGISTERED_BLOCKS.add(block);

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	private static Block registerOreBlock(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, Block baseBlock, Item itemToDrop, int count) {
		ResourceKey<Block> blockKey = keyOfBlock(name);
		
		Block block = blockFactory.apply(settings.requiresCorrectToolForDrops().setId(blockKey));

		ResourceKey<Item> itemKey = keyOfItem(name);

		BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
		Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

		REGISTERED_BLOCKS.add(block);

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

    private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
	}

	private static ResourceKey<Item> keyOfItem(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
	}
}