package de.solarpancake.tweaks.network;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.item.custom.MagnetItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleMagnetPayload() implements CustomPacketPayload {
    public static final ToggleMagnetPayload INSTANCE = new ToggleMagnetPayload();
    public static final CustomPacketPayload.Type<ToggleMagnetPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "toggle_magnet"));
    public static final StreamCodec<ByteBuf, ToggleMagnetPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public CustomPacketPayload.Type<ToggleMagnetPayload> type() {
        return TYPE;
    }

    public static void handle(ToggleMagnetPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> MagnetItem.findMagnet(context.player())
                .ifPresent(stack -> MagnetItem.toggle(context.player(), stack)));
    }
}
