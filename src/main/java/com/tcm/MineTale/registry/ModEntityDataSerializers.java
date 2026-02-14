package com.tcm.MineTale.registry;

public class ModEntityDataSerializers {

    /**
     * Entry point to register the mod's entity data serializers.
     *
     * <p>When implemented, this method should register any custom EntityDataSerializer instances
     * required by the mod. Currently no serializers are registered.</p>
     */
    public static void initialize() {

    }
    
    // private static <T> EntityDataSerializer<T> register(String id, StreamCodec<? super ByteBuf, T> streamCodec) {
    //     EntityDataSerializer<T> serializer = EntityDataSerializer.forValueType(streamCodec);
    //     FabricTrackedDataRegistry.register(Identifier.fromNamespaceAndPath(MineTale.MOD_ID, id), serializer);
    //     return serializer;
    // }

    
}