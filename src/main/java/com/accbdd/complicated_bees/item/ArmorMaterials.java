package com.accbdd.complicated_bees.item;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.*;
public final class ArmorMaterials {
    public static final ArmorMaterial APIARIST=new ArmorMaterial(5,
        Map.of(ArmorType.BOOTS,1,ArmorType.LEGGINGS,2,ArmorType.CHESTPLATE,3,ArmorType.HELMET,1,ArmorType.BODY,3),
        15,SoundEvents.ARMOR_EQUIP_LEATHER,0,0,
        TagKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath("complicated_bees","repairs_apiarist")),
        ResourceKey.create(EquipmentAssets.ROOT_ID,Identifier.fromNamespaceAndPath("complicated_bees","apiarist")));
}
