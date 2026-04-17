package com.accbdd.complicated_bees.compat.emi.ingredient;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class EmiFlower extends EmiStack {
    private final List<EmiStack> stacks;
    private final Flower flower;

    public EmiFlower(Flower flower) {
        this.flower = flower;
        this.stacks = new ArrayList<>();
        stacks.addAll(flower.getAllFlowerBlocks().stream().map(EmiBlock::new).toList());
    }

    @Override
    public EmiStack copy() {
        return new EmiFlower(flower);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        if (!stacks.isEmpty()) {
            stacks.getFirst().render(draw, x, y, delta, flags);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            draw.blit(ResourceLocation.fromNamespaceAndPath(MODID, "textures/item/flower_overlay.png"), x, y, 0, 0, 16, 16, 16, 16);
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    @Override
    public DataComponentPatch getComponentChanges() {
        return null;
    }

    @Override
    public Object getKey() {
        return flower;
    }

    @Override
    public ResourceLocation getId() {
        return GeneticHelper.getRegistryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).get().getKey(flower);
    }

    @Override
    public List<Component> getTooltipText() {
        return List.of(GeneticHelper.getTranslationKey(flower));
    }

    @Override
    public Component getName() {
        return GeneticHelper.getTranslationKey(flower);
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        ArrayList<ClientTooltipComponent> list = new ArrayList<>();
        list.add(new ClientTextTooltip(getName().getVisualOrderText()));
        list.add(new ClientTextTooltip(Component.translatable("gui.complicated_bees.jei.bee_flower").withStyle(ChatFormatting.GOLD).getVisualOrderText()));
        EmiTooltipComponents.appendModName(list, getId().getNamespace());
        return list;
    }
}
