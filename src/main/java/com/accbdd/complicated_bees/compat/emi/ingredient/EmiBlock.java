package com.accbdd.complicated_bees.compat.emi.ingredient;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgumentIndexOutOfBoundsException;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;

public class EmiBlock extends EmiStack {
    private final Block block;
    private final ItemStack stack;

    public EmiBlock(Block block) {
        this.block = block;
        this.stack = block.asItem().getDefaultInstance();
    }

    @Override
    public List<EmiStack> getEmiStacks() {
        return stack.isEmpty() ? List.of(this) : List.of(EmiStack.of(stack));
    }

    @Override
    public EmiStack copy() {
        return new EmiBlock(block);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public DataComponentPatch getComponentChanges() {
        return null;
    }

    @Override
    public Object getKey() {
        return block;
    }

    @Override
    public ResourceLocation getId() {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    @Override
    public List<Component> getTooltipText() {
        return List.of(block.getName());
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        return List.of(new ClientTextTooltip(block.getName().getVisualOrderText()));
    }

    @Override
    public Component getName() {
        return block.getName();
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        render(draw, x, y, delta, 0);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        try {
            if (block.asItem() != Items.AIR) {
                draw.renderFakeItem(stack, x, y);
            } else {
                ModelManager manager = Minecraft.getInstance().getModelManager();
                Level level = Minecraft.getInstance().level;
                if (level != null) {
                    ResourceLocation loc = level.registryAccess().registry(Registries.BLOCK).orElseThrow().getKey(block);
                    if (loc != null) {
                        TextureAtlasSprite sprite = manager.getBlockModelShaper().getBlockModel(block.defaultBlockState()).getParticleIcon(ModelData.EMPTY);
                        draw.blit(x, y, 0, 16, 16, sprite);
                    } else {
                        LOGGER.debug("Error parsing block: {} for EMI plugin.", block.getName().getString());
                    }
                }
            }
        } catch (IllegalArgumentException | ArgumentIndexOutOfBoundsException e) {
            LOGGER.error("Block [{}] was handled badly and may not render properly in recipe viewers.",
                    block.getName().getString());
        }
    }
}
