package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModArmorMaterials;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
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
        itemModels.generateFlatItem(ModItems.LEMON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ICE_DETECTOR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_ROD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RASPBERRY.get(), ModelTemplates.FLAT_ITEM);



        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_HELMET.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_CHESTPLATE.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_LEGGINGS.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_BOOTS.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);


        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(ModItems.ICED_CRYSTAL_SPEAR.get());




        /* BLOCKS */
        blockModels.createTrivialCube(ModBlocks.ICED_CRYSTAL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.FLOWERING_LEMON_TREE_LEAVES.get());
        blockModels.createTrivialCube(ModBlocks.BUDDING_ICED_CRYSTAL.get());
        blockModels.createAmethystCluster(ModBlocks.ICED_CRYSTAL_CLUSTER.get());
        blockModels.createAmethystCluster(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get());
        blockModels.createAmethystCluster(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get());
        blockModels.createAmethystCluster(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get());

        blockModels.family(ModBlocks.ICED_CRYSTAL_BRICKS.get())
                .stairs(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS.get())
                .slab(ModBlocks.ICED_CRYSTAL_BRICKS_SLAB.get());

        blockModels.createRotatedPillarWithHorizontalVariant(ModBlocks.ICED_CRYSTAL_PILLAR.get(),
                TexturedModel.COLUMN,
                TexturedModel.COLUMN_HORIZONTAL
        );


    }
}
