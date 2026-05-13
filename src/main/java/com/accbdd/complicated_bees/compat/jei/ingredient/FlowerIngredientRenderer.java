package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class FlowerIngredientRenderer implements IIngredientRenderer<Flower> {
    @Override
    public void render(GuiGraphics guiGraphics, Flower ingredient) {
        BlockIngredientRenderer.INSTANCE.render(guiGraphics, ingredient.getAllFlowerBlocks().stream().map(BlockWrapper::new).findAny().get());
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(MODID, "textures/item/flower_overlay.png"), 0, 0, 0, 0, 16, 16, 16, 16);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    @Override
    public List<Component> getTooltip(Flower ingredient, TooltipFlag tooltipFlag) {
        return List.of();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Flower ingredient, TooltipFlag tooltipFlag) {
        tooltip.add(GeneticHelper.getTranslationKey(ingredient));
        tooltip.add(Component.translatable("gui.complicated_bees.jei.bee_flower").withStyle(ChatFormatting.GOLD));
    }
}
