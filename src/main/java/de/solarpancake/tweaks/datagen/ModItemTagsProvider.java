package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.item.ModItems;
import de.solarpancake.tweaks.tags.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Solarpancakestweaks.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.TRANSFORMABLE_ITEMS)
                .add(Items.ANDESITE);


        tag(ModTags.Items.ICED_CRYSTAL_REPAIRABLE)
                .add(ModItems.ICED_CRYSTAL.get());

        tag(ItemTags.SWORDS).add(ModItems.ICED_CRYSTAL_SWORD.get());
        tag(ItemTags.PICKAXES).add(ModItems.ICED_CRYSTAL_PICKAXE.get());
        tag(ItemTags.AXES).add(ModItems.ICED_CRYSTAL_AXE.get());
        tag(ItemTags.SHOVELS).add(ModItems.ICED_CRYSTAL_SHOVEL.get());
        tag(ItemTags.HOES).add(ModItems.ICED_CRYSTAL_HOE.get());
        tag(ItemTags.SPEARS).add(ModItems.ICED_CRYSTAL_SPEAR.get());

        tag(ItemTags.HEAD_ARMOR).add(ModItems.ICED_CRYSTAL_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(ModItems.ICED_CRYSTAL_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR).add(ModItems.ICED_CRYSTAL_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(ModItems.ICED_CRYSTAL_BOOTS.get());
    }
}
