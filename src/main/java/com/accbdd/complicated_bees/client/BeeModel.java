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
import net.minecraft.nbt.CompoundTag;
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

import java.util.*;
import java.util.function.Function;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeModel implements IUnbakedGeometry<BeeModel> {
    public static HashMap<ResourceLocation, Variant> cacheMap = new HashMap<ResourceLocation, Variant>();

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
            return OverrideList.INSTANCE;
        }

        public static class OverrideList extends ItemOverrides {
            public static OverrideList INSTANCE = new OverrideList();
            private static final Map<Integer, BakedModel> stackCache = new LinkedHashMap<>() {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, BakedModel> eldest) {
                    return this.size() > 2000; //how big to make this? idk :(
                }
            };

            @Nullable
            @Override
            public BakedModel resolve(BakedModel pModel, ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
                int stackHash = getStackHash(pStack);
                BakedModel cached = stackCache.get(stackHash);
                if (cached != null) return cached;

                ResourceLocation speciesLoc = GeneticHelper.getSpeciesLoc(pStack);
                if (speciesLoc == null) {
                    Minecraft.getInstance().getModelManager().getModel(Species.DEFAULT_MODELS.get(0));
                }

                Variant variant = cacheMap.get(speciesLoc);

                if (variant == null) {
                    Species species = GeneticHelper.getSpecies(pStack, true);
                    if (species == null) {
                        variant = new Variant(Minecraft.getInstance().getModelManager().getMissingModel(), Minecraft.getInstance().getModelManager().getMissingModel(), Minecraft.getInstance().getModelManager().getMissingModel());
                    } else {
                        BakedModel droneModel = Minecraft.getInstance().getModelManager().getModel(species.getModels().get(0));
                        BakedModel princessModel = Minecraft.getInstance().getModelManager().getModel(species.getModels().get(1));
                        BakedModel queenModel = Minecraft.getInstance().getModelManager().getModel(species.getModels().get(2));
                        variant = new Variant(droneModel, princessModel, queenModel);
                    }
                    cacheMap.put(speciesLoc, variant);
                }

                if (pStack.is(ItemsRegistration.QUEEN.get())) {
                    cached = variant.queen;
                } else if (pStack.is(ItemsRegistration.PRINCESS.get()))
                    cached = variant.princess;
                else
                    cached = variant.drone;

                stackCache.put(stackHash, cached);
                return cached;
            }

            private int getStackHash(ItemStack stack) {
                CompoundTag tag = stack.getTag();
                String species = tag != null && tag.contains("species") ? tag.getString("species") : "none";
                int type = stack.is(ItemsRegistration.QUEEN.get()) ? 2 :
                        stack.is(ItemsRegistration.PRINCESS.get()) ? 1 : 0;
                return Objects.hash(species, type);
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
