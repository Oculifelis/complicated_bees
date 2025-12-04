package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.mutation.Mutation;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@OnlyIn(Dist.CLIENT)
public class ResearchToast implements Toast {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/toasts.png");
    private static final long DISPLAY_TIME = 5000L;
    private static final Component TITLE_TEXT = Component.translatable("toast.complicated_bees.research");
    private final Component descriptionText;
    private final Mutation mutation;
    private long lastChanged;
    private boolean changed;

    public ResearchToast(Mutation mutation) {
        this.mutation = mutation;
        descriptionText = GeneticHelper.getTranslationKey(this.mutation.getResultSpecies());
    }

    public Visibility render(GuiGraphics pGuiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
        if (this.changed) {
            this.lastChanged = pTimeSinceLastVisible;
            this.changed = false;
        }

        pGuiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        pGuiGraphics.drawString(pToastComponent.getMinecraft().font, TITLE_TEXT, 30, 7, -11534256, false);
        pGuiGraphics.drawString(pToastComponent.getMinecraft().font, descriptionText, 30, 18, -16777216, false);
        ItemStack drone = mutation.getResultSpecies().toStack(ItemsRegistration.DRONE.get());
        pGuiGraphics.renderFakeItem(drone, 8, 8);
        return (double)(pTimeSinceLastVisible - this.lastChanged) >= 5000.0D * pToastComponent.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }
}
