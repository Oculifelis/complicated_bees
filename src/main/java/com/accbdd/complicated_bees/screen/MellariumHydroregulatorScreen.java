package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.recipe.HydroRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Optional;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumHydroregulatorScreen extends AbstractContainerScreen<MellariumHydroregulatorMenu> {
    private final ResourceLocation GUI;
    private final RecipeManager.CachedCheck<Container, HydroRecipe> recipeCheck = RecipeManager.createCheck(EsotericRegistration.HYDROREGULATOR_RECIPE.get());

    public MellariumHydroregulatorScreen(MellariumHydroregulatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.GUI = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/mellarium_hydroregulator.png");
        this.imageHeight = 161;
        this.imageWidth = 176;
        this.inventoryLabelY = imageHeight - 93;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.imageWidth/2 - this.font.width(this.title)/2, this.titleLabelY, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        ItemStack stack = getMenu().getItems().get(0);
        recipeCheck.getRecipeFor(new SimpleContainer(stack), getMenu().getLevel()).ifPresent(recipe -> {
            graphics.blit(GUI, leftPos+82, topPos+27, recipe.getHumidityChange().up > 0 ? 176 : 183, 0, 7, 11);
            if (mouseX > leftPos+81 && mouseX < leftPos+81+9 && mouseY > topPos+24 && mouseY < topPos+24+15) {
                graphics.renderTooltip(this.font,
                        List.of(
                                Component.translatable("jei.complicated_bees.modifier", recipe.getHumidityChange().getTranslationKey()),
                                Component.translatable("jei.complicated_bees.consumption_chance", String.format("%.0f%%", recipe.getUseChance() * 100))
                        ),
                        Optional.empty(),
                        mouseX,
                        mouseY);
            }
        });
        renderTooltip(graphics, mouseX, mouseY);
    }
}
