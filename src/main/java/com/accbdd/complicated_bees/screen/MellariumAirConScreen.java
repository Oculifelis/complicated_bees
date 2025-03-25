package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumAirConScreen extends AbstractContainerScreen<MellariumAirConMenu> {
    private final ResourceLocation GUI;

    public MellariumAirConScreen(MellariumAirConMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.GUI = new ResourceLocation(MODID, "textures/gui/mellarium_air_con.png");
        this.imageHeight = 143;
        this.imageWidth = 176;
        this.inventoryLabelY = this.imageHeight - 10000;
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
        if (stack.is(ItemTagGenerator.AIR_CON_COOLING_1))
            graphics.blit(GUI, leftPos + 100, topPos + 24, 176, 0, 16, 16);
        else if (stack.is(ItemTagGenerator.AIR_CON_COOLING_2))
            graphics.blit(GUI, leftPos + 100, topPos + 24, 176, 16, 16, 16);
        else if (stack.is(ItemTagGenerator.AIR_CON_COOLING_3))
            graphics.blit(GUI, leftPos + 100, topPos + 24, 176, 32, 16, 16);
        else if (stack.is(ItemTagGenerator.AIR_CON_HEATING_1))
            graphics.blit(GUI, leftPos + 100, topPos + 24, 192, 0, 16, 16);
        else if (stack.is(ItemTagGenerator.AIR_CON_HEATING_2))
            graphics.blit(GUI, leftPos + 100, topPos + 24, 192, 16, 16, 16);
        else if (stack.is(ItemTagGenerator.AIR_CON_HEATING_3))
            graphics.blit(GUI, leftPos + 100, topPos + 24, 192, 32, 16, 16);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
