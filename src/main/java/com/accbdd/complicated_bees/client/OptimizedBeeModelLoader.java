package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.function.Function;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class OptimizedBeeModelLoader implements IGeometryLoader<OptimizedBeeModelLoader.BeeGeometry> {
    public static final ResourceLocation ID = new ResourceLocation(MODID, "optimized_bee_model");
    @Override
    public BeeGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new BeeGeometry(deserializationContext.deserialize(jsonObject.get("base_model"), BlockModel.class));
    }

    public record Variant(BakedModel drone, BakedModel princess, BakedModel queen) {
    }

    static class BeeGeometry implements IUnbakedGeometry<BeeGeometry> {
        private final UnbakedModel model;

        BeeGeometry(UnbakedModel model) {
            this.model = model;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            BakedModel bakedModel = model.bake(baker, spriteGetter, modelState, modelLocation);
            return new BeeOverrideModel(bakedModel);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            model.resolveParents(modelGetter);
        }
    }

    private static class BeeOverrideModel extends BakedModelWrapper<BakedModel>
    {
        private final ItemOverrides overrideList;

        BeeOverrideModel(BakedModel originalModel)
        {
            super(originalModel);
            this.overrideList = new BeeOverrideList();
        }

        @Override
        public ItemOverrides getOverrides()
        {
            return overrideList;
        }
    }

    private static class BeeOverrideList extends ItemOverrides {
        public final IdentityHashMap<Species, Variant> cacheMap = new IdentityHashMap<>();

        @Nullable
        @Override
        public BakedModel resolve(BakedModel bakedModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            Species species = GeneticHelper.getSpecies(stack, true);
            cacheMap.computeIfAbsent(species, spec -> {
                BakedModel[] bakedModels = new BakedModel[3];
                for (int i = 0; i < 3; i++) {
                    ResourceLocation modelLoc = species.getModels().get(i);
                    bakedModels[i] = Minecraft.getInstance().getModelManager().getModel(modelLoc);
                }
                return new Variant(bakedModels[0], bakedModels[1], bakedModels[2]);
            });
            if (stack.is(ItemsRegistration.QUEEN.get()))
                return cacheMap.get(species).queen;
            else if (stack.is(ItemsRegistration.PRINCESS.get()))
                return cacheMap.get(species).princess;
            else
                return cacheMap.get(species).drone;
        }
    }
}
