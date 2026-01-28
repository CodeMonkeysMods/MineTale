package com.tcm.MineTale.registry;

import com.tcm.MineTale.MineTale;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public class ModEntityDataSerializers {

    public static void initialize() {

    }
    
    private static <T> EntityDataSerializer<T> register(String id, StreamCodec<? super ByteBuf, T> streamCodec) {
        EntityDataSerializer<T> serializer = EntityDataSerializer.forValueType(streamCodec);
        FabricTrackedDataRegistry.register(Identifier.fromNamespaceAndPath(MineTale.MOD_ID, id), serializer);
        return serializer;
    }

    
}
