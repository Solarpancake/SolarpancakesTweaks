package de.solarpancake.tweaks.tags;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTABLES = createTag("metal_detectables");

        public static final TagKey<Block> NEEDS_ICED_CRYSTAL_TOOL = createTag("needs_iced_crystal_tool");
        public static final TagKey<Block> INCORRECT_FOR_ICED_CRYSTAL_TOOL = createTag("incorrect_for_iced_crystal_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, name));
        }
    }


    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");
        public static final TagKey<Item> ICED_CRYSTAL_REPAIRABLE = createTag("iced_crystal_repairable");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, name));
        }
    }

    public static class Trades {
        public static final TagKey<VillagerTrade> BARTENDER_LEVEL_1 = createTag("bartender/level_1");
        public static final TagKey<VillagerTrade> BARTENDER_LEVEL_2 = createTag("bartender/level_2");

        
        private static TagKey<VillagerTrade> createTag(String name) {
            return TagKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, name));
        }
    }


}
