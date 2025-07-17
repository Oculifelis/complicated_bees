package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeModel implements IUnbakedGeometry<BeeModel> {
    public static IdentityHashMap<ResourceLocation, Variant> cacheMap = new IdentityHashMap<ResourceLocation, Variant>();

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new Baked();
    }

    public record Variant(BakedModel drone, BakedModel princess, BakedModel queen) {
    }

    public static class Loader implements IGeometryLoader<BeeModel> {
        public static final ResourceLocation ID = ResourceLocation.tryBuild(MODID, "bee_model");
        @Override
        public BeeModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
            return new BeeModel();
        }
    }

    private static class Baked implements BakedModel {

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
            return List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean usesBlockLight() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return null;
        }

        @Override
        public ItemOverrides getOverrides() {
            return new OverrideList();
        }

        public class OverrideList extends ItemOverrides {
            @Nullable
            @Override
            public BakedModel resolve(BakedModel pModel, ItemStack pstack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
                ResourceLocation speciesLoc = GeneticHelper.getSpeciesLoc(pstack);
                if (speciesLoc == null)
                    return pModel;

                Variant variant = cacheMap.computeIfAbsent(speciesLoc, loc -> {
                    BakedModel[] beeModels = new BakedModel[3];
                    Species species = GeneticHelper.getSpecies(pstack, true);
                    for (int i = 0; i < 3; i++) {
                        if (species == null) {
                            beeModels[i] = pModel;
                            continue;
                        }
                        beeModels[i] = Minecraft.getInstance().getModelManager().getModel(species.getModels().get(i));
                    }
                    return new Variant(beeModels[0], beeModels[1], beeModels[2]);
                });
                if (pstack.is(ItemsRegistration.QUEEN.get())) {
                    return variant.queen;
                } else if (pstack.is(ItemsRegistration.PRINCESS.get()))
                    return variant.princess;
                else
                    return variant.drone;
            }
        }
    }

    public static class Builder extends ModelBuilder<Builder> {
        public Builder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
            super(outputLocation, existingFileHelper);
        }

        @Override
        public JsonObject toJson() {
            JsonObject root = new JsonObject();
            root.addProperty("loader", Loader.ID.toString());
            return root;
        }
    }
}
