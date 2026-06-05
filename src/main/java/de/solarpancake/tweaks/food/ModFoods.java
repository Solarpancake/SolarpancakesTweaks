package de.solarpancake.tweaks.food;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoods {
    public static final FoodProperties GREEN_APPLE = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final FoodProperties LEMON = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final FoodProperties RASPBERRY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final Consumable RASPBERRY_CONSUMABLE = Consumables.defaultFood()
            .consumeSeconds(1.2f).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();

    public static final Consumable GREE_APPLE_CONSUMABLE = Consumables.defaultFood()
            .consumeSeconds(1.2f).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();

    public static final FoodProperties ORIGINAL_CAN = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final Consumable ORIGINAL_CAN_CONSUMABLE = Consumables.defaultDrink()
            .consumeSeconds(1.6f)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();

    public static final FoodProperties BLACK_CAN = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final Consumable BLACK_CAN_CONSUMABLE = Consumables.defaultDrink()
            .consumeSeconds(1.6f)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();

    public static final FoodProperties RASPBERRY_CAN = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final Consumable RASPBERRY_CAN_CONSUMABLE = Consumables.defaultDrink()
            .consumeSeconds(1.6f)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();

    public static final FoodProperties GREEN_APPLE_CAN = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();

    public static final Consumable GREEN_APPLE_CAN_CONSUMABLE = Consumables.defaultDrink()
            .consumeSeconds(1.6f)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();

    public static final Consumable LEMON_CONSUMABLE = Consumables.defaultFood()
            .consumeSeconds(1.2f).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 400))).build();




}
