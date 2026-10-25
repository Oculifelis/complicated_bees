package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.CombRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class CombItem extends Item {

    public CombItem(Properties pProperties) {
        super(pProperties);
    }

    public static Comb getComb(ItemStack stack) {
        return GeneticHelper.getRegistryAccess().lookupOrThrow(CombRegistration.COMB_REGISTRY_KEY).getValue(stack.get(EsotericRegistration.COMB_TYPE));
    }

    public static ItemStack setComb(ItemStack stack, Identifier comb) {
        stack.set(EsotericRegistration.COMB_TYPE.get(), comb);
        return stack;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable("comb.complicated_bees." +
                        (Optional.ofNullable(stack.get(EsotericRegistration.COMB_TYPE.get())).map(Identifier::toString).orElse("invalid")))
                .append(" ")
                .append(Component.translatable(getDescriptionId()));
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        Identifier combLocation = stack.get(EsotericRegistration.COMB_TYPE.get());
        Registry<Comb> registry = GeneticHelper.getRegistryAccess().lookupOrThrow(CombRegistration.COMB_REGISTRY_KEY);
        if (combLocation != null) {
            switch (tintIndex) {
                case 0:
                    return registry.containsKey(combLocation) ? Objects.requireNonNull(registry.getValue(combLocation)).getOuterColor() : 0xe7d46a;
                case 1:
                    return registry.containsKey(combLocation) ? Objects.requireNonNull(registry.getValue(combLocation)).getInnerColor() : 0xfea02b;
            }
        }
        return 0xFFFFFF;
    }
}
