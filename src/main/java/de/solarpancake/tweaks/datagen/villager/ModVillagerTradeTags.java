package de.solarpancake.tweaks.datagen.villager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VillagerTradesTagsProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.VillagerTradeTags;

import java.util.concurrent.CompletableFuture;

public class ModVillagerTradeTags extends VillagerTradesTagsProvider {
    public ModVillagerTradeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }


    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(VillagerTradeTags.FARMER_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FARMER_1_EMERALD_LEMON.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FARMER_1_EMERALD_RASPBERRY.identifier()));
    }
}
