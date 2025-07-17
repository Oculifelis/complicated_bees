package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class OptimizedBeeModelLoader implements IGeometryLoader<OptimizedBeeModelLoader.BeeGeometry> {
    public static final ResourceLocation ID = ResourceLocation.tryBuild(MODID, "optimized_bee_model");
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

    private static class BeeModel extends BakedModelWrapper<BakedModel>
    {
        private final BakedModel model;

        BeeModel(BakedModel baked)
        {
            super(baked);
            this.model = baked;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand)
        {
            if (side == null)
            {
                return model.getQuads(state, side, rand);
            }
            return List.of();
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext ctx, PoseStack poseStack, boolean applyLeftHandTransform)
        {
            getTransforms().getTransform(ctx).apply(applyLeftHandTransform, poseStack);
            return this;
        }

        @Override
        public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous)
        {
            return List.of(this);
        }
    }

    private static class BeeOverrideList extends ItemOverrides {
        public static final IdentityHashMap<ResourceLocation, Variant> cacheMap = new IdentityHashMap<>();

        public BeeOverrideList() {

        }

        @Nullable
        @Override
        public BakedModel resolve(BakedModel bakedModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            ResourceLocation location = GeneticHelper.getSpeciesLoc(stack);
            if (location == null)
                return bakedModel;
            cacheMap.computeIfAbsent(location, loc -> {
                BakedModel[] beeModels = new BakedModel[3];
                Species species = GeneticHelper.getSpecies(stack, true);
                for (int i = 0; i < 3; i++) {
                    if (species == null) {
                        beeModels[i] = bakedModel;
                        continue;
                    }
                    beeModels[i] = new BeeModel(Minecraft.getInstance().getModelManager().getModel(species.getModels().get(i)));
                }
                return new Variant(beeModels[0], beeModels[1], beeModels[2]);
            });
            if (stack.is(ItemsRegistration.QUEEN.get())) {
                return cacheMap.get(location).queen;
            } else if (stack.is(ItemsRegistration.PRINCESS.get()))
                return cacheMap.get(location).princess;
            else
                return cacheMap.get(location).drone;
        }
    }
}
