package com.accbdd.complicated_bees.port;
import java.util.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/** Full machine recipes are synchronized separately because vanilla sends only displays. */
public final class PortRecipes {
    public static List<RecipeHolder<?>> clientRecipes=List.of();
    public static Access manager(Level level){return new Access(level.getServer()==null?RecipeMap.create(clientRecipes):level.getServer().getRecipeManager().recipeMap());}
    public record Access(RecipeMap map){
        public <I extends RecipeInput,T extends Recipe<I>> List<RecipeHolder<T>> getAllRecipesFor(RecipeType<T> type){return List.copyOf(map.byType(type));}
        public <I extends RecipeInput,T extends Recipe<I>> List<RecipeHolder<T>> getRecipesFor(RecipeType<T> type,I input,Level level){return map.getRecipesFor(type,input,level).toList();}
        public <I extends RecipeInput,T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> type,I input,Level level){return map.getRecipesFor(type,input,level).findFirst();}
        public Optional<RecipeHolder<?>> byKey(net.minecraft.resources.Identifier id){return Optional.ofNullable(map.byKey(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE,id)));}
        public Collection<RecipeHolder<?>> getRecipes(){return map.values();}
    }
    public static <I extends RecipeInput,T extends Recipe<I>> Check<I,T> createCheck(RecipeType<T> type){return new Check<>(type);}
    public record Check<I extends RecipeInput,T extends Recipe<I>>(RecipeType<T> type){
        public Optional<RecipeHolder<T>> getRecipeFor(I input,Level level){return manager(level).getRecipeFor(type,input,level);}
    }
}
