package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MellariumAirConBlockEntity extends MellariumAbstractBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final BeeHousingModifier COLD_1 = new BeeHousingModifier.Builder().temperature(EnumTolerance.DOWN_1).build();
    private static final BeeHousingModifier COLD_2 = new BeeHousingModifier.Builder().temperature(EnumTolerance.DOWN_2).build();
    private static final BeeHousingModifier COLD_3 = new BeeHousingModifier.Builder().temperature(EnumTolerance.DOWN_3).build();
    private static final BeeHousingModifier HOT_1 = new BeeHousingModifier.Builder().temperature(EnumTolerance.UP_1).build();
    private static final BeeHousingModifier HOT_2 = new BeeHousingModifier.Builder().temperature(EnumTolerance.UP_2).build();
    private static final BeeHousingModifier HOT_3 = new BeeHousingModifier.Builder().temperature(EnumTolerance.UP_3).build();
    private static final String ITEMS_TAG = "Items";
    private final ItemStackHandler items;
    private final LazyOptional<IItemHandler> itemHandler;

    public MellariumAirConBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_AIR_CON_BLOCK_ENTITY.get(), pPos, pBlockState);
        items = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.is(ItemTagGenerator.AIR_CON_FUEL);
            }
        };
        itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put(ITEMS_TAG, items.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(ITEMS_TAG))
            items.deserializeNBT(pTag.getCompound(ITEMS_TAG));
    }

    @Override
    public BeeHousingModifier getModifier() {
        ItemStack stack = items.getStackInSlot(0);
        if (stack.is(ItemTagGenerator.AIR_CON_COOLING_1))
            return COLD_1;
        else if (stack.is(ItemTagGenerator.AIR_CON_COOLING_2))
            return COLD_2;
        else if (stack.is(ItemTagGenerator.AIR_CON_COOLING_3))
            return COLD_3;
        else if (stack.is(ItemTagGenerator.AIR_CON_HEATING_1))
            return HOT_1;
        else if (stack.is(ItemTagGenerator.AIR_CON_HEATING_2))
            return HOT_2;
        else if (stack.is(ItemTagGenerator.AIR_CON_HEATING_3))
            return HOT_3;
        else
            return new BeeHousingModifier();
    }

    @Override
    public void beeTick() {
        ItemStack stack = items.getStackInSlot(0);
        if (stack.is(ItemTagGenerator.AIR_CON_FUEL)) {
            if (level.getRandom().nextFloat() < 0.1f) {
                if (stack.hasCraftingRemainingItem()) {
                    items.setStackInSlot(0, stack.getCraftingRemainingItem());
                } else {
                    stack.shrink(1);
                }
            }
        }
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }
}
