package com.tcm.MineTale.network;

import java.util.List;

import com.tcm.MineTale.MineTale;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public record ClientboundNearbyInventorySyncPacket(List<ItemStack> items) implements CustomPacketPayload {
    public static final Type<ClientboundNearbyInventorySyncPacket> TYPE = 
        new Type<>(Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "nearby_chest_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNearbyInventorySyncPacket> STREAM_CODEC = 
        StreamCodec.composite(
            ItemStack.OPTIONAL_LIST_STREAM_CODEC, ClientboundNearbyInventorySyncPacket::items,
            ClientboundNearbyInventorySyncPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
