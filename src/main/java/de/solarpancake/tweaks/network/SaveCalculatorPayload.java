package de.solarpancake.tweaks.network;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.component.CalculatorData;
import de.solarpancake.tweaks.item.custom.CalculatorItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client -> server: calculator screen closed, store its input + history on the held calculator. */
public record SaveCalculatorPayload(InteractionHand hand, CalculatorData data) implements CustomPacketPayload {
    public static final Type<SaveCalculatorPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "save_calculator"));
    public static final StreamCodec<ByteBuf, SaveCalculatorPayload> STREAM_CODEC = StreamCodec.composite(
            InteractionHand.STREAM_CODEC, SaveCalculatorPayload::hand,
            CalculatorData.STREAM_CODEC, SaveCalculatorPayload::data,
            SaveCalculatorPayload::new);

    @Override
    public Type<SaveCalculatorPayload> type() {
        return TYPE;
    }

    public static void handle(SaveCalculatorPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ItemStack stack = context.player().getItemInHand(payload.hand());
            if (stack.getItem() instanceof CalculatorItem) {
                CalculatorItem.setData(stack, payload.data());
            }
        });
    }
}
