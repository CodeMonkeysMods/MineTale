package com.tcm.MineTale.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.block.workbenches.ArmorersWorkbench;
import com.tcm.MineTale.block.workbenches.BuildersWorkbench;
import com.tcm.MineTale.block.workbenches.CampfireWorkbench;
import com.tcm.MineTale.block.workbenches.FarmersWorkbench;
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

import static net.minecraft.world.level.block.Blocks.litBlockEmission;
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
		BlockBehaviour.Properties.of().sound(SoundType.STONE).noOcclusion()
				.requiresCorrectToolForDrops().strength(3.5F)
				.lightLevel(litBlockEmission(13)),
			true
	);

	public static final Block FURNACE_WORKBENCH_BLOCK_T2 = register(
		"furnace_workbench_block_t2",
		(props) -> new FurnaceWorkbench(props, ModTiers.TIER_2),
		BlockBehaviour.Properties.of().sound(SoundType.STONE).noOcclusion()
				.requiresCorrectToolForDrops().strength(3.5F)
				.lightLevel(litBlockEmission(13)),
		true
	);

	public static final Block FARMERS_WORKBENCH_BLOCK = register(
		"farmers_workbench",
		FarmersWorkbench::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOD),
		true
	);

	public static final Block BUILDERS_WORKBENCH_BLOCK = register(
		"builders_workbench",
		BuildersWorkbench::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOD),
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

	// Base Moss (The raw materials used in recipes)
    public static final Block MOSS = register("moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block BLUE_MOSS = register("blue_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block RED_MOSS = register("red_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block YELLOW_MOSS = register("yellow_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block DARK_GREEN_MOSS = register("dark_green_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);

	// World Blocks (Natural Soils and Terrains)
    public static final Block DRY_MUD = register("dry_mud", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MUD), true);
    public static final Block LEAFY_SOIL = register("leafy_soil", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block SOIL_PATHWAY = register("soil_pathway", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block NEEDLED_SOIL = register("needled_soil", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block FULL_GRASS = register("full_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block COLD_DIRT = register("cold_dirt", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block SUMMER_GRASS = register("summer_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block WET_GRASS = register("wet_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block DRY_GRASS = register("dry_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block DRY_DIRT = register("dry_dirt", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block DEEP_GRASS = register("deep_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block POISONED_DIRT = register("poisoned_dirt", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);

    // Moss Blocks
    public static final Block BLUE_MOSS_BLOCK = register("blue_moss_block", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block DARK_GREEN_MOSS_BLOCK = register("dark_green_moss_block", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block RED_MOSS_BLOCK = register("red_moss_block", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block YELLOW_MOSS_BLOCK = register("yellow_moss_block", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block GREEN_MOSS_BLOCK = register("green_moss_block", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);

    // Moss Decoration (Rugs & Hanging)
    public static final Block GREEN_MOSS_RUG = register("green_moss_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block BLUE_MOSS_RUG = register("blue_moss_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block DARK_GREEN_MOSS_RUG = register("dark_green_moss_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block YELLOW_MOSS_RUG = register("yellow_moss_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block RED_MOSS_RUG = register("red_moss_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block SORREL_RUG = register("sorrel_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block VINE_RUG = register("vine_rug", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);

    public static final Block RED_HANGING_MOSS = register("red_hanging_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block DARK_GREEN_HANGING_MOSS = register("dark_green_hanging_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block BLUE_HANGING_MOSS = register("blue_hanging_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block YELLOW_HANGING_MOSS = register("yellow_hanging_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);
    public static final Block GREEN_HANGING_MOSS = register("green_hanging_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.MOSS), true);

    // Short Moss & Plants
    public static final Block SHORT_RED_MOSS = register("short_red_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block SHORT_YELLOW_MOSS = register("short_yellow_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block SHORT_BLUE_MOSS = register("short_blue_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block SHORT_DARK_GREEN_MOSS = register("short_dark_green_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block SHORT_MOSS = register("short_moss", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);

    // Vines & Ivy
    public static final Block LIANA = register("liana", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block VINE = register("vine", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block DRY_VINE = register("dry_vine", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block POISONED_IVY = register("poisoned_ivy", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block WALL_IVY = register("wall_ivy", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block IVY = register("ivy", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);

    // Specialized Grasses
    public static final Block COLD_GRASS = register("cold_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
    public static final Block BURNT_GRASS = register("burnt_grass", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);

    // Functional / Crafted
    public static final Block BAMBOO_PLANTER = register("bamboo_planter", Block::new, BlockBehaviour.Properties.of().sound(SoundType.WOOD), true);

	// Decorational
	public static final Block ROPE = register("rope", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);
	public static final Block ROPE_DIAGONAL = register("rope_diagonal", Block::new, BlockBehaviour.Properties.of().sound(SoundType.GRASS), true);

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
     * Adds all mod-registered blocks to the MineTale creative tab and logs the action.
     *
     * Subscribes an entries-modification handler for ModCreativeTab.MINETALE_CREATIVE_TAB_KEY that
     * inserts every block collected in REGISTERED_BLOCKS into the creative tab, then prints a
     * registration message that includes the mod ID.
     */
    public static void initialize() { 
		ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.MINETALE_CREATIVE_TAB_KEY).register(entries -> {
            REGISTERED_BLOCKS.forEach(entries::accept);
        });
		

        System.out.println("Registered Mod Blocks for " + MineTale.MOD_ID);
    }

    /**
	 * Register a block under the mod's namespace, optionally create and register its corresponding BlockItem,
	 * and add the block to the internal list of registered blocks.
	 *
	 * @param name              the registry name (path) to use for the block and item
	 * @param blockFactory      factory that creates the Block from provided BlockBehaviour.Properties
	 * @param settings          the BlockBehaviour.Properties to apply to the block; this method will set the block's registry ID on it
	 * @param shouldRegisterItem if `true`, a BlockItem for the block will be created and registered with the same name
	 * @return                  the registered Block instance
	 */
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

	/**
	 * Creates and registers an ore block and its corresponding BlockItem in the mod registries.
	 *
	 * The created block is configured to require the correct tool for drops and is added to the
	 * internal REGISTERED_BLOCKS list. A BlockItem for the block is also created and registered
	 * under the same name.
	 *
	 * @param name the registry name (path) for the block and its item
	 * @param blockFactory a factory that produces the Block given BlockBehaviour.Properties
	 * @param settings base block properties to apply to the created block
	 * @param baseBlock an existing block used as the ore's base reference (semantic association)
	 * @param itemToDrop the item that the ore is intended to drop when mined
	 * @param count the number of items the ore is intended to drop
	 * @return the registered Block instance
	 */
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