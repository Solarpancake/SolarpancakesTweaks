package de.solarpancake.tweaks.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class DrunkEffect extends MobEffect {
    private static final Random RANDOM = new Random();
    public DrunkEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplifier) {
        double strength = 0.03 + (amplifier * 0.02);
        double dx = (RANDOM.nextDouble() - 0.5) * strength;
        double dz = (RANDOM.nextDouble() - 0.5) * strength;
        Vec3 motion = mob.getDeltaMovement();
        mob.setDeltaMovement(motion.x + dx, motion.y, motion.z + dz);
        return true;
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return tickCount % 10 == 0;
    }
}