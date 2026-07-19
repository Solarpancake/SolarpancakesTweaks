package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock;
import static net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger.TriggerInstance.placedBlock;

public class ModAdvancements extends AdvancementProvider {
    public ModAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new SolarpancakesTweaksAdvancements()));
    }

    public static class SolarpancakesTweaksAdvancements implements AdvancementSubProvider {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
            var items = registries.lookupOrThrow(Registries.ITEM);

            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(
                            ModItems.ICED_CRYSTAL,
                            Component.translatable("advancements.solarpancakestweaks.root.title"),
                            Component.translatable("advancements.solarpancakestweaks.root.description"),
                            Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                            AdvancementType.TASK,
                            false,
                            false,
                            false
                    )
                    .addCriterion("has_iced_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, ModItems.ICED_CRYSTAL.asItem())))
                    .save(output, Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "solarpancakestweaks/root"));

            AdvancementHolder plantSeed = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ModItems.RASPBERRY,
                            Component.translatable("advancements.solarpancakestweaks.plant_custom.title"),
                            Component.translatable("advancements.solarpancakestweaks.plant_custom.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion("berries", placedBlock(ModBlocks.RASPBERRY_BUSH.get()))
                    .addCriterion("dill", placedBlock(ModBlocks.DILL_CROP.get()))
                    .save(output, "solarpancakestweaks/plant_custom");

            AdvancementHolder iceDetector = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ModItems.ICE_DETECTOR,
                            Component.translatable("advancements.solarpancakestweaks.ice_detector.title"),
                            Component.translatable("advancements.solarpancakestweaks.ice_detector.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion("ice_detector", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setCanSeeSky(true),
                            ItemPredicate.Builder.item().of(items, ModItems.ICE_DETECTOR.asItem())))
                    .save(output, Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "solarpancakestweaks/ice_detector"));
        }
    }

}
