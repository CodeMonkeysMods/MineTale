package com.tcm.MineTale.network;

import com.tcm.MineTale.MineTale;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public record CraftRequestPayload(ItemStack resultItem, int amount) implements CustomPacketPayload {

    // Matches your source: CustomPacketPayload.Type
    public static final CustomPacketPayload.Type<CraftRequestPayload> TYPE = 
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MineTale.MOD_ID, "craft_request"));

    // Uses StreamCodec and ByteBufCodecs to match 1.21.1+ logic
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftRequestPayload> CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, CraftRequestPayload::resultItem,
        ByteBufCodecs.VAR_INT, CraftRequestPayload::amount,
        CraftRequestPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}