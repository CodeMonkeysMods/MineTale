package com.tcm.MineTale.item;

import com.tcm.MineTale.MineTale;
import com.tcm.MineTale.util.ModTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.EnumMap;

public class ModArmorMaterials {
    static ResourceKey<? extends Registry<EquipmentAsset>> RESOURCE_KEY =
            ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<EquipmentAsset> WOOD_KEY = ResourceKey.create(RESOURCE_KEY, Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "wood"));

    public static final ArmorMaterial WOOD_MATERIAL = new ArmorMaterial(200, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.HELMET, 1);
        map.put(ArmorType.CHESTPLATE, 1);
        map.put(ArmorType.LEGGINGS, 1);
        map.put(ArmorType.BOOTS, 1); //Change to gauntlets when possible
    }), 5, SoundEvents.ARMOR_EQUIP_GENERIC, 1, 0, ModTags.Items.WOOD_REPAIR, WOOD_KEY);
}
