package com.accbdd.complicated_bees.compat.emi;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import com.google.common.collect.Lists;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class EmiFlowerBlocks extends EmiStack {
    private final List<EmiStack> blocks;
    private final Flower flower;
    private float loopCount;

    public EmiFlowerBlocks(Flower flower) {
        this.blocks = flower.getAllFlowerBlocks().stream().map(block -> new EmiBlock(block).copy()).toList();
        this.flower = flower;
    }

    @Override
    public EmiStack copy() {
        return new EmiFlowerBlocks(flower);
    }

    @Override
    public List<EmiStack> getEmiStacks() {
        return blocks;
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        render(draw, x, y, delta, 0);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        blocks.get((int)loopCount).render(draw, x, y, delta, flags);
        loopCount += delta / 20;
        loopCount %= blocks.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public CompoundTag getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return flower;
    }

    @Override
    public ResourceLocation getId() {
        return GeneticHelper.getRegistryAccess().registryOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY).getKey(flower);
    }

    @Override
    public List<Component> getTooltipText() {
        return List.of(GeneticHelper.getTranslationKey(flower));
    }

    @Override
    public Component getName() {
        return blocks.get((int)loopCount).getName();
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = Lists.newArrayList();
        list.add(ClientTooltipComponent.create(getName().getVisualOrderText()));
        return list;
    }
}
