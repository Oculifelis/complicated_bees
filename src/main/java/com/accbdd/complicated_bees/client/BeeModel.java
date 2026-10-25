package com.accbdd.complicated_bees.client;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.mojang.serialization.MapCodec;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4fc;

/** Bakes resource-pack bee geometry and selects it using the stack's primary species. */
public record BeeModel() implements ItemModel.Unbaked {
    public static final MapCodec<BeeModel> CODEC = MapCodec.unit(BeeModel::new);
    private static Set<Identifier> models() {
        Set<Identifier> result = new HashSet<>();
        Minecraft.getInstance().getResourceManager().listResources("models/bee", id -> id.getPath().endsWith(".json")).keySet().forEach(id ->
            result.add(id.withPath(id.getPath().substring(7, id.getPath().length()-5))));
        return result;
    }
    public MapCodec<BeeModel> type() { return CODEC; }
    public void resolveDependencies(ResolvableModel.Resolver resolver) { models().forEach(resolver::markDependency); }
    public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
        Map<Identifier,ItemModel> baked=new HashMap<>();
        for (Identifier id: models()) baked.put(id, new CuboidItemModelWrapper.Unbaked(id, Optional.empty(),
            List.of(new ColorHandlers.Tint(0),new ColorHandlers.Tint(1))).bake(context,transformation));
        return (output,stack,resolver,display,level,owner,seed) -> {
            var species=GeneticHelper.getSpecies(stack,true);
            int index=stack.is(ItemsRegistration.QUEEN.get())?2:stack.is(ItemsRegistration.PRINCESS.get())?1:0;
            ItemModel model=species==null?null:baked.get(species.getModels().get(index));
            if(model==null) model=context.missingItemModel(transformation);
            model.update(output,stack,resolver,display,level,owner,seed);
        };
    }
}
