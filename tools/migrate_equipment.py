from pathlib import Path
import re
root=Path(__file__).resolve().parents[1]
base=root/'complicated_bees/src/main/java/com/accbdd/complicated_bees'
(base/'item/ArmorMaterials.java').write_text('''package com.accbdd.complicated_bees.item;
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
''',encoding='utf-8')
path=base/'registry/ItemsRegistration.java'
s=path.read_text(encoding='utf-8').replace('DeferredItem<ArmorItem>','DeferredItem<Item>')
s=re.sub(r'new ArmorItem\(ArmorMaterials.APIARIST, ArmorItem.Type.(\w+), new Item.Properties\(\).durability\(ArmorItem.Type.\w+.getDurability\(5\)\)\)',r'new Item(new Item.Properties().humanoidArmor(ArmorMaterials.APIARIST, net.minecraft.world.item.equipment.ArmorType.\1))',s)
# Food effects became a consumable component; retain all three guaranteed ambrosia effects.
s=s.replace('            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 1), 1.0F)\n','').replace('            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 1200, 2), 1.0F)\n','').replace('            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1), 1.0F)\n','')
s=s.replace('.build()).rarity(Rarity.RARE)', '.build()).component(DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumables.defaultFood().onConsume(new net.minecraft.world.item.consume_effect.ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION,400,1),new MobEffectInstance(MobEffects.JUMP_BOOST,1200,2),new MobEffectInstance(MobEffects.ABSORPTION,2400,1)),1.0f)).build()).rarity(Rarity.RARE)')
path.write_text(s,encoding='utf-8')
(base/'item/ScoopItem.java').write_text('''package com.accbdd.complicated_bees.item;
import com.accbdd.complicated_bees.datagen.BlockTagGenerator;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
public class ScoopItem extends Item {
    public ScoopItem(Properties properties){super(properties.durability(50).enchantable(14).repairable(ItemTags.WOOL)
        .component(DataComponents.TOOL,new Tool(List.of(Tool.Rule.minesAndDrops(BuiltInRegistries.BLOCK.getOrThrow(BlockTagGenerator.SCOOPABLE),4)),1,1,false)));}
    @Override public boolean mineBlock(ItemStack stack,Level level,BlockState state,BlockPos pos,LivingEntity miner){
        if(!level.isClientSide())stack.hurtAndBreak(1,miner,EquipmentSlot.MAINHAND);
        return state.is(BlocksRegistration.BEE_NEST.get());
    }
}
''',encoding='utf-8')

for name in ['InheritHiveCombFunction','InheritHiveSpeciesFunction']:
 path=base/'loot'/ (name+'.java')
 if not path.exists():path=next(base.rglob(name+'.java'))
 s=path.read_text(encoding='utf-8').replace('import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;\n','')
 s=re.sub(r'public LootItemFunctionType<'+name+r'> getType\(\) \{.*?\}', 'public com.mojang.serialization.MapCodec<'+name+'> codec() { return CODEC; }',s,flags=re.S)
 path.write_text(s,encoding='utf-8')
path=base/'registry/EsotericRegistration.java'
s=path.read_text(encoding='utf-8').replace('import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;','import com.mojang.serialization.MapCodec;\nimport net.minecraft.world.level.storage.loot.functions.LootItemFunction;')
s=s.replace('LootItemFunctionType<?>','MapCodec<? extends LootItemFunction>').replace('LootItemFunctionType<InheritHiveSpeciesFunction>','MapCodec<InheritHiveSpeciesFunction>').replace('LootItemFunctionType<InheritHiveCombFunction>','MapCodec<InheritHiveCombFunction>')
s=re.sub(r'new LootItemFunctionType<>\(([^)]+)\)',r'\1',s)
path.write_text(s,encoding='utf-8')
