package com.accbdd.complicated_bees.client;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@OnlyIn(Dist.CLIENT)
public class DiscoverToast implements Toast {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/toasts.png");
    private static final long DISPLAY_TIME = 5000L;
    private static final Component TITLE_TEXT = Component.translatable("toast.complicated_bees.new_species");
    private final Component descriptionText;
    private final Species species;
    private long lastChanged;
    private boolean changed;

    public DiscoverToast(Species species) {
        this.species = species;
        descriptionText = GeneticHelper.getTranslationKey(species).append(" - ").append(GeneticHelper.getSpeciesTaxonomyKey(species).withStyle(ChatFormatting.ITALIC));
    }

    public Toast.Visibility render(GuiGraphics pGuiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
        if (this.changed) {
            this.lastChanged = pTimeSinceLastVisible;
            this.changed = false;
        }

        if (species == null) {
            return Visibility.HIDE;
        }

        pGuiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        pGuiGraphics.drawString(pToastComponent.getMinecraft().font, TITLE_TEXT, 30, 7, -11534256, false);
        pGuiGraphics.drawString(pToastComponent.getMinecraft().font, descriptionText, 30, 18, -16777216, false);
        ItemStack drone = species.toStack(ItemsRegistration.DRONE.get());
        pGuiGraphics.renderFakeItem(drone, 8, 8);
        return (double)(pTimeSinceLastVisible - this.lastChanged) >= 5000.0D * pToastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
