package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.bees.Flower;
import mezz.jei.api.ingredients.IIngredientType;

public class ComplicatedIngredients {

    public static final IIngredientType<BlockWrapper> BLOCK = new IIngredientType<>() {
        @Override
        public String getUid() {
            return "bee_block";
        }

        @Override
        public Class<? extends BlockWrapper> getIngredientClass() {
            return BlockWrapper.class;
        }
    };

    public static final IIngredientType<Flower> FLOWER = new IIngredientType<>() {
        @Override
        public String getUid() {
            return "bee_flower";
        }

        @Override
        public Class<? extends Flower> getIngredientClass() {
            return Flower.class;
        }
    };
}