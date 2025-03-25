package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.screen.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MenuRegistration {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    public static final Supplier<MenuType<CentrifugeMenu>> CENTRIFUGE_MENU = MENU_TYPES.register("centrifuge",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CentrifugeMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<ApiaryMenu>> APIARY_MENU = MENU_TYPES.register("apiary",
            () -> IForgeMenuType.create((windowId, inv, data) -> new ApiaryMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<MellariumMenu>> MELLARIUM_MENU = MENU_TYPES.register("mellarium",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MellariumMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<GeneratorMenu>> GENERATOR_MENU = MENU_TYPES.register("generator",
            () -> IForgeMenuType.create(((windowId, inv, data) -> new GeneratorMenu(windowId, inv.player, data.readBlockPos()))));
    public static final Supplier<MenuType<MicroscopeMenu>> MICROSCOPE_MENU = MENU_TYPES.register("microscope",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MicroscopeMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<AnalyzerMenu>> ANALYZER_MENU = MENU_TYPES.register("analyzer",
            () -> IForgeMenuType.create((windowId, inv, data) -> AnalyzerMenu.fromNetwork(windowId, inv)));
    public static final Supplier<MenuType<LibraryMenu>> LIBRARY_MENU = MENU_TYPES.register("library",
            () -> IForgeMenuType.create((windowId, inv, data) -> new LibraryMenu(windowId, inv.player)));
    public static final Supplier<MenuType<MellariumFrameHousingMenu>> MELLARIUM_FRAME_MENU_1 = MENU_TYPES.register("mellarium_frame_1",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MellariumFrameHousingMenu(windowId, inv.player, data.readBlockPos(), 1)));
    public static final Supplier<MenuType<MellariumFrameHousingMenu>> MELLARIUM_FRAME_MENU_2 = MENU_TYPES.register("mellarium_frame_2",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MellariumFrameHousingMenu(windowId, inv.player, data.readBlockPos(), 2)));
    public static final Supplier<MenuType<MellariumFrameHousingMenu>> MELLARIUM_FRAME_MENU_3 = MENU_TYPES.register("mellarium_frame_3",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MellariumFrameHousingMenu(windowId, inv.player, data.readBlockPos(), 3)));
    public static final List<Supplier<MenuType<MellariumFrameHousingMenu>>> MELLARIUM_FRAME_MENUS = List.of(MELLARIUM_FRAME_MENU_1, MELLARIUM_FRAME_MENU_2, MELLARIUM_FRAME_MENU_3);
    public static final Supplier<MenuType<MellariumTempUnitMenu>> MELLARIUM_TEMP_UNIT_MENU = MENU_TYPES.register("mellarium_temp_unit",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MellariumTempUnitMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<MellariumMutatorMenu>> MELLARIUM_MUTATOR_MENU = MENU_TYPES.register("mellarium_mutator",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MellariumMutatorMenu(windowId, inv.player, data.readBlockPos())));


}
