package de.solarpancake.tweaks.item;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.food.ModFoods;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Solarpancakestweaks.MODID);
    
    public static final DeferredItem<Item> ICED_CRYSTAL = ITEMS.registerSimpleItem("iced_crystal");
    public static final DeferredItem<Item> CRUSHED_ICED_CRYSTAL = ITEMS.registerSimpleItem("crushed_iced_crystal");
    public static final DeferredItem<Item> GREEN_APPLE_SLICED = ITEMS.registerSimpleItem("green_apple_sliced");
    public static final DeferredItem<Item> EMPTY_CAN = ITEMS.registerSimpleItem("empty_can");

    public static final DeferredItem<Item> GREEN_APPLE = ITEMS.registerItem("green_apple",
            properties -> new Item(properties.food(ModFoods.GREEN_APPLE, ModFoods.GREE_APPLE_CONSUMABLE)));

    public static final DeferredItem<Item> ORIGINAL_CAN = ITEMS.registerItem("original_can",
            properties -> new Item(properties.food(ModFoods.ORIGINAL_CAN, ModFoods.ORIGINAL_CAN_CONSUMABLE)));

    public static final DeferredItem<Item> GREEN_APPLE_CAN = ITEMS.registerItem("green_apple_can",
            properties -> new Item(properties.food(ModFoods.GREEN_APPLE_CAN, ModFoods.GREEN_APPLE_CAN_CONSUMABLE)));

    public static final DeferredItem<Item> RASPBERRY_CAN = ITEMS.registerItem("raspberry_can",
            properties -> new Item(properties.food(ModFoods.RASPBERRY_CAN, ModFoods.RASPBERRY_CAN_CONSUMABLE)));

    public static final DeferredItem<Item> BLACK_CAN = ITEMS.registerItem("black_can",
            properties -> new Item(properties.food(ModFoods.BLACK_CAN, ModFoods.BLACK_CAN_CONSUMABLE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
