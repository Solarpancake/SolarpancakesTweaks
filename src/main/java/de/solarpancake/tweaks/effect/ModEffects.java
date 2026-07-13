package de.solarpancake.tweaks.effect;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Solarpancakestweaks.MODID);

    public static final Holder<MobEffect> DRUNK_EFFECT = MOB_EFFECTS.register("drunk",
            () -> new DrunkEffect(MobEffectCategory.NEUTRAL, 0x36ebab));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
