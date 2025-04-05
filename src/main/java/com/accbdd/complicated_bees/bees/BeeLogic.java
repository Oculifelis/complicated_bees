package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.bees.gene.*;
import com.accbdd.complicated_bees.bees.gene.enums.EnumHumidity;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTemperature;
import com.accbdd.complicated_bees.item.QueenItem;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeLogic {
    private Level level;
    private BlockPos pos;
    private final IBeeHousing housing;
    private boolean queenSatisfied;
    private boolean queenEcstatic;
    private EnumTemperature temperatureCache = null;
    private EnumHumidity humidityCache = null;
    private final List<BlockPos> flowerCache = new ArrayList<>();
    private ItemStack queen;

    public BeeLogic(Level level, BlockPos pos, IBeeHousing housing) {
        this.level = level;
        this.pos = pos;
        this.housing = housing;
    }

    public void clearConditionCache() {
        temperatureCache = null;
        humidityCache = null;
    }

    public ItemStack getQueen() {
        return queen;
    }

    public void setQueen(ItemStack queen) {
        this.queen = queen;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    private void checkQueenSatisfied() {
        clearConditionCache();
        if (!(getQueen().getItem() instanceof QueenItem)) {
            queenSatisfied = false;
            return;
        }

        if (level == null) {
            queenSatisfied = false;
            return;
        }

        Chromosome chromosome = GeneticHelper.getChromosome(getQueen(), true);

        if (!((GeneTemperature) chromosome.getGene(GeneTemperature.ID)).withinTolerance(getTemperature())) {
            addError(EnumErrorCodes.WRONG_TEMP);
            queenSatisfied = false;
            return;
        } else {
            removeError(EnumErrorCodes.WRONG_TEMP);
        }
        if (!((GeneHumidity) chromosome.getGene(GeneHumidity.ID)).withinTolerance(getHumidity())) {
            addError(EnumErrorCodes.WRONG_HUMIDITY);
            queenSatisfied = false;
            return;
        } else {
            removeError(EnumErrorCodes.WRONG_HUMIDITY);
        }
        if (this.flowerCache.isEmpty()) {
            addError(EnumErrorCodes.NO_FLOWER);
            queenSatisfied = false;
            return;
        } else {
            removeError(EnumErrorCodes.NO_FLOWER);
        }
        if (!checkRainOverride() && !(boolean) chromosome.getGene(new ResourceLocation(MODID, "weatherproof")).get()) {
            addError(EnumErrorCodes.WEATHER);
            queenSatisfied = false;
            return;
        } else {
            removeError(EnumErrorCodes.WEATHER);
        }
        if (!checkSky()
                && !(boolean) chromosome.getGene(new ResourceLocation(MODID, "cave_dwelling")).get()) {
            addError(EnumErrorCodes.UNDERGROUND);
            queenSatisfied = false;
            return;
        } else {
            removeError(EnumErrorCodes.UNDERGROUND);
        }
        if (!((GeneActiveTime) chromosome.getGene(new ResourceLocation(MODID, "active_time"))).isSatisfied(level)) {
            addError(EnumErrorCodes.WRONG_TIME);
            queenSatisfied = false;
            return;
        } else {
            removeError(EnumErrorCodes.WRONG_TIME);
        }

        queenSatisfied = true;
    }

    private void checkQueenEcstatic() {
        Chromosome chromosome = GeneticHelper.getChromosome(getQueen(), true);
        if (((GeneTemperature) chromosome.getGene(GeneTemperature.ID)).get().equals(getTemperature())
                && ((GeneHumidity) chromosome.getGene(GeneHumidity.ID)).get().equals(getHumidity())
                && queenSatisfied) {
            addError(EnumErrorCodes.ECSTATIC);
            this.queenEcstatic = true;
        } else {
            removeError(EnumErrorCodes.ECSTATIC);
            this.queenEcstatic = false;
        }
    }

    public void checkConditions() {
        checkFlowerCache();
        checkQueenSatisfied();
        checkQueenEcstatic();
    }


    /**
     * @return true if it is NOT raining anywhere inside the housing's territory
     */
    private boolean checkRainOverride() {
        if (getLevel().isRaining())
            return true;
        boolean clear = !(getLevel().isRainingAt(getPos().above()));
        for (BlockPosBoxIterator it = getTerritoryIterator(); it.hasNext(); ) {
            BlockPos checkPos = it.next();
            if (getLevel().isLoaded(checkPos) && getLevel().isRainingAt(checkPos.above())) {
                clear = false;
                break;
            }
        }

        if (clear) {
            return true;
        } else {
            for (BeeHousingModifier mod : housing.getHousingModifiers()) {
                if (mod.getRainOverride())
                    return true;
            }
        }
        return false;
    }

    private boolean checkSky() {
        boolean sky = getLevel().canSeeSky(pos.above());
        if (sky) {
            return true;
        } else {
            for (BeeHousingModifier mod : housing.getHousingModifiers()) {
                if (mod.getSkyOverride())
                    return true;
            }
        }
        return false;
    }

    public boolean isQueenSatisfied() {
        return queenSatisfied;
    }

    public boolean isQueenEcstatic() {
        return queenEcstatic;
    }

    public EnumHumidity getHumidity() {
        if (this.humidityCache == null) {
            if (getLevel() == null) {
                return null;
            }
            this.humidityCache = EnumHumidity.getFromPosition(getLevel(), getPos());

            for (BeeHousingModifier mod : housing.getHousingModifiers()) {
                int ordinal = humidityCache.ordinal() + mod.getHumidityMod().up - mod.getHumidityMod().down;
                humidityCache = EnumHumidity.values()[Math.max(0, Math.min(EnumHumidity.values().length - 1, ordinal))];
            }
        }

        return this.humidityCache;
    }

    public EnumTemperature getTemperature() {
        if (this.temperatureCache == null) {
            if (getLevel() == null) {
                return null;
            }
            this.temperatureCache = EnumTemperature.getFromPosition(getLevel(), getPos());

            for (BeeHousingModifier mod : housing.getHousingModifiers()) {
                int ordinal = temperatureCache.ordinal() + mod.getTemperatureMod().up - mod.getTemperatureMod().down;
                temperatureCache = EnumTemperature.values()[Math.max(0, Math.min(EnumTemperature.values().length - 1, ordinal))];
            }
        }

        return this.temperatureCache;
    }

    private void checkFlowerCache() {
        Flower flower = ServerLifecycleHooks.getCurrentServer().registryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).get()
                .get(((GeneFlower) GeneticHelper.getGene(getQueen(), GeneFlower.ID, true)).get());
        Level level = getLevel();
        if (flower == null || level == null) {
            //no valid flower gene or the level isn't loaded
            flowerCache.clear();
            flowerCache.add(getPos());
            return;
        }
        for (int i = 0; i < flowerCache.size(); i++) {
            if (flower.isAcceptable(level.getBlockState(flowerCache.get(i)))) {
                return;
            } else {
                flowerCache.remove(i);
                i--;
            }
        }
        //if we get here, there are no valid flowers in the flowerCache, rebuild
        rebuildFlowerCache();
    }

    public void rebuildFlowerCache() {
        clearFlowerCache();
        Flower flower = ServerLifecycleHooks.getCurrentServer().registryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).get()
                .get(((GeneFlower) GeneticHelper.getGene(getQueen(), GeneFlower.ID, true)).get());

        if (flower == null) {
            //no valid flower gene
            flowerCache.add(getPos());
            return;
        }
        BlockPosBoxIterator it = getTerritoryIterator();
        while (it.hasNext() && getQueen().is(ItemsRegistration.QUEEN.get())) {
            BlockPos pos = it.next();
            if (flower.isAcceptable(getLevel().getBlockState(pos))) {
                flowerCache.add(pos);
            }
        }
    }

    public void clearFlowerCache() {
        flowerCache.clear();
    }

    BlockPosBoxIterator getTerritoryIterator() {
        float rangeModifier = 1f;
        for (BeeHousingModifier modifier : housing.getHousingModifiers()) {
            rangeModifier *= modifier.getTerritoryMod();
        }
        int[] searchRadii = (int[]) GeneticHelper.getGeneValue(getQueen(), GeneTerritory.ID, true);
        return new BlockPosBoxIterator(getPos(), Math.round(searchRadii[0] * rangeModifier), Math.round(searchRadii[1] * rangeModifier));
    }

    private void addError(EnumErrorCodes... error) {
        housing.addError(error);
    }

    private void removeError(EnumErrorCodes... error) {
        housing.removeError(error);
    }
}
