package de.solarpancake.tweaks.network;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.component.CalculatorData;
import de.solarpancake.tweaks.item.custom.CalculatorItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client -> server: player left-clicked a block with the calculator; in coord mode its coords become the input. */
public record CaptureCalculatorPosPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CaptureCalculatorPosPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "capture_calculator_pos"));
    public static final StreamCodec<ByteBuf, CaptureCalculatorPosPayload> STREAM_CODEC =
            BlockPos.STREAM_CODEC.map(CaptureCalculatorPosPayload::new, CaptureCalculatorPosPayload::pos);

    @Override
    public Type<CaptureCalculatorPosPayload> type() {
        return TYPE;
    }

    public static void handle(CaptureCalculatorPosPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getMainHandItem();
            // only accept blocks the player could actually reach
            if (stack.getItem() instanceof CalculatorItem && player.isWithinBlockInteractionRange(payload.pos(), 1.0)) {
                CalculatorData data = CalculatorItem.getData(stack);
                if (data.mode() == CalculatorData.Mode.COORD) {
                    CalculatorItem.setData(stack, data.withCoordinateInput(payload.pos()));
                    player.sendOverlayMessage(Component.translatable("item.solarpancakestweaks.calculator.captured",
                            CalculatorData.coordinateToken(payload.pos())));
                }
            }
        });
    }
}
