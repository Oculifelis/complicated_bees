package com.accbdd.complicated_bees.port;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.state.properties.WoodType;
public final class TargetClient {
    public static void registerWood(WoodType type){Sheets.addWoodType(type);WoodType.register(type);}
    public static void toast(Toast toast){Minecraft.getInstance().getToastManager().addToast(toast);}
    public static Screen screen(){return Minecraft.getInstance().screen;}
    public static void setScreen(Screen screen){Minecraft.getInstance().setScreen(screen);}
}
