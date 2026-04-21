package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgumentIndexOutOfBoundsException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class BlockIngredientRenderer implements IIngredientRenderer<Block> {
    public static BlockIngredientRenderer INSTANCE = new BlockIngredientRenderer();

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
                ResourceLocation loc = BuiltInRegistries.BLOCK.getKeyOrNull(block);
                if (loc != null) {
                    TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(block.defaultBlockState()).getParticleIcon(ModelData.EMPTY);
                    guiGraphics.blit(0, 0, 0, 16, 16, sprite);
                } else {
                    LOGGER.debug("Error parsing block: {} for JEI plugin.", block.getName().getString());
                }
            }
        } catch (IllegalArgumentException | ArgumentIndexOutOfBoundsException e) {
            LOGGER.error("Block [{}] was handled badly and may not render properly in recipe viewers.",
                    block.getName().getString());
        }

    }

    @Override
    public List<Component> getTooltip(Block ingredient, TooltipFlag tooltipFlag) {
        return List.of();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Block ingredient, TooltipFlag tooltipFlag) {
        tooltip.add(ingredient.getName());
    }
}
