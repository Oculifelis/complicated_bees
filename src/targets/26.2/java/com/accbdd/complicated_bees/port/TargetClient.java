package com.accbdd.complicated_bees.port;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.state.properties.WoodType;
public final class TargetClient {
    public static void registerWood(WoodType type){WoodType.register(type);}
    public static void toast(Toast toast){Minecraft.getInstance().gui.toastManager().addToast(toast);}
    public static Screen screen(){return Minecraft.getInstance().gui.screen();}
    public static void setScreen(Screen screen){Minecraft.getInstance().gui.setScreen(screen);}
}
