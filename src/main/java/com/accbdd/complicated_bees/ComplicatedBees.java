package com.accbdd.complicated_bees;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.effect.IBeeEffect;
import com.accbdd.complicated_bees.bees.gene.IGene;
import com.accbdd.complicated_bees.bees.mutation.Mutation;
import com.accbdd.complicated_bees.bees.mutation.condition.IMutationCondition;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.block.BeeNestBlock;
import com.accbdd.complicated_bees.block.entity.renderer.MicroscopeBlockEntityRenderer;
import com.accbdd.complicated_bees.client.BeeModel;
import com.accbdd.complicated_bees.client.ColorHandlers;
import com.accbdd.complicated_bees.client.OptimizedBeeModelLoader;
import com.accbdd.complicated_bees.command.ModCommands;
import com.accbdd.complicated_bees.config.CommonConfig;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.datagen.DataGenerators;
import com.accbdd.complicated_bees.datagen.condition.ItemEnabledCondition;
import com.accbdd.complicated_bees.event.ComplicatedBeesEvents;
import com.accbdd.complicated_bees.item.CombItem;
import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.TrackerSyncClientbound;
import com.accbdd.complicated_bees.particle.BeeParticle;
import com.accbdd.complicated_bees.registry.*;
import com.accbdd.complicated_bees.screen.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.*;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mod(ComplicatedBees.MODID)
public class ComplicatedBees {
    public static final String MODID = "complicated_bees";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static Supplier<Registry<IGene<?>>> GENE_REGISTRY;
    public static Supplier<Registry<IBeeEffect>> BEE_EFFECT_REGISTRY;
    public static Supplier<Registry<IMutationCondition>> MUTATION_CONDITION_REGISTRY;

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BEES_TAB = CREATIVE_MODE_TABS.register("complicated_bees", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.complicated_bees"))
            .icon(() -> ItemsRegistration.DRONE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (DeferredItem<?> item : ItemsRegistration.CREATIVE_TAB_ITEMS) {
                    output.accept((Item) item.get());
                }
                RegistryAccess access = GeneticHelper.getRegistryAccess();
                if (access != null) {
                    Set<Map.Entry<ResourceKey<Species>, Species>> speciesSet = access.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().entrySet();
                    for (Map.Entry<ResourceKey<Species>, Species> entry : speciesSet) {
                        if (entry.getValue().getColor() != -1)
                            output.acceptAll(entry.getValue().toMembers());
                    }
                    for (ResourceLocation id : access.registry(CombRegistration.COMB_REGISTRY_KEY).get().keySet()) {
                        output.accept(CombItem.setComb(ItemsRegistration.COMB.get().getDefaultInstance(), id));
                    }
                    for (Map.Entry<ResourceKey<Species>, Species> entry : speciesSet) {
                        output.accept(BeeNestBlock.stackNest(ItemsRegistration.BEE_NEST.get().getDefaultInstance(), entry.getValue()));
                    }
                }
            }).build());

    public ComplicatedBees(ModContainer modContainer, IEventBus modEventBus) {
        modEventBus.addListener(this::registerSerializers);
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerDatapackRegistries);
        modEventBus.addListener(DataGenerators::generate);
        modEventBus.addListener(PacketHandler::registerPayloadHandlers);

        if(FMLLoader.getDist().isClient()) {
            modEventBus.addListener(ColorHandlers::registerItemColorHandlers);
            modEventBus.addListener(ColorHandlers::registerBlockColorHandlers);
        }

        BlocksRegistration.BLOCKS.register(modEventBus);
        ItemsRegistration.ITEMS.register(modEventBus);
        BlockEntitiesRegistration.BLOCK_ENTITIES.register(modEventBus);
        MenuRegistration.MENU_TYPES.register(modEventBus);
        GeneRegistration.GENES.register(modEventBus);
        BeeEffectRegistration.EFFECTS.register(modEventBus);
        MutationRegistration.MUTATION_CONDITIONS.register(modEventBus);
        EntitiesRegistration.ENTITY_TYPE.register(modEventBus);
        EsotericRegistration.LOOT_ITEM_FUNCTION_REGISTER.register(modEventBus);
        EsotericRegistration.TREE_DECORATOR_REGISTER.register(modEventBus);
        EsotericRegistration.FEATURE_REGISTER.register(modEventBus);
        EsotericRegistration.RECIPE_TYPE_REGISTER.register(modEventBus);
        EsotericRegistration.RECIPE_SERIALIZER_REGISTER.register(modEventBus);
        EsotericRegistration.PARTICLE_TYPE.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(ComplicatedBeesEvents::onItemPickup);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC);

        CREATIVE_MODE_TABS.register(modEventBus);
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        // TODO
    }

    @SubscribeEvent
    public void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                SpeciesRegistration.SPECIES_REGISTRY_KEY,
                Species.SPECIES_CODEC,
                Species.SPECIES_CODEC
        );

        event.dataPackRegistry(
                CombRegistration.COMB_REGISTRY_KEY,
                Comb.CODEC,
                Comb.CODEC
        );

        event.dataPackRegistry(
                MutationRegistration.MUTATION_REGISTRY_KEY,
                Mutation.MUTATION_CODEC,
                Mutation.MUTATION_CODEC
        );

        event.dataPackRegistry(
                FlowerRegistration.FLOWER_REGISTRY_KEY,
                FlowerRegistration.CODEC,
                FlowerRegistration.CODEC
        );
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event) {
        var geneRegistry = event.create(GeneRegistration.GENE_REGISTRY);
        var beeEffectRegistry = event.create(BeeEffectRegistration.BEE_EFFECT_REGISTRY);
        var mutationConditionRegistry = event.create(MutationRegistration.MUTATION_CONDITION_REGISTRY);
        GENE_REGISTRY = () -> geneRegistry;
        BEE_EFFECT_REGISTRY = () -> beeEffectRegistry;
        MUTATION_CONDITION_REGISTRY = () -> mutationConditionRegistry;
    }

    @SubscribeEvent
    public void registerSerializers(RegisterEvent event) {
        event.register(NeoForgeRegistries.CONDITION_SERIALIZERS.key(),
                helper -> helper.register(ResourceLocation.fromNamespaceAndPath(MODID, "item_enabled"), ItemEnabledCondition.CODEC));
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event) {
        LOGGER.info("Registered {} species", ServerLifecycleHooks.getCurrentServer().registryAccess().registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().size());
        LOGGER.info("Registered {} combs", ServerLifecycleHooks.getCurrentServer().registryAccess().registry(CombRegistration.COMB_REGISTRY_KEY).get().size());
        LOGGER.info("Registered {} mutations", ServerLifecycleHooks.getCurrentServer().registryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get().size());
        LOGGER.info("Registered {} flowers", ServerLifecycleHooks.getCurrentServer().registryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).get().size());
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.info("syncing tracker to {}", event.getEntity().getName());
        PacketDistributor.sendToPlayer(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(event.getEntity().getUUID()), new TrackerSyncClientbound(BreedingTracker.getTracker(event.getEntity())));
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EntitiesRegistration.BEE_STAFF_MOUNT.get(), (context) -> new ThrownItemRenderer<>(context, 1.0f, true));

            event.enqueueWork(() -> {
                Sheets.addWoodType(BlocksRegistration.HONEYED_WOOD);
                WoodType.register(BlocksRegistration.HONEYED_WOOD);
            });
        }
        
        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(MenuRegistration.CENTRIFUGE_MENU.get(), CentrifugeScreen::new);
            event.register(MenuRegistration.APIARY_MENU.get(), ApiaryScreen::new);
            event.register(MenuRegistration.MELLARIUM_MENU.get(), MellariumScreen::new);
            event.register(MenuRegistration.MELLARIUM_FRAME_MENUS.get(0).get(), MellariumFrameHousingScreen::new);
            event.register(MenuRegistration.MELLARIUM_FRAME_MENUS.get(1).get(), MellariumFrameHousingScreen::new);
            event.register(MenuRegistration.MELLARIUM_FRAME_MENUS.get(2).get(), MellariumFrameHousingScreen::new);
            event.register(MenuRegistration.MELLARIUM_TEMP_UNIT_MENU.get(), MellariumTempUnitScreen::new);
            event.register(MenuRegistration.MELLARIUM_MUTATOR_MENU.get(), MellariumMutatorScreen::new);
            event.register(MenuRegistration.MELLARIUM_HYDROREGULATOR_MENU.get(), MellariumHydroregulatorScreen::new);
            event.register(MenuRegistration.FURNACE_GENERATOR_MENU.get(), FurnaceGeneratorScreen::new);
            event.register(MenuRegistration.HONEY_GENERATOR_MENU.get(), HoneyGeneratorScreen::new);
            event.register(MenuRegistration.ANALYZER_MENU.get(), AnalyzerScreen::new);
            event.register(MenuRegistration.MICROSCOPE_MENU.get(), MicroscopeScreen::new);
            event.register(MenuRegistration.LIBRARY_MENU.get(), LibraryScreen::new);
            event.register(MenuRegistration.BEE_SORTER_MENU.get(), BeeSorterScreen::new);
            event.register(MenuRegistration.GYROFUGE_MENU.get(), GyrofugeScreen::new);
            event.register(MenuRegistration.AUTOLYZER_MENU.get(), AnalyzerScreen::new);
        }

        @SubscribeEvent
        public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(OptimizedBeeModelLoader.ID, new OptimizedBeeModelLoader());
            event.register(BeeModel.Loader.ID, new BeeModel.Loader());
        }

        @SubscribeEvent
        public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
            ResourceManager manager = Minecraft.getInstance().getResourceManager();
            try {
                for (Map.Entry<ResourceLocation, Resource> entry : manager.listResources("models/bee", res -> res.getPath().endsWith(".json")).entrySet()) {
                    String path = entry.getKey().getPath(); // e.g. models/item/custom/fire_wand.json
                    String modelPath = path.substring("models/".length(), path.length() - ".json".length());
                    event.register(ModelResourceLocation.inventory(ResourceLocation.tryBuild(entry.getKey().getNamespace(), modelPath)));
                    ComplicatedBees.LOGGER.debug("Loaded bee model: " + entry.getKey());
                }
            } catch (Exception e) {
                ComplicatedBees.LOGGER.error("Failed to read bee models: " + e);
            }
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(EsotericRegistration.BEE_PARTICLE.get(),
                    BeeParticle.Provider::new);
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BlockEntitiesRegistration.MICROSCOPE_BLOCK_ENTITY.get(), MicroscopeBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(BlockEntitiesRegistration.CB_SIGN_ENTITY.get(), SignRenderer::new);
            event.registerBlockEntityRenderer(BlockEntitiesRegistration.CB_HANGING_SIGN_ENTITY.get(), HangingSignRenderer::new);
        }
    }
}
