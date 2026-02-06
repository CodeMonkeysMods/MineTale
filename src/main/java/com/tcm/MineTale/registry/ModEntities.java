package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;

public class ModEntities {

    /**
     * Writes a registration message to standard output identifying the mod by its ID.
     *
     * The message printed is "Registered Mod Entities for " followed by MineTale.MOD_ID.
     */
    public static void initialize() {
        System.out.println("Registered Mod Entities for " + MineTale.MOD_ID);
    }
    
    // private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
    //     // Create the key
    //     ResourceKey<EntityType<?>> entityKey = ResourceKey.create(
    //             Registries.ENTITY_TYPE,
    //             Identifier.fromNamespaceAndPath(MineTale.MOD_ID, name)
    //     );

    //     // Build the type using the key
    //     EntityType<T> type = builder.build(entityKey);

    //     // Register and return
    //     return Registry.register(BuiltInRegistries.ENTITY_TYPE, entityKey, type);
    // }
}