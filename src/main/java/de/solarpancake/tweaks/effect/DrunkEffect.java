package de.solarpancake.tweaks.effect;

import de.solarpancake.tweaks.attachment.ModAttachments;
import de.solarpancake.tweaks.network.TripPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Random;

/**
 * Drunk, level 1-5. Every sip of a can raises the level by one (max 5) and resets the timer.
 * <p>
 * Players walking around drunk can trip: they fall into the crawling pose for {@link #TRIP_TICKS} and may drop
 * what they hold. Both chances grow with the level. No message or sound, it just happens.
 */
public class DrunkEffect extends MobEffect {
    public static final int MAX_LEVEL = 5;
    /** Every sip resets the effect to this duration (20 s). */
    public static final int DURATION_PER_SIP = 400;

    /** Chance per check (every 10 ticks) per level: lvl 1 = 1 % (~2 %/s), lvl 5 = 5 % (~10 %/s). */
    private static final float TRIP_CHANCE_PER_LEVEL = 0.01f;
    /** Chance to drop the held item when tripping, per level: lvl 1 = 10 %, lvl 5 = 50 %. */
    private static final float DROP_CHANCE_PER_LEVEL = 0.10f;
    /** How long the player lies on the ground. */
    public static final int TRIP_TICKS = 30;
    /** Extra time after getting up before the next trip can happen. */
    public static final int TRIP_COOLDOWN = 40;
    /** Minimum horizontal speed (blocks/tick) to count as walking. Standing still never trips. */
    private static final double MIN_WALK_SPEED = 0.05;

    private static final Random RANDOM = new Random();

    public DrunkEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    /** One sip: drunk level +1 (max {@link #MAX_LEVEL}) and the timer starts over. Server side only. */
    public static void addSip(LivingEntity entity) {
        MobEffectInstance current = entity.getEffect(ModEffects.DRUNK_EFFECT);
        int amplifier = current == null ? 0 : Math.min(current.getAmplifier() + 1, MAX_LEVEL - 1);
        entity.removeEffect(ModEffects.DRUNK_EFFECT);
        entity.addEffect(new MobEffectInstance(ModEffects.DRUNK_EFFECT, DURATION_PER_SIP, amplifier));
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplifier) {
        if (mob instanceof ServerPlayer player) {
            tryTrip(player, amplifier + 1);
            return true;
        }
        // mobs: random wobble
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

    /* ---------- tripping ---------- */

    private static void tryTrip(ServerPlayer player, int level) {
        if (!canTrip(player) || player.getRandom().nextFloat() >= TRIP_CHANCE_PER_LEVEL * level) {
            return;
        }
        player.setData(ModAttachments.TRIP_TICKS, TRIP_TICKS + TRIP_COOLDOWN);
        player.setForcedPose(Pose.SWIMMING); // crawling, also seen by other players
        PacketDistributor.sendToPlayer(player, new TripPayload(TRIP_TICKS)); // the client needs it too, or it stands back up

        ItemStack held = player.getMainHandItem();
        if (!held.isEmpty() && player.getRandom().nextFloat() < DROP_CHANCE_PER_LEVEL * level) {
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            player.drop(held, true, false);
        }
    }

    private static boolean canTrip(ServerPlayer player) {
        Vec3 movement = player.getKnownMovement();
        return player.getData(ModAttachments.TRIP_TICKS) <= 0
                && !player.isCreative() && !player.isSpectator()
                && player.onGround() && !player.isPassenger() && !player.isInWater()
                && !player.isFallFlying() && !player.getAbilities().flying
                && movement.horizontalDistanceSqr() > MIN_WALK_SPEED * MIN_WALK_SPEED;
    }

    /** Called every server tick for every player: gets them back up and runs the cooldown. */
    public static void tickTrip(ServerPlayer player) {
        int ticks = player.getData(ModAttachments.TRIP_TICKS);
        if (ticks <= 0) {
            return;
        }
        ticks--;
        player.setData(ModAttachments.TRIP_TICKS, ticks);
        if (ticks == TRIP_COOLDOWN) {
            player.setForcedPose(null);
        }
    }
}
