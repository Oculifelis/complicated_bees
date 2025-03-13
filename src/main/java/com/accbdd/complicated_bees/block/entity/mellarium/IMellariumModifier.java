package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;

/**
 * Interface for mellarium blocks that provide a BeeHousingModifier
 */
public interface IMellariumModifier {
    BeeHousingModifier getModifier();
}
