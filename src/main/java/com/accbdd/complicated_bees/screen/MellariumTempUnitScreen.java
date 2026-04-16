package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.recipe.TempUnitRecipe;
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

public class MellariumTempUnitScreen extends AbstractContainerScreen<MellariumTempUnitMenu> {
    private final ResourceLocation GUI;
    private final RecipeManager.CachedCheck<Container, TempUnitRecipe> recipeCheck = RecipeManager.createCheck(EsotericRegistration.TEMP_UNIT_RECIPE.get());

    public MellariumTempUnitScreen(MellariumTempUnitMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.GUI = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/mellarium_temp_unit.png");
        this.imageHeight = 161;
        this.imageWidth = 176;
        this.inventoryLabelY = imageHeight - 93;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.imageWidth/2 - this.font.width(this.title)/2, this.titleLabelY, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        ItemStack stack = getMenu().getItems().get(0);
        recipeCheck.getRecipeFor(new SimpleContainer(stack), getMenu().getLevel()).ifPresent(recipe -> {
            switch (recipe.getTempChange()) {
                case DOWN_1 -> drawThermometer(graphics, 0, 0);
                case DOWN_2 -> drawThermometer(graphics, 8, 0);
                case DOWN_3, DOWN_4, DOWN_5 -> drawThermometer(graphics, 16, 0);
                case UP_1 -> drawThermometer(graphics, 0, 16);
                case UP_2 -> drawThermometer(graphics, 8, 16);
                case UP_3, UP_4, UP_5 -> drawThermometer(graphics, 16, 16);
            }
            if (mouseX > leftPos+84 && mouseX < leftPos+84+8 && mouseY > topPos+22 && mouseY < topPos+22+16) {
                graphics.renderTooltip(this.font,
                        List.of(
                                Component.translatable("jei.complicated_bees.modifier", recipe.getTempChange().getTranslationKey()),
                                Component.translatable("jei.complicated_bees.consumption_chance", String.format("%.0f%%", recipe.getUseChance() * 100))
                        ),
                        Optional.empty(),
                        mouseX,
                        mouseY);
            }
        });
        renderTooltip(graphics, mouseX, mouseY);
    }

    private void drawThermometer(GuiGraphics graphics, int uOffset, int vOffset) {
        graphics.blit(GUI, leftPos+84, topPos+22, 176+uOffset, vOffset, 8, 16);
    }
}
