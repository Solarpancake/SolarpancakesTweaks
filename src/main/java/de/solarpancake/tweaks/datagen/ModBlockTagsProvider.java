package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.tags.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Solarpancakestweaks.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        /*  Pickaxe Mineable  */
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_BLOCK.get()))
                .add(ModBlocks.getRK(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get()))
                .add(ModBlocks.getRK(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get()))
                .add(ModBlocks.getRK(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_CLUSTER.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_BRICKS.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_BRICKS_SLAB.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_PILLAR.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_LAMP.get()))
                .add(ModBlocks.getRK(ModBlocks.CAT_TREE_1.get()))
                .add(ModBlocks.getRK(ModBlocks.CAT_TREE_2.get()))
                .add(ModBlocks.getRK(ModBlocks.CAT_TREE_3.get()))
                .add(ModBlocks.getRK(ModBlocks.CAT_TREE_4.get()))
                .add(ModBlocks.getRK(ModBlocks.CAT_TREE_5.get()))
                .add(ModBlocks.getRK(ModBlocks.STEEL_GRATE.get()))
                .add(ModBlocks.getRK(ModBlocks.CAT_TREE_6.get()));

        /*  Needs Iron Tool  */
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_BLOCK.get()))
                .add(ModBlocks.getRK(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get()))
                .add(ModBlocks.getRK(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get()))
                .add(ModBlocks.getRK(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get()))
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_CLUSTER.get()));

        /*  Needs/Incorrect for Iced Crystal Tool  */
        tag(ModTags.Blocks.NEEDS_ICED_CRYSTAL_TOOL)
                .add(ModBlocks.getRK(ModBlocks.ICED_CRYSTAL_BLOCK.get()))
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(ModTags.Blocks.INCORRECT_FOR_ICED_CRYSTAL_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_ICED_CRYSTAL_TOOL);

        tag(BlockTags.CROPS)
                .add(ModBlocks.getRK(ModBlocks.DILL_CROP.get()));
    }
}
