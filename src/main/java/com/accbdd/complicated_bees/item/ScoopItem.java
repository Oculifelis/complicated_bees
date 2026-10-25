package com.accbdd.complicated_bees.item;
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
        .component(DataComponents.TOOL,new Tool(List.of(Tool.Rule.minesAndDrops(BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK).getOrThrow(BlockTagGenerator.SCOOPABLE),4)),1,1,false)));}
    @Override public boolean mineBlock(ItemStack stack,Level level,BlockState state,BlockPos pos,LivingEntity miner){
        if(!level.isClientSide())stack.hurtAndBreak(1,miner,EquipmentSlot.MAINHAND);
        return state.is(BlocksRegistration.BEE_NEST.get());
    }
}
