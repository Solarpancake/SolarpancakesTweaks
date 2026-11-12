package de.solarpancake.tweaks.component;

import com.mojang.serialization.Codec;
import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Solarpancakestweaks.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> MAGNET_ACTIVE =
            DATA_COMPONENTS.registerComponentType("magnet_active",
                    builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CalculatorData>> CALCULATOR_DATA =
            DATA_COMPONENTS.registerComponentType("calculator_data",
                    builder -> builder.persistent(CalculatorData.CODEC).networkSynchronized(CalculatorData.STREAM_CODEC));

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
