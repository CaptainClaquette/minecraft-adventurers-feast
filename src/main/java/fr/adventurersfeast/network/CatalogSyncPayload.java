package fr.adventurersfeast.network;

import fr.adventurersfeast.AdventurersFeast;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CatalogSyncPayload(String json)
    implements CustomPacketPayload {

    public static final Type<CatalogSyncPayload> TYPE =
        new Type<>(
            Identifier.fromNamespaceAndPath(
                AdventurersFeast.MODID,
                "catalog_sync"
            )
        );

    public static final StreamCodec<FriendlyByteBuf, CatalogSyncPayload> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.stringUtf8(1_048_576),
            CatalogSyncPayload::json,
            CatalogSyncPayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}