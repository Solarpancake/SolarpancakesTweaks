package de.solarpancake.tweaks.item;

import com.google.common.collect.Maps;
import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.tags.ModTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.Map;

public class ModArmorMaterials {
    public static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<EquipmentAsset> ICED_CRYSTAL_KEY = ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "iced_crystal"));

    public static final ArmorMaterial ICED_CRYSTAL_ARMOR_MATERIAL = new ArmorMaterial(1200,
            makeDefense(5, 7, 9, 5, 11), 16, SoundEvents.ARMOR_EQUIP_NETHERITE,
            2f, 0.1f, ModTags.Items.ICED_CRYSTAL_REPAIRABLE, ICED_CRYSTAL_KEY);


    public static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(
                Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body)
        );
    }
}
