package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Solarpancakestweaks.MODID);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CRUSHED_ICED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_APPLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_APPLE_SLICED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_APPLE_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RASPBERRY_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ORIGINAL_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLACK_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.EMPTY_CAN.get(), ModelTemplates.FLAT_ITEM);



        /* BLOCKS */
        blockModels.createTrivialCube(ModBlocks.ICED_CRYSTAL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.BUDDING_ICED_CRYSTAL.get());
        blockModels.createAmethystCluster(ModBlocks.ICED_CRYSTAL_CLUSTER.get());
        blockModels.createAmethystCluster(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get());
        blockModels.createAmethystCluster(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get());
        blockModels.createAmethystCluster(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get());


    }
}
