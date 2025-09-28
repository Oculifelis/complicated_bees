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

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class BlockRenderer implements IIngredientRenderer<Block> {
    /**
     * Renders an ingredient.
     *
     * @param guiGraphics The current {@link GuiGraphics} for rendering the ingredient.
     * @param ingredient  the ingredient to render.
     * @since 9.3.0
     */
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
                if (null != level) {
                    ResourceLocation rl = level.registryAccess().registry(Registries.BLOCK).orElseThrow().getKey(block);
                    if (null != rl) {
                        TextureAtlasSprite sprite = manager.getBlockModelShaper().getBlockModel(block.defaultBlockState()).getParticleIcon(ModelData.EMPTY);
                        guiGraphics.blit(0, 0, 0, 16, 16, sprite);
                    } else {
                        LOGGER.debug("Error parsing block: {} for JEI plugin.", block.getName().getString());
                    }
                }
            }
        } catch (IllegalArgumentException | ArgumentIndexOutOfBoundsException e) {
            LOGGER.error("Block [{}] handled badly and may not render properly in recipe viewers.",
                    block.getName().getString());
            e.printStackTrace();
        }

    }

    /**
     * Get the tooltip text for this ingredient. JEI renders the tooltip based on this.
     *
     * @param ingredient  The ingredient to get the tooltip for.
     * @param tooltipFlag Whether to show advanced information on item tooltips, toggled by F3+H
     * @return The tooltip text for the ingredient.
     * @deprecated use {@link #getTooltip(ITooltipBuilder, Object, TooltipFlag)}
     */
    @Override
    public List<Component> getTooltip(Block ingredient, TooltipFlag tooltipFlag) {
        return List.of(ingredient.getName());
    }
}
