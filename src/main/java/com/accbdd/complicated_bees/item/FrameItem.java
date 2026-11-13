package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.bees.IBeeHousing;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class FrameItem extends DisableableItem {
    private final BeeHousingModifier modifier;

    public FrameItem(Properties pProperties, BeeHousingModifier modifier, ModConfigSpec.ConfigValue<Boolean> configValue) {
        super(pProperties, configValue);
        this.modifier = modifier;
    }

    public BeeHousingModifier getModifier() {
        return modifier;
    }

    /**
     * Called on the server once per bee cycle for every frame in a working housing, before frames take damage.
     * Addons override this for frames that do something each cycle.
     * @param housing the housing the frame sits in
     * @param frame the frame stack itself
     * @param queen the queen being worked
     */
    public void onBeeCycle(IBeeHousing housing, ItemStack frame, ItemStack queen) {
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (context.level() != null) {
            modifier.getTooltipComponent().forEach(tooltipComponents);
        }
    }
}
