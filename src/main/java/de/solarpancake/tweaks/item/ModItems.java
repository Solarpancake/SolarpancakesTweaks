package de.solarpancake.tweaks.item;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.food.ModFoods;
import de.solarpancake.tweaks.item.custom.IceDetectorItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

import static de.solarpancake.tweaks.block.ModBlocks.STEEL_GRATE;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Solarpancakestweaks.MODID);


    /*  SimpleItems  */
    public static final DeferredItem<Item> ICED_CRYSTAL = ITEMS.registerSimpleItem("iced_crystal");
    public static final DeferredItem<Item> CRUSHED_ICED_CRYSTAL = ITEMS.registerSimpleItem("crushed_iced_crystal");
    public static final DeferredItem<Item> GREEN_APPLE_SLICED = ITEMS.registerSimpleItem("green_apple_sliced");
    public static final DeferredItem<Item> EMPTY_CAN = ITEMS.registerSimpleItem("empty_can");
    public static final DeferredItem<Item> ICED_CRYSTAL_ROD = ITEMS.registerSimpleItem("iced_crystal_rod");


    /*  Items  */
    public static final DeferredItem<Item> GREEN_APPLE = ITEMS.registerItem("green_apple",
            properties -> new Item(properties.food(ModFoods.GREEN_APPLE, ModFoods.GREE_APPLE_CONSUMABLE)));
    public static final DeferredItem<Item> LEMON = ITEMS.registerItem("lemon",
            properties -> new Item(properties.food(ModFoods.LEMON, ModFoods.LEMON_CONSUMABLE)));
    public static final DeferredItem<Item> DILL = ITEMS.registerItem("dill",
            properties -> new Item(properties.food(ModFoods.DILL, ModFoods.DILL_CONSUMABLE)));
    public static final DeferredItem<Item> RASPBERRY = ITEMS.registerItem("raspberry",
            properties -> new BlockItem(ModBlocks.RASPBERRY_BUSH.get(), properties.food(ModFoods.RASPBERRY)));
    public static final DeferredItem<Item> ORIGINAL_CAN = ITEMS.registerItem("original_can",
            properties -> new Item(properties.food(ModFoods.ORIGINAL_CAN, ModFoods.ORIGINAL_CAN_CONSUMABLE)));
    public static final DeferredItem<Item> GREEN_APPLE_CAN = ITEMS.registerItem("green_apple_can",
            properties -> new Item(properties.food(ModFoods.GREEN_APPLE_CAN, ModFoods.GREEN_APPLE_CAN_CONSUMABLE)));
    public static final DeferredItem<Item> RASPBERRY_CAN = ITEMS.registerItem("raspberry_can",
            properties -> new Item(properties.food(ModFoods.RASPBERRY_CAN, ModFoods.RASPBERRY_CAN_CONSUMABLE)));
    public static final DeferredItem<Item> BLACK_CAN = ITEMS.registerItem("black_can",
            properties -> new Item(properties.food(ModFoods.BLACK_CAN, ModFoods.BLACK_CAN_CONSUMABLE)));
    public static final DeferredItem<Item> ICED_CRYSTAL_SWORD = ITEMS.registerItem("iced_crystal_sword",
            properties -> new Item(properties.sword(ModToolTiers.ICED_CRYSTAL, 3, -2.4f)));
    public static final DeferredItem<Item> ICED_CRYSTAL_PICKAXE = ITEMS.registerItem("iced_crystal_pickaxe",
            properties -> new Item(properties.pickaxe(ModToolTiers.ICED_CRYSTAL, 1, -2.8f)));
    public static final DeferredItem<Item> ICED_CRYSTAL_AXE = ITEMS.registerItem("iced_crystal_axe",
            properties -> new AxeItem(ModToolTiers.ICED_CRYSTAL, 6, -3.2f, properties));
    public static final DeferredItem<Item> ICED_CRYSTAL_SHOVEL = ITEMS.registerItem("iced_crystal_shovel",
            properties -> new ShovelItem(ModToolTiers.ICED_CRYSTAL, 1.5f, -3.0f, properties));
    public static final DeferredItem<Item> ICED_CRYSTAL_HOE = ITEMS.registerItem("iced_crystal_hoe",
            properties -> new HoeItem(ModToolTiers.ICED_CRYSTAL, 0, -3, properties));
    public static final DeferredItem<Item> ICED_CRYSTAL_SPEAR = ITEMS.registerItem("iced_crystal_spear",
            properties -> new Item(properties.spear(ModToolTiers.ICED_CRYSTAL, 0.96f, 0.7f, 0.1f, 3.5f, 13f, 8.5f, 5.1f, 13.37f, 4.67f)));
    public static final DeferredItem<Item> ICED_CRYSTAL_HELMET = ITEMS.registerItem("iced_crystal_helmet",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ICED_CRYSTAL_ARMOR_MATERIAL, ArmorType.HELMET)));
    public static final DeferredItem<Item> ICED_CRYSTAL_CHESTPLATE = ITEMS.registerItem("iced_crystal_chestplate",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ICED_CRYSTAL_ARMOR_MATERIAL, ArmorType.CHESTPLATE)));
    public static final DeferredItem<Item> ICED_CRYSTAL_LEGGINGS = ITEMS.registerItem("iced_crystal_leggings",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ICED_CRYSTAL_ARMOR_MATERIAL, ArmorType.LEGGINGS)));
    public static final DeferredItem<Item> ICED_CRYSTAL_BOOTS = ITEMS.registerItem("iced_crystal_boots",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ICED_CRYSTAL_ARMOR_MATERIAL, ArmorType.BOOTS)));
    public static final DeferredItem<Item> ICED_CRYSTAL_HORSE_ARMOR = ITEMS.registerItem("iced_crystal_horse_armor",
            properties -> new Item(properties.horseArmor(ModArmorMaterials.ICED_CRYSTAL_ARMOR_MATERIAL)));
    public static final DeferredItem<Item> ICED_CRYSTAL_BOW = ITEMS.registerItem("iced_crystal_bow",
            properties -> new BowItem(properties.durability(500)));
    public static final DeferredItem<Item> DILL_SEEDS = ITEMS.registerItem("dill_seeds",
            properties -> new BlockItem(ModBlocks.DILL_CROP.get(), properties));


    /*  CustomItems  */
    public static final DeferredItem<Item> ICE_DETECTOR = ITEMS.registerItem("ice_detector",
            properties -> new IceDetectorItem(properties.durability(64)){
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    for (int i = 0; i <= 3; i++) {
                        builder.accept(Component.translatable("tooltip.ice_detector.tooltips." + i));
                    }
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
    });


    /* BlockItems */
    public static final DeferredItem<BlockItem> STEEL_GRATE = ITEMS.registerSimpleBlockItem(ModBlocks.STEEL_GRATE);


    /* helper methods */

    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
