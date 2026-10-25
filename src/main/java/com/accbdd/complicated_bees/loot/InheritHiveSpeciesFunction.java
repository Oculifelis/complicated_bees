package com.accbdd.complicated_bees.loot;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Genome;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.block.entity.BeeNestBlockEntity;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class InheritHiveSpeciesFunction extends LootItemConditionalFunction {

    public static final MapCodec<InheritHiveSpeciesFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).apply(instance, InheritHiveSpeciesFunction::new)
    );

    public InheritHiveSpeciesFunction(List<LootItemCondition> pPredicates) {
        super(pPredicates);
    }

    @Override
    public com.mojang.serialization.MapCodec<InheritHiveSpeciesFunction> codec() { return CODEC; }

    @Override
    protected ItemStack run(ItemStack pStack, LootContext pContext) {
        BlockEntity blockEntity = pContext.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BeeNestBlockEntity) {
            Species species = ((BeeNestBlockEntity) blockEntity).getSpecies();
            return GeneticHelper.setGenome(pStack, new Genome(species.getDefaultChromosome()));
        }
        return pStack;
    }

    public static LootItemConditionalFunction.Builder<?> set() {
        return simpleBuilder(InheritHiveSpeciesFunction::new);
    }
}
