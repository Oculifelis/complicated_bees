package com.accbdd.complicated_bees.port;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
@EventBusSubscriber(modid="complicated_bees")
public final class RecipeSync {
    @SubscribeEvent public static void sync(OnDatapackSyncEvent event){
        event.sendRecipes(EsotericRegistration.CENTRIFUGE_RECIPE.get(),EsotericRegistration.MUTATION_RECIPE.get(),
            EsotericRegistration.MUTATOR_RECIPE.get(),EsotericRegistration.TEMP_UNIT_RECIPE.get(),
            EsotericRegistration.HYDROREGULATOR_RECIPE.get(),EsotericRegistration.HONEY_GENERATOR_RECIPE.get());
    }
}
