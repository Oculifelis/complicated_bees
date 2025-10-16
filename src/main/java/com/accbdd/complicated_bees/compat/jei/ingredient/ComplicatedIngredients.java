package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.level.block.Block;

public class ComplicatedIngredients {

    public static final IIngredientType<Block> BLOCK = new IIngredientType<>() {
        @Override
        public String getUid() {
            return "block";
        }

        @Override
        public Class<? extends Block> getIngredientClass() {
            return Block.class;
        }
    };
}