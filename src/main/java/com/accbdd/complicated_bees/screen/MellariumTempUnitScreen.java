package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.recipe.TempUnitRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumTempUnitScreen extends AbstractContainerScreen<MellariumTempUnitMenu> {
    private final ResourceLocation GUI;
    private final RecipeManager.CachedCheck<Container, TempUnitRecipe> recipeCheck = RecipeManager.createCheck(EsotericRegistration.TEMP_UNIT_RECIPE.get());

    public MellariumTempUnitScreen(MellariumTempUnitMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.GUI = new ResourceLocation(MODID, "textures/gui/mellarium_single_slot.png");
        this.imageHeight = 143;
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        ItemStack stack = getMenu().getItems().get(0);
        var recipe = recipeCheck.getRecipeFor(new SimpleContainer(stack), getMenu().getLevel());
        if (recipe.isPresent()) {
            switch (recipe.get().getTempChange()) {
                case DOWN_1 -> graphics.blit(GUI, leftPos + 100, topPos + 24, 176, 0, 16, 16);
                case DOWN_2 -> graphics.blit(GUI, leftPos + 100, topPos + 24, 176, 16, 16, 16);
                case DOWN_3, DOWN_4, DOWN_5 -> graphics.blit(GUI, leftPos + 100, topPos + 24, 176, 32, 16, 16);
                case UP_1 -> graphics.blit(GUI, leftPos + 100, topPos + 24, 192, 0, 16, 16);
                case UP_2 -> graphics.blit(GUI, leftPos + 100, topPos + 24, 192, 16, 16, 16);
                case UP_3, UP_4, UP_5 -> graphics.blit(GUI, leftPos + 100, topPos + 24, 192, 32, 16, 16);
            }
            graphics.drawString(Minecraft.getInstance().font, String.format("%.0f%%", recipe.get().getUseChance() * 100), leftPos + 120, topPos + 28, 0x404040, false);
        }

        renderTooltip(graphics, mouseX, mouseY);
    }
}
