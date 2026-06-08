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
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ICED_CRYSTAL_BLOCK.get())
                .add(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get())
                .add(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get())
                .add(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get())
                .add(ModBlocks.ICED_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.ICED_CRYSTAL_BRICKS.get())
                .add(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS.get())
                .add(ModBlocks.ICED_CRYSTAL_BRICKS_SLAB.get())
                .add(ModBlocks.ICED_CRYSTAL_PILLAR.get())
                .add(ModBlocks.ICED_CRYSTAL_LAMP.get());


        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ICED_CRYSTAL_BLOCK.get())
                .add(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get())
                .add(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get())
                .add(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get())
                .add(ModBlocks.ICED_CRYSTAL_CLUSTER.get());


        tag(ModTags.Blocks.NEEDS_ICED_CRYSTAL_TOOL)
                .add(ModBlocks.ICED_CRYSTAL_BLOCK.get())
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(ModTags.Blocks.INCORRECT_FOR_ICED_CRYSTAL_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_ICED_CRYSTAL_TOOL);

        /*tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)*/
    }
}
