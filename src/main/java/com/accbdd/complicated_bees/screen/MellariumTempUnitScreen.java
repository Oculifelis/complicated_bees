package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.recipe.TempUnitRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumTempUnitScreen extends AbstractContainerScreen<MellariumTempUnitMenu> {
    private final Identifier GUI;
    private final com.accbdd.complicated_bees.port.PortRecipes.Check<RecipeInput, TempUnitRecipe> recipeCheck = com.accbdd.complicated_bees.port.PortRecipes.createCheck(EsotericRegistration.TEMP_UNIT_RECIPE.get());

    public MellariumTempUnitScreen(MellariumTempUnitMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 161);
        this.GUI = Identifier.fromNamespaceAndPath(MODID, "textures/gui/mellarium_temp_unit.png");
        this.inventoryLabelY = imageHeight - 93;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY) {
        pGuiGraphicsExtractor.text(this.font, this.title, this.imageWidth/2 - this.font.width(this.title)/2, this.titleLabelY, 4210752, false);
        pGuiGraphicsExtractor.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        ItemStack stack = getMenu().getItems().getFirst();
        recipeCheck.getRecipeFor(new RecipeWrapper(new InvWrapper(new SimpleContainer(stack))), getMenu().getLevel()).ifPresent(recipe -> {
            switch (recipe.value().tempChange()) {
                case DOWN_1 -> drawThermometer(graphics, 0, 0);
                case DOWN_2 -> drawThermometer(graphics, 8, 0);
                case DOWN_3, DOWN_4, DOWN_5 -> drawThermometer(graphics, 16, 0);
                case UP_1 -> drawThermometer(graphics, 0, 16);
                case UP_2 -> drawThermometer(graphics, 8, 16);
                case UP_3, UP_4, UP_5 -> drawThermometer(graphics, 16, 16);
            }
            if (mouseX > leftPos+84 && mouseX < leftPos+84+8 && mouseY > topPos+22 && mouseY < topPos+22+16) {
                com.accbdd.complicated_bees.port.PortGui.tooltip(graphics,this.font,
                        List.of(
                                Component.translatable("jei.complicated_bees.modifier", recipe.value().tempChange().getTranslationKey()),
                                Component.translatable("jei.complicated_bees.consumption_chance", String.format("%.0f%%", recipe.value().useChance() * 100))
                        ),
                        Optional.empty(),
                        mouseX,
                        mouseY);
            }
        });
        extractTooltip(graphics, mouseX, mouseY);
    }

    private void drawThermometer(GuiGraphicsExtractor graphics, int uOffset, int vOffset) {
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, leftPos+84, topPos+22, 176+uOffset, vOffset, 8, 16);
    }
}
