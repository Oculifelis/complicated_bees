package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.screen.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MenuRegistration {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);

    public static final Supplier<MenuType<CentrifugeMenu>> CENTRIFUGE_MENU = MENU_TYPES.register("centrifuge",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new CentrifugeMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<ApiaryMenu>> APIARY_MENU = MENU_TYPES.register("apiary",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new ApiaryMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<MellariumMenu>> MELLARIUM_MENU = MENU_TYPES.register("mellarium",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<GyrofugeMenu>> GYROFUGE_MENU = MENU_TYPES.register("gyrofuge",
            () -> IMenuTypeExtension.create(((windowId, inv, data) -> new GyrofugeMenu(windowId, inv.player, data.readBlockPos()))));
    public static final Supplier<MenuType<AbstractGeneratorMenu>> FURNACE_GENERATOR_MENU = MENU_TYPES.register("furnace_generator",
            () -> IMenuTypeExtension.create(((windowId, inv, data) -> new FurnaceGeneratorMenu(windowId, inv.player, data.readBlockPos()))));
    public static final Supplier<MenuType<AbstractGeneratorMenu>> HONEY_GENERATOR_MENU = MENU_TYPES.register("honey_generator",
            () -> IMenuTypeExtension.create(((windowId, inv, data) -> new HoneyGeneratorMenu(windowId, inv.player, data.readBlockPos()))));
    public static final Supplier<MenuType<MicroscopeMenu>> MICROSCOPE_MENU = MENU_TYPES.register("microscope",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MicroscopeMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<AnalyzerMenu>> ANALYZER_MENU = MENU_TYPES.register("analyzer",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> AnalyzerMenu.fromNetwork(windowId, inv)));
    public static final Supplier<MenuType<LibraryMenu>> LIBRARY_MENU = MENU_TYPES.register("library",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new LibraryMenu(windowId, inv.player)));
    public static final Supplier<MenuType<MellariumFrameHousingMenu>> MELLARIUM_FRAME_MENU_1 = MENU_TYPES.register("mellarium_frame_1",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumFrameHousingMenu(windowId, inv.player, data.readBlockPos(), 1)));
    public static final Supplier<MenuType<MellariumFrameHousingMenu>> MELLARIUM_FRAME_MENU_2 = MENU_TYPES.register("mellarium_frame_2",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumFrameHousingMenu(windowId, inv.player, data.readBlockPos(), 2)));
    public static final Supplier<MenuType<MellariumFrameHousingMenu>> MELLARIUM_FRAME_MENU_3 = MENU_TYPES.register("mellarium_frame_3",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumFrameHousingMenu(windowId, inv.player, data.readBlockPos(), 3)));
    public static final List<Supplier<MenuType<MellariumFrameHousingMenu>>> MELLARIUM_FRAME_MENUS = List.of(MELLARIUM_FRAME_MENU_1, MELLARIUM_FRAME_MENU_2, MELLARIUM_FRAME_MENU_3);
    public static final Supplier<MenuType<MellariumTempUnitMenu>> MELLARIUM_TEMP_UNIT_MENU = MENU_TYPES.register("mellarium_temp_unit",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumTempUnitMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<MellariumMutatorMenu>> MELLARIUM_MUTATOR_MENU = MENU_TYPES.register("mellarium_mutator",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumMutatorMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<MellariumHydroregulatorMenu>> MELLARIUM_HYDROREGULATOR_MENU = MENU_TYPES.register("mellarium_hydroregulator",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MellariumHydroregulatorMenu(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<BeeSorterMenu>> BEE_SORTER_MENU = MENU_TYPES.register("bee_sorter",
            () -> IMenuTypeExtension.create(((windowId, inv, data) -> new BeeSorterMenu(windowId, inv, data.readBlockPos(), data.readByteArray()))));
    public static final Supplier<MenuType<AutolyzerMenu>> AUTOLYZER_MENU = MENU_TYPES.register("autolyzer",
            () -> IMenuTypeExtension.create((((windowId, inv, data) -> new AutolyzerMenu(windowId, inv.player, data.readBlockPos())))));


}
