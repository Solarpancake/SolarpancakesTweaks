package de.solarpancake.tweaks.creativemodetab;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Solarpancakestweaks.MODID);

    public static final Supplier<CreativeModeTab> SOLARPANCAKES_TWEAKS_TAB = CREATIVE_MODE_TABS.register("solarpancakes_tweaks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ICED_CRYSTAL.get()))
                    .title(Component.translatable("creativetab.solarpancakestweaks.solarpancakes_tweaks"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.ICED_CRYSTAL_SWORD);
                        output.accept(ModItems.ICED_CRYSTAL_PICKAXE);
                        output.accept(ModItems.ICED_CRYSTAL_SHOVEL);
                        output.accept(ModItems.ICED_CRYSTAL_AXE);
                        output.accept(ModItems.ICED_CRYSTAL_HOE);
                        output.accept(ModItems.ICED_CRYSTAL_SPEAR);

                        output.accept(ModItems.ICED_CRYSTAL_HELMET);
                        output.accept(ModItems.ICED_CRYSTAL_CHESTPLATE);
                        output.accept(ModItems.ICED_CRYSTAL_LEGGINGS);
                        output.accept(ModItems.ICED_CRYSTAL_BOOTS);
                        output.accept(ModItems.ICED_CRYSTAL_HORSE_ARMOR);
                        output.accept(ModItems.ICED_CRYSTAL_BOW);


                        output.accept(ModItems.CRUSHED_ICED_CRYSTAL);
                        output.accept(ModItems.GREEN_APPLE);
                        output.accept(ModItems.LEMON);
                        output.accept(ModItems.RASPBERRY);
                        output.accept(ModItems.GREEN_APPLE_SLICED);
                        output.accept(ModItems.GREEN_APPLE_CAN);
                        output.accept(ModItems.ORIGINAL_CAN);
                        output.accept(ModItems.RASPBERRY_CAN);
                        output.accept(ModItems.BLACK_CAN);
                        output.accept(ModItems.EMPTY_CAN);
                        output.accept(ModItems.ICED_CRYSTAL_ROD);
                        output.accept(ModItems.ICED_CRYSTAL);

                        output.accept(ModBlocks.ICED_CRYSTAL_BLOCK);
                        output.accept(ModBlocks.SMALL_ICED_CRYSTAL_BUD);
                        output.accept(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD);
                        output.accept(ModBlocks.LARGE_ICED_CRYSTAL_BUD);
                        output.accept(ModBlocks.ICED_CRYSTAL_CLUSTER);
                        output.accept(ModBlocks.BUDDING_ICED_CRYSTAL);
                        output.accept(ModBlocks.FLOWERING_LEMON_TREE_LEAVES);
                        output.accept(ModBlocks.ICED_CRYSTAL_BRICKS);
                        output.accept(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS);
                        output.accept(ModBlocks.ICED_CRYSTAL_BRICKS_SLAB);
                        output.accept(ModBlocks.ICED_CRYSTAL_PILLAR);
                        output.accept(ModBlocks.ICED_CRYSTAL_LAMP);
                        output.accept(ModBlocks.CAT_TREE_1);

                        output.accept(ModItems.ICE_DETECTOR);


                    }).build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
