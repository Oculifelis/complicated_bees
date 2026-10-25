package com.accbdd.complicated_bees.port;
import java.util.List;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
@EventBusSubscriber(modid="complicated_bees",value=Dist.CLIENT)
public final class ClientRecipeSync {
    @SubscribeEvent public static void recipes(RecipesReceivedEvent event){PortRecipes.clientRecipes=List.copyOf(event.getRecipeMap().values());}
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event){PortRecipes.clientRecipes=List.of();}
}
