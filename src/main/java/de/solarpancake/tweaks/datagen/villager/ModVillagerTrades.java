package de.solarpancake.tweaks.datagen.villager;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

public class ModVillagerTrades {
    public static final ResourceKey<VillagerTrade> FARMER_1_EMERALD_RASPBERRY = createKey("farmer_1_emerald_raspberry");
    public static final ResourceKey<VillagerTrade> FARMER_1_EMERALD_LEMON = createKey("farmer_1_emerald_lemon");



    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);



        context.register(FARMER_1_EMERALD_LEMON, new VillagerTrade(
                new TradeCost(Items.EMERALD, 4),
                new ItemStackTemplate(ModItems.LEMON, 8),
                12, 6, 0.05f, Optional.empty(), List.of()));

        context.register(FARMER_1_EMERALD_RASPBERRY, new VillagerTrade(
                new TradeCost(Items.EMERALD, 4),
                new ItemStackTemplate(ModItems.RASPBERRY, 8),
                12, 6, 0.05f, Optional.empty(), List.of()));


    }


    private static ResourceKey<VillagerTrade> createKey (String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, name));
    }
}
