package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgumentIndexOutOfBoundsException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;

@ParametersAreNonnullByDefault
public class BlockIngredientRenderer implements IIngredientRenderer<BlockWrapper> {
    public static BlockIngredientRenderer INSTANCE = new BlockIngredientRenderer();

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, BlockWrapper ingredient) {
        drawBlock(guiGraphics, ingredient);
    }

    private void drawBlock(GuiGraphicsExtractor guiGraphics, BlockWrapper block) {
        try {
            ItemStack blockItemStack = new ItemStack(block.block().asItem());
            if (!blockItemStack.is(Items.AIR)) {
                guiGraphics.fakeItem(new ItemStack(block.block().asItem()), 0, 0);
            } else {
                // Handle blocks without corresponding BlockItems
                Identifier loc = BuiltInRegistries.BLOCK.getKeyOrNull(block.block());
                if (loc != null) {
                    TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(block.block().defaultBlockState()).sprite();
                    guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,sprite,0,0,16,16);

                } else {
                    LOGGER.debug("Error parsing block: {} for JEI plugin.", block.block().getName().getString());
                }
            }
        } catch (IllegalArgumentException | ArgumentIndexOutOfBoundsException e) {
            LOGGER.error("Block [{}] was handled badly and may not render properly in recipe viewers.",
                    block.block().getName().getString());
        }

    }

    @Override
    public List<Component> getTooltip(BlockWrapper ingredient, TooltipFlag tooltipFlag) {
        return List.of();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, BlockWrapper ingredient, TooltipFlag tooltipFlag) {
        tooltip.add(ingredient.block().getName());
    }
}
