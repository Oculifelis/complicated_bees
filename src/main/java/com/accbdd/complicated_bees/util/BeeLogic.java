package com.accbdd.complicated_bees.util;

import com.accbdd.complicated_bees.genetics.gene.enums.EnumHumidity;
import com.accbdd.complicated_bees.genetics.gene.enums.EnumTemperature;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BeeLogic {
    private EnumTemperature temperatureCache = null;
    private EnumHumidity humidityCache = null;
    private final List<BlockPos> flowerCache = new ArrayList<>();
}
