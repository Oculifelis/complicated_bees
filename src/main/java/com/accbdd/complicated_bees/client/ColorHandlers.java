package com.accbdd.complicated_bees.client;
import com.accbdd.complicated_bees.block.BeeNestBlock;
import com.accbdd.complicated_bees.block.entity.BeeNestBlockEntity;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import java.util.List;

public final class ColorHandlers {
    public record Tint(int index) implements ItemTintSource {
        public static final MapCodec<Tint> CODEC=Codec.INT.optionalFieldOf("index",0).xmap(Tint::new,Tint::index);
        public MapCodec<Tint> type(){return CODEC;}
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity owner){
            return ARGB.opaque(stack.getItem() instanceof BeeItem?BeeItem.getItemColor(stack,index):
                stack.getItem() instanceof CombItem?CombItem.getItemColor(stack,index):BeeNestBlock.getItemColor(stack,index));
        }
    }
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.ItemTintSources event){
        event.register(Identifier.fromNamespaceAndPath("complicated_bees","color"),Tint.CODEC);
    }
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.BlockTintSources event){
        event.register(List.of(new BlockTintSource(){public int color(BlockState state){return -1;}},new BlockTintSource(){
            public int color(BlockState state){return -1;}
            public int colorInWorld(BlockState state,BlockAndTintGetter level,BlockPos pos){
                return ARGB.opaque(BeeNestBlockEntity.getNestColor(state,level,pos,1));
            }
        }),BlocksRegistration.BEE_NEST.get());
    }
}
