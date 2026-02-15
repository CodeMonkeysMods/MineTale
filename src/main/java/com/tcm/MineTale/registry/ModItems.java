package com.tcm.MineTale.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.item.ModCreativeTab;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class ModItems {

    private static final List<Item> REGISTERED_ITEMS = new ArrayList<>();

    public static void initialize() {
        System.out.println("Registered Mod Items for " + MineTale.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.MINETALE_CREATIVE_TAB_KEY).register(entries -> {
            REGISTERED_ITEMS.forEach(entries::accept);
        });
    }

    // --- NATURAL MATERIALS & GATHERABLES ---
    public static final Item PLANT_FIBER = register("plant_fiber", Item::new, new Item.Properties());
    public static final Item TREE_SAP = register("tree_sap", Item::new, new Item.Properties());
    public static final Item SAP_GLOB = register("sap_glob", Item::new, new Item.Properties());
    public static final Item RUBBLE = register("rubble", Item::new, new Item.Properties());
    public static final Item TREE_BARK = register("tree_bark", Item::new, new Item.Properties());
    public static final Item MOSS = register("moss", Item::new, new Item.Properties());
    public static final Item BLUE_CRYSTAL_SHARDS = register("blue_crystal_shards", Item::new, new Item.Properties());
    public static final Item GREEN_CRYSTAL_SHARDS = register("green_crystal_shards", Item::new, new Item.Properties());
    public static final Item YELLOW_CRYSTAL_SHARDS = register("yellow_crystal_shards", Item::new, new Item.Properties());

    // --- MINERALS & REFINED METALS (Unique to Hytale) ---
    public static final Item THORIUM_INGOT = register("thorium_ingot", Item::new, new Item.Properties());
    public static final Item COBALT_INGOT = register("cobalt_ingot", Item::new, new Item.Properties());
    public static final Item ADAMANTITE_INGOT = register("adamantite_ingot", Item::new, new Item.Properties());
    public static final Item MITHRIL_INGOT = register("mithril_ingot", Item::new, new Item.Properties());
    public static final Item BRONZE_INGOT = register("bronze_ingot", Item::new, new Item.Properties());
    public static final Item STEEL_INGOT = register("steel_ingot", Item::new, new Item.Properties());

    // --- MOB DROPS, HIDES & LEATHERS ---
    public static final Item LIGHT_HIDE = register("light_hide", Item::new, new Item.Properties());
    public static final Item MEDIUM_HIDE = register("medium_hide", Item::new, new Item.Properties());
    public static final Item HEAVY_HIDE = register("heavy_hide", Item::new, new Item.Properties());
    public static final Item SOFT_HIDE = register("soft_hide", Item::new, new Item.Properties());
    public static final Item PRISMATIC_HIDE = register("prismatic_hide", Item::new, new Item.Properties());
    
    public static final Item LIGHT_LEATHER = register("light_leather", Item::new, new Item.Properties());
    public static final Item MEDIUM_LEATHER = register("medium_leather", Item::new, new Item.Properties());
    public static final Item HEAVY_LEATHER = register("heavy_leather", Item::new, new Item.Properties());
    public static final Item STORM_LEATHER = register("storm_leather", Item::new, new Item.Properties());
    public static final Item PRISMATIC_LEATHER = register("prismatic_leather", Item::new, new Item.Properties());
    
    public static final Item FERAN_RIB = register("feran_rib", Item::new, new Item.Properties());
    public static final Item STURDY_CHITIN = register("sturdy_chitin", Item::new, new Item.Properties());
    public static final Item VENOM_SAC = register("venom_sac", Item::new, new Item.Properties());
    public static final Item BONE_FRAGMENT = register("bone_fragment", Item::new, new Item.Properties());

    // --- FABRICS & TEXTILES ---
    public static final Item LINEN_SCRAPS = register("linen_scraps", Item::new, new Item.Properties());
    public static final Item BOLT_OF_LINEN = register("bolt_of_linen", Item::new, new Item.Properties());
    public static final Item SHADOWEAVE_SCRAPS = register("shadoweave_scraps", Item::new, new Item.Properties());
    public static final Item CINDERCLOTH_SCRAPS = register("cindercloth_scraps", Item::new, new Item.Properties());
    public static final Item BOLT_OF_WOOL = register("bolt_of_wool", Item::new, new Item.Properties());
    public static final Item YELLOW_CLOTH = register("yellow_cloth", Item::new, new Item.Properties());

    // --- SEEDS & FARMING (Bags and Bulbs) ---
    public static final Item LETTUCE = register("lettuce", Item::new, new Item.Properties().food(
        new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build()
    ));
    public static final Item CHILLI_SEED_BAG = register("chilli_seed_bag", Item::new, new Item.Properties());
    public static final Item CHILLI_SEED_BAG_ETERNAL = register("chilli_seed_bag_eternal", Item::new, new Item.Properties());
    public static final Item SUNFLOWER_SEED_BAG = register("sunflower_seed_bag", Item::new, new Item.Properties());
    public static final Item CORN_SEED_BAG = register("corn_seed_bag", Item::new, new Item.Properties());
    public static final Item COTTON_SEED_BAG = register("cotton_seed_bag", Item::new, new Item.Properties());
    public static final Item RICE_SEED_BAG = register("rice_seed_bag", Item::new, new Item.Properties());
    public static final Item ONION_BULB = register("onion_bulb", Item::new, new Item.Properties());

    // --- MAGICAL & ALCHEMICAL ---
    public static final Item ESSENCE_OF_LIFE = register("essence_of_life", Item::new, new Item.Properties());
    public static final Item ESSENCE_OF_FIRE = register("essence_of_fire", Item::new, new Item.Properties());
    public static final Item ESSENCE_OF_ICE = register("essence_of_ice", Item::new, new Item.Properties());
    public static final Item ESSENCE_OF_THE_VOID = register("essence_of_the_void", Item::new, new Item.Properties());
    public static final Item VOID_HEART = register("void_heart", Item::new, new Item.Properties());

    // --- FLORA COMPONENTS (Non-placeable petals) ---
    public static final Item RED_PETALS = register("red_petals", Item::new, new Item.Properties());
    public static final Item YELLOW_PETALS = register("yellow_petals", Item::new, new Item.Properties());
    public static final Item GREEN_PETALS = register("green_petals", Item::new, new Item.Properties());
    public static final Item WHITE_PETALS = register("white_petals", Item::new, new Item.Properties());
    public static final Item AZURE_PETALS = register("azure_petals", Item::new, new Item.Properties());
    public static final Item STORM_PETALS = register("storm_petals", Item::new, new Item.Properties());
    public static final Item BLOOD_PETALS = register("blood_petals", Item::new, new Item.Properties());
    public static final Item CYAN_PETALS = register("cyan_petals", Item::new, new Item.Properties());

    // --- REGISTRATION LOGIC ---
    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        // 4. ADD TO CREATIVE TAB AUTOMATICALLY
        REGISTERED_ITEMS.add(item);
        
        return item;
    }
}
