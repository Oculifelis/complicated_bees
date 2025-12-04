package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractGyrofugePoweredModifierBlockEntity extends AbstractPoweredGyrofugeBlockEntity implements IGyrofugeModifier {
    private final MachineModifier modifier;
    private final int idleUsage;

    public AbstractGyrofugePoweredModifierBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, MachineModifier modifier, int idleUsage) {
        super(pType, pPos, pBlockState);
        this.modifier = modifier;
        this.idleUsage = idleUsage;
    }

    @Override
    public int getIdleUsage() {
        return idleUsage;
    }

    @Override
    public MachineModifier getMachineModifier() {
        return modifier;
    }

    public List<Component> getTooltip() {
        var tooltip = modifier.getTooltipComponents();
        tooltip.add(Component.translatable("upgrade.complicated_bees.rf_label", getIdleUsage()).withStyle(ChatFormatting.GRAY));
        return tooltip;
    }
}
