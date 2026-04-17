package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.recipe.MutatorRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumMutatorScreen extends AbstractContainerScreen<MellariumMutatorMenu> {
    private final ResourceLocation GUI;
    private final RecipeManager.CachedCheck<RecipeInput, MutatorRecipe> recipeCheck = RecipeManager.createCheck(EsotericRegistration.MUTATOR_RECIPE.get());

    public MellariumMutatorScreen(MellariumMutatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.GUI = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/mellarium_mutator.png");
        this.imageHeight = 161;
        this.imageWidth = 176;
        this.inventoryLabelY = imageHeight - 93;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
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
        ItemStack stack = getMenu().getItems().getFirst();
        recipeCheck.getRecipeFor(new RecipeWrapper(new InvWrapper(new SimpleContainer(stack))), getMenu().getLevel()).ifPresent(recipe -> {
            graphics.blit(GUI, leftPos+84, topPos+26, 176, 0, 8, 8);
            if (mouseX > leftPos+83 && mouseX < leftPos+83+10 && mouseY > topPos+25 && mouseY < topPos+25+10) {
                graphics.renderTooltip(this.font,
                        Component.translatable("jei.complicated_bees.modifier", recipe.value().mutationModifier() + "x"),
                        mouseX,
                        mouseY);
            }
        });
        renderTooltip(graphics, mouseX, mouseY);
    }
}
