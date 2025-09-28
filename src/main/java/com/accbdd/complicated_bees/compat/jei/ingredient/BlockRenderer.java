package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Services;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelDataManager;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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
//                BlockModel
//                new BlockRenderDispatcher()
//                ModelBlockRenderer renderer = new ModelBlockRenderer(BlockColors.createDefault());
//                renderer.renderModel(Pose.CROAKING, );
//                new BlockRenderDispatcher().getBlockModel(block.defaultBlockState()).getQuads().get(0).getSprite();
                ModelManager manager = Minecraft.getInstance().getModelManager();
                Level level = Minecraft.getInstance().level;
                if (null != level) {
                    ResourceLocation rl = level.registryAccess().registry(Registries.BLOCK).orElseThrow().getKey(block);
                    if (null != rl) {
                        TextureAtlasSprite sprite = manager.getModel(rl).getQuads(block.defaultBlockState(), Direction.NORTH, level.random).get(0).getSprite();
                        guiGraphics.blit(0, 0, 0, 16, 16, sprite);
                    } else {
                        LOGGER.debug("Error parsing block: {} for JEI plugin.", block.getName().getString());
                    }
                }
//                BlockRendererDispatcher.getModelForState(block.defaultBlockState()).getQuads.get.getSprite()
//                BlockModelProvider.
//                 block.defaultBlockState()
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Block [{}] handled badly and may not render properly in recipe viewers.",
                    block.getName().getString());
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
