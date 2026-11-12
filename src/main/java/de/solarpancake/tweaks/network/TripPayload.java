package de.solarpancake.tweaks.network;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.client.drunk.DrunkClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server -> client: the player tripped and lies on the ground for {@code ticks}.
 * Needed because NeoForge's forced pose isn't synced, the client would stand right back up otherwise.
 */
public record TripPayload(int ticks) implements CustomPacketPayload {
    public static final Type<TripPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "trip"));
    public static final StreamCodec<ByteBuf, TripPayload> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(TripPayload::new, TripPayload::ticks);

    @Override
    public Type<TripPayload> type() {
        return TYPE;
    }

    public static void handle(TripPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> DrunkClient.startTrip(payload.ticks()));
    }
}
