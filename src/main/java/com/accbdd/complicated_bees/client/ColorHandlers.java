package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.block.BeeNestBlock;
import com.accbdd.complicated_bees.block.entity.BeeNestBlockEntity;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.item.CombItem;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@OnlyIn(Dist.CLIENT)
public class ColorHandlers {
    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> FastColor.ARGB32.opaque(BeeItem.getItemColor(stack, tintIndex)), ItemsRegistration.DRONE.get(), ItemsRegistration.PRINCESS.get(), ItemsRegistration.QUEEN.get());
        event.register((stack, tintIndex) -> FastColor.ARGB32.opaque(CombItem.getItemColor(stack, tintIndex)), ItemsRegistration.COMB.get());
        event.register((stack, tintIndex) -> FastColor.ARGB32.opaque(BeeNestBlock.getItemColor(stack, tintIndex)), ItemsRegistration.BEE_NEST.get());
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level == null ? -1 : FastColor.ARGB32.opaque(BeeNestBlockEntity.getNestColor(state, level, pos, tintIndex)), BlocksRegistration.BEE_NEST.get());
    }
}
