package de.solarpancake.tweaks.network;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Solarpancakestweaks.MODID)
public class ModNetworking {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ToggleMagnetPayload.TYPE, ToggleMagnetPayload.STREAM_CODEC, ToggleMagnetPayload::handle);
        registrar.playToServer(SaveCalculatorPayload.TYPE, SaveCalculatorPayload.STREAM_CODEC, SaveCalculatorPayload::handle);
        registrar.playToServer(CaptureCalculatorPosPayload.TYPE, CaptureCalculatorPosPayload.STREAM_CODEC, CaptureCalculatorPosPayload::handle);
        registrar.playToClient(TripPayload.TYPE, TripPayload.STREAM_CODEC, TripPayload::handle);
    }
}
