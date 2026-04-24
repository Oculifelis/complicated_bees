package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.bees.Chromosome;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.*;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.GeneRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeItem extends Item {
    public static final String AGE_TAG = "bee_age";
    public static final String ANALYZED_TAG = "analyzed";

    public BeeItem(Properties prop) {
        super(prop);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return GeneticHelper.getSpecies(pStack, true).isFoil();
    }

    public static float getAge(ItemStack stack) {
        return stack.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).age();
    }

    public static void setAge(ItemStack stack, float age) {
        stack.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> bee.withAge(age));
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        Species primary = GeneticHelper.getSpecies(stack, true);
        MutableComponent component = Component.empty();
        component.append(GeneticHelper.getTranslationKey(primary));

        if (primary == null) {
            return component;
        }
        component.append(" ").append(Component.translatable(getDescriptionId()));

        if (!(BreedingTracker.CLIENT_INSTANCE == null) && !BreedingTracker.CLIENT_INSTANCE.isDiscovered(GeneticHelper.getSpecies(stack, true)))
            component.withStyle(ChatFormatting.ITALIC);

        return component;
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 1) {
            Species species = GeneticHelper.getSpecies(stack, true);
            if (species != null) {
                return species.getColor();
            }
            return 0xFFFFFF;
        }
        return 0xFFFFFF;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        GeneSpecies geneSpecies = (GeneSpecies) GeneticHelper.getGene(stack, ResourceLocation.fromNamespaceAndPath(MODID, GeneSpecies.TAG), true);
        if (geneSpecies == null) {
            //broken nbt
            tooltipComponents.add(Component.literal("INVALID ITEM"));
        } else if (geneSpecies.get() == null) {
            //species doesn't exist in registry
            tooltipComponents.add(Component.literal("INVALID SPECIES"));
        } else if (!stack.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).analyzed()) {
            tooltipComponents.add(Component.translatable("gui.complicated_bees.not_analyzed").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        } else if (!Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("gui.complicated_bees.more_info").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        } else if (Minecraft.getInstance().level != null) {
            Chromosome primary = GeneticHelper.getChromosome(stack, true);
            MutableComponent hybridName = GeneticHelper.getSpeciesHybridName(stack);
            if (hybridName != null)
                tooltipComponents.add(hybridName
                        .withStyle(ChatFormatting.BLUE));
            tooltipComponents.add(Component.translatable("gene.complicated_bees.lifespan_label", primary.getGene(GeneLifespan.ID).getTranslationKey())
                    .withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("gene.complicated_bees.productivity_label.short", primary.getGene(GeneProductivity.ID).getTranslationKey())
                    .withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("gene.complicated_bees.temperature_label.short", primary.getGene(GeneTemperature.ID).getTranslationKey(), ((GeneTolerant<?>) primary.getGene(GeneTemperature.ID)).getTolerance().getTranslationKey())
                    .withStyle(ChatFormatting.GREEN));
            tooltipComponents.add(Component.translatable("gene.complicated_bees.humidity_label.short", primary.getGene(GeneHumidity.ID).getTranslationKey(), ((GeneTolerant<?>) primary.getGene(GeneHumidity.ID)).getTolerance().getTranslationKey())
                    .withStyle(ChatFormatting.GREEN));
            tooltipComponents.add(primary.getGene(GeneFlower.ID).getTranslationKey()
                    .withStyle(ChatFormatting.GRAY));
            if (!Screen.hasControlDown()) {
                tooltipComponents.add(Component.translatable("gui.complicated_bees.even_more_info").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.complicated_bees.slash_separated",
                        Component.translatable("gene.complicated_bees.cave_dwelling_label.short", primary.getGene(GeneRegistration.CAVE_DWELLING.getId()).getTranslationKey()),
                        Component.translatable("gene.complicated_bees.weatherproof_label.short", primary.getGene(GeneRegistration.WEATHERPROOF.getId()).getTranslationKey()))
                        .withStyle(ChatFormatting.GOLD));
                tooltipComponents.add(Component.translatable("tooltip.complicated_bees.slash_separated",
                                Component.translatable("gene.complicated_bees.active_time_label.short", primary.getGene(GeneActiveTime.ID).getTranslationKey()),
                                Component.translatable("gene.complicated_bees.fertility_label.short", primary.getGene(GeneFertility.ID).getTranslationKey()))
                        .withStyle(ChatFormatting.GOLD));
                tooltipComponents.add(Component.translatable("tooltip.complicated_bees.slash_separated",
                                Component.translatable("gene.complicated_bees.effect_label.short", primary.getGene(GeneEffect.ID).getTranslationKey()),
                                primary.getGene(GeneTerritory.ID).getTranslationKey())
                        .withStyle(ChatFormatting.GOLD));
            }
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!pLevel.isClientSide() && pEntity instanceof Player player)
            BreedingTracker.getTracker(player).discoverIndividual(pStack);
    }
}
