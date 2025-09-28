package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.constants.Tags;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class BlockHelper implements IIngredientHelper<Block> {

    /**
     * @return The ingredient type for this {@link IIngredientHelper}.
     */
    @Override
    public IIngredientType<Block> getIngredientType() {
        return ComplicatedIngredients.BLOCK;
    }

    /**
     * Display name used for searching. Normally this is the first line of the tooltip.
     *
     * @param ingredient
     */
    @Override
    public String getDisplayName(Block ingredient) {
        return "Block: " + ingredient.getName().getString();
    }

    /**
     * Unique ID for use in comparing, blacklisting, and looking up ingredients.
     *
     * @param ingredient
     * @param context
     * @since 7.3.0
     */
    @Override
    public String getUniqueId(Block ingredient, UidContext context) {

        return "Block: " + getRegistryNameForBlock(ingredient);
    }

    private String getRegistryNameForBlock(Block ingredient) {
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(ingredient);
        if (null == rl) {
            throw new NullPointerException("Blocks must be registered before being used as ingredients.");
        }
        return rl.toString();
    }

    /**
     * Return the registry name of the given ingredient.
     *
     * @param ingredient
     * @since 9.2.2
     */
    @Override
    public ResourceLocation getResourceLocation(Block ingredient) {
        return new ResourceLocation(getRegistryNameForBlock(ingredient));
    }

    /**
     * Makes a copy of the given ingredient.
     * Used by JEI to protect against mutation of ingredients.
     *
     * @param ingredient the ingredient to copy
     * @return a copy of the ingredient
     */
    @Override
    public Block copyIngredient(Block ingredient) {
        // Blocks are singletons, so the only way to copy this would be to create
        // an unregistered copy, and creating unregistered blocks is generally a bad idea.
        // Let's hope there are no mutations.
        return ingredient;
    }



    /**
     * Get information for error messages involving this ingredient.
     * Be extremely careful not to crash here, get as much useful info as possible.
     *
     * @param block
     */
    @Override
    public String getErrorInfo(@Nullable Block block) {
        if (null == block) {

            return "Block ingredient is null";
        }

        return MODID + " registered an ingredient for the block " + getDisplayName(block) + ", registered at " + getRegistryNameForBlock(block) + " that has caused an error.";
    }

    /**
     * Called when a player is in cheat mode and clicks an ingredient in the list.
     *
     * @param block The ingredient to cheat in. Do not edit this ingredient.
     * @return an ItemStack for JEI to give the player, or an empty stack if there is nothing that can be given.
     */
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

    /**
     * Return true if the given ingredient is hidden from recipe viewers by its tags.
     *
     * @param ingredient
     * @see Tags#HIDDEN_FROM_RECIPE_VIEWERS
     * @since 15.6.0
     */
    @Override
    public boolean isHiddenFromRecipeViewersByTags(Block ingredient) {
        // While showing blocktags is a useful utility, it's kind of wierd to have every block show up twice
        // in JEI. Better to hide these from the search panel. Can be turned into a config option if desired.
        return true;
    }
}
