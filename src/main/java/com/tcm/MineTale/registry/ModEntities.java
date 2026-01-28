package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class ModEntities {

    public static void initialize() {
        System.out.println("Registered Mod Entities for " + MineTale.MOD_ID);
    }
    
    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        // Create the key
        ResourceKey<EntityType<?>> entityKey = ResourceKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name)
        );

        // Build the type using the key
        EntityType<T> type = builder.build(entityKey);

        // Register and return
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, entityKey, type);
    }
}
