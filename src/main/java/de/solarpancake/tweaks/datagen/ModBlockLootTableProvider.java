package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        add(ModBlocks.ICED_CRYSTAL_CLUSTER.get(),
                createMultipleDrops(ModBlocks.ICED_CRYSTAL_CLUSTER.get(), ModItems.ICED_CRYSTAL.get(), 1, 3));

        add(ModBlocks.FLOWERING_LEMON_TREE_LEAVES.get(), noDrop());

        add(ModBlocks.BUDDING_ICED_CRYSTAL.get(), noDrop());

        add(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get(), noDrop());

        add(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get(), noDrop());

        add(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get(), noDrop());

        add(ModBlocks.ICED_CRYSTAL_BLOCK.get(),
                createMultipleDrops(ModBlocks.ICED_CRYSTAL_BLOCK.get(), ModItems.ICED_CRYSTAL.get(), 2, 4));

        dropSelf(ModBlocks.ICED_CRYSTAL_BRICKS.get());
        dropSelf(ModBlocks.ICED_CRYSTAL_PILLAR.get());
        dropSelf(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS.get());
        dropSelf(ModBlocks.ICED_CRYSTAL_LAMP.get());
        dropSelf(ModBlocks.CAT_TREE_1.get());
        dropSelf(ModBlocks.CAT_TREE_2.get());
        dropSelf(ModBlocks.CAT_TREE_3.get());
        dropSelf(ModBlocks.CAT_TREE_4.get());
        dropSelf(ModBlocks.CAT_TREE_5.get());
        dropSelf(ModBlocks.CAT_TREE_6.get());

        add(ModBlocks.ICED_CRYSTAL_BRICKS_SLAB.get(), this::createSlabItemTable);

    }




    protected LootTable.Builder createMultipleDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                                //.apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
