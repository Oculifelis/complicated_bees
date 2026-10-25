package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;
public class ResearchToast implements Toast {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/gui/toasts.png");
    private static final long DISPLAY_TIME = 5000L;
    private static final Component TITLE_TEXT = Component.translatable("toast.complicated_bees.research");
    private final Component descriptionText;
    private final MutationRecipe mutation;
    private long lastChanged;
    private boolean changed;

    public ResearchToast(MutationRecipe mutation) {
        this.mutation = mutation;
        descriptionText = GeneticHelper.getTranslationKey(this.mutation.getResultSpecies());
    }

    private Visibility wanted = Visibility.SHOW;
    public Visibility getWantedVisibility(){return wanted;}
    public void update(ToastManager manager,long visibleMs){
        wanted=visibleMs>=5000*manager.getNotificationDisplayTimeMultiplier()?Visibility.HIDE:Visibility.SHOW;
    }
    public void extractRenderState(GuiGraphicsExtractor graphics,Font font,long visibleMs){
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,TEXTURE,0,0,0,0,width(),height(),256,256);
        graphics.text(font,TITLE_TEXT,30,7,-11534256,false);
        graphics.text(font,descriptionText,30,18,-16777216,false);
        graphics.fakeItem(mutation.getResultSpecies().toStack(ItemsRegistration.DRONE.get()),8,8);
    }
}
