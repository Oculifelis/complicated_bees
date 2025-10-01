package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgumentIndexOutOfBoundsException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;

@SuppressWarnings("removal")
@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class BlockIngredientRenderer implements IIngredientRenderer<Block> {
    @Override
    public void render(GuiGraphics guiGraphics, Block ingredient) {
        drawBlock(guiGraphics, ingredient);
    }

    private void drawBlock(GuiGraphics guiGraphics, Block block) {
        try {
            ItemStack blockItemStack = new ItemStack(block.asItem());
            if (!blockItemStack.is(Items.AIR)) {
                guiGraphics.renderFakeItem(new ItemStack(block.asItem()), 0, 0);
            } else {
                // Handle blocks without corresponding BlockItems
                ModelManager manager = Minecraft.getInstance().getModelManager();
                Level level = Minecraft.getInstance().level;
                if (level != null) {
                    ResourceLocation loc = level.registryAccess().registry(Registries.BLOCK).orElseThrow().getKey(block);
                    if (loc != null) {
                        TextureAtlasSprite sprite = manager.getBlockModelShaper().getBlockModel(block.defaultBlockState()).getParticleIcon(ModelData.EMPTY);
                        guiGraphics.blit(0, 0, 0, 16, 16, sprite);
                    } else {
                        LOGGER.debug("Error parsing block: {} for JEI plugin.", block.getName().getString());
                    }
                }
            }
        } catch (IllegalArgumentException | ArgumentIndexOutOfBoundsException e) {
            LOGGER.error("Block [{}] was handled badly and may not render properly in recipe viewers.",
                    block.getName().getString());
        }

    }

    @Override
    public List<Component> getTooltip(Block ingredient, TooltipFlag tooltipFlag) {
        return null;//List.of(ingredient.getName());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Block ingredient, TooltipFlag tooltipFlag) {
        tooltip.add(ingredient.getName());
    }
}
