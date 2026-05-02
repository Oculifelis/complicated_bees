package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;
import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class BlockIngredientHelper implements IIngredientHelper<Block> {

    @Override
    public IIngredientType<Block> getIngredientType() {
        return ComplicatedIngredients.BLOCK;
    }

    @Override
    public String getDisplayName(Block ingredient) {
        return "Block: " + ingredient.getName().getString();
    }

    @Override
    public String getUniqueId(Block ingredient, UidContext context) {
        return getUid(ingredient, context);
    }

    @Override
    public String getUid(Block ingredient, UidContext context) {

        return "Block:" + getRegistryNameForBlock(ingredient);
    }

    private String getRegistryNameForBlock(Block ingredient) {
        ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(ingredient);
        if (null == rl) {
            throw new NullPointerException("Blocks must be registered before being used as ingredients.");
        }
        return rl.toString();
    }

    @Override
    public ResourceLocation getResourceLocation(Block ingredient) {
        return ResourceLocation.parse(getRegistryNameForBlock(ingredient));
    }

    @Override
    public Block copyIngredient(Block ingredient) {
        // Blocks are singletons, so the only way to copy this would be to create
        // an unregistered copy, and creating unregistered blocks is generally a bad idea.
        // Let's hope there are no mutations.
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable Block block) {
        if (block == null) {
            return "Block ingredient is null";
        }

        return MODID + " registered an ingredient for the block " + getDisplayName(block) + ", registered at " + getRegistryNameForBlock(block) + " that has caused an error.";
    }

    @Override
    public ItemStack getCheatItemStack(Block block) {
        // This try catch probably isn't necessary, and both paths should spit out an air stack (effectively nothing)
        // if the BlockItem doesn't exist.
        try {
            return new ItemStack(block.asItem());
        } catch (IllegalArgumentException e) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean isHiddenFromRecipeViewersByTags(Block ingredient) {
        // While showing blocktags is a useful utility, it's kind of weird to have every block show up twice
        // in JEI. Better to hide these from the search panel. Can be turned into a config option if desired.
        return true;
    }

    /**
     * @return a list of all blocks that are in flower datapack entries
     */
    public static List<Block> createList() {

        Set<Block> blocks = new HashSet<>();
        GeneticHelper.getRegistryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).orElseThrow()
                .forEach(flower -> blocks.addAll(flower.getAllFlowerBlocks()));

        LOGGER.debug("Added {} flower blocks to the JEI ingredient list", blocks.size());

        return blocks.stream().toList();
    }
}
