package com.accbdd.complicated_bees.port;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public final class PortGui {
    public static void blit(GuiGraphicsExtractor g,Identifier id,int x,int y,int u,int v,int width,int height){
        g.blit(RenderPipelines.GUI_TEXTURED,id,x,y,u,v,width,height,256,256);
    }
    public static void blit(GuiGraphicsExtractor g,Identifier id,int x,int y,float u,float v,int width,int height,int tw,int th){
        g.blit(RenderPipelines.GUI_TEXTURED,id,x,y,u,v,width,height,tw,th);
    }
    public static void tooltip(GuiGraphicsExtractor g,Font font,Component text,int x,int y){g.setTooltipForNextFrame(font,text,x,y);}
    public static void tooltip(GuiGraphicsExtractor g,Font font,List<Component> text,int x,int y){g.setTooltipForNextFrame(font,text,Optional.empty(),x,y);}
    public static void tooltip(GuiGraphicsExtractor g,Font font,List<Component> text,Optional<TooltipComponent> image,int x,int y){g.setTooltipForNextFrame(font,text,image,x,y);}
    public static void tooltip(GuiGraphicsExtractor g,Font font,List<Component> text,Optional<TooltipComponent> image,ItemStack stack,int x,int y){g.setTooltipForNextFrame(font,text,image,x,y);}
}
