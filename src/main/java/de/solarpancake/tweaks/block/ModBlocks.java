package de.solarpancake.tweaks.block;

import de.solarpancake.tweaks.Solarpancakestweaks;

import de.solarpancake.tweaks.block.custom.BuddingIcedCrystalBlock;
import de.solarpancake.tweaks.block.custom.CatTree1;
import de.solarpancake.tweaks.block.custom.IcedCrystalLampBlock;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Solarpancakestweaks.MODID);



    public static final DeferredBlock<Block> ICED_CRYSTAL_BLOCK = registerBlock("iced_crystal_block",
            properties -> new Block(properties.strength(4f)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> FLOWERING_LEMON_TREE_LEAVES = registerBlock("flowering_lemon_tree_leaves",
            properties -> new Block(properties.strength(4f)));


    public static final DeferredBlock<Block> ICED_CRYSTAL_BRICKS = registerBlock("iced_crystal_bricks",
            properties -> new Block(properties.strength(4f)));

    public static final DeferredBlock<Block> BUDDING_ICED_CRYSTAL = registerBlock("budding_iced_crystal",
            properties -> new BuddingIcedCrystalBlock(properties
                    .strength(1.5f)
                    .sound(SoundType.AMETHYST)
                    .randomTicks()));


    public static final DeferredBlock<Block> ICED_CRYSTAL_CLUSTER = registerBlock("iced_crystal_cluster",
            properties -> new AmethystClusterBlock(7, 3, properties
                    .strength(1.5f)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .noOcclusion()
                    .randomTicks()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> SMALL_ICED_CRYSTAL_BUD = registerBlock("small_iced_crystal_bud",
            properties -> new AmethystClusterBlock(3, 4, properties
                    .strength(1.5f)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .noOcclusion()
                    .randomTicks()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> MEDIUM_ICED_CRYSTAL_BUD = registerBlock("medium_iced_crystal_bud",
            properties -> new AmethystClusterBlock(4, 3, properties
                    .strength(1.5f)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .noOcclusion()
                    .randomTicks()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> LARGE_ICED_CRYSTAL_BUD = registerBlock("large_iced_crystal_bud",
            properties -> new AmethystClusterBlock(5, 3, properties
                    .strength(1.5f)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .noOcclusion()
                    .randomTicks()
                    .pushReaction(PushReaction.DESTROY)));


    public static final DeferredBlock<Block> ICED_CRYSTAL_BRICKS_STAIRS = registerBlock("iced_crystal_bricks_stairs",
            properties -> new StairBlock(ModBlocks.ICED_CRYSTAL_BRICKS.get().defaultBlockState(), properties.strength(2f)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> ICED_CRYSTAL_BRICKS_SLAB = registerBlock("iced_crystal_bricks_slab",
            properties -> new SlabBlock(properties.strength(2f)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> ICED_CRYSTAL_PILLAR = registerBlock("iced_crystal_pillar",
            properties -> new RotatedPillarBlock(properties
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> ICED_CRYSTAL_LAMP = registerBlock("iced_crystal_lamp",
            properties -> new IcedCrystalLampBlock(properties.strength(2f)
                    .requiresCorrectToolForDrops().lightLevel(state -> state.getValue(IcedCrystalLampBlock.CLICKED) ? 15 : 0)));

    public static final DeferredBlock<Block> CAT_TREE_1 = registerBlock("cat_tree_1",
            properties -> new CatTree1(properties.strength(2f)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CAT_TREE_2 = registerBlock("cat_tree_2",
            properties -> new CatTree1(properties.strength(2f)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CAT_TREE_3 = registerBlock("cat_tree_3",
            properties -> new CatTree1(properties.strength(2f)
                    .requiresCorrectToolForDrops()));



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function){
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
