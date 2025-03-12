package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.ApiaryBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.ItemSlot;
import com.accbdd.complicated_bees.screen.slot.OutputSlot;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

import static com.accbdd.complicated_bees.block.entity.ApiaryBlockEntity.*;

public class ApiaryMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private final ContainerData data;
    private static final int INV_X = 8;
    private static final int INV_Y = 105;

    public ApiaryMenu(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(3));
    }

    public ApiaryMenu(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(MenuRegistration.APIARY_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.data = data;
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof ApiaryBlockEntity apiary) {
            addSlot(new TagSlot(apiary.getBeeItems(), BEE_SLOT, 29, 38, ItemTagGenerator.ROYAL));
            addSlot(new ItemSlot(apiary.getBeeItems(), BEE_SLOT + 1, 29, 63, ItemsRegistration.DRONE.get()));

            addSlot(new TagSlot(apiary.getFrameItems(), FRAME_SLOT, 65, 23, ItemTagGenerator.FRAME));
            addSlot(new TagSlot(apiary.getFrameItems(), FRAME_SLOT + 1, 65, 51, ItemTagGenerator.FRAME));
            addSlot(new TagSlot(apiary.getFrameItems(), FRAME_SLOT + 2, 65, 79, ItemTagGenerator.FRAME));

            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT, 115, 51));
            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT + 1, 115, 26));
            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT + 2, 137, 39));
            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT + 3, 137, 64));
            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT + 4, 115, 76));
            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT + 5, 93, 64));
            addSlot(new OutputSlot(apiary.getOutputItems(), OUTPUT_SLOT + 6, 93, 39));
        }
        layoutPlayerInventorySlots(player.getInventory());

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, BlocksRegistration.APIARY.get());
    }

    public boolean hasQueen() {
        ItemStack item = getSlot(0).getItem();
        return item.getItem() == ItemsRegistration.QUEEN.get();
    }

    public int getScaledProgress(float progress, int maxProgress) {
        int barHeight = 45;
        return maxProgress != 0 && progress != 0 ? (int) (progress * barHeight / maxProgress) : 0;
    }

    public ItemStack getQueen() {
        return getSlot(0).getItem();
    }

    public boolean isBreeding() {
        return data.get(0) > 0;
    }

    public ContainerData getData() {
        return this.data;
    }

    public boolean hasQueue() {
        return data.get(2) == EnumErrorCodes.OUTPUT_FULL.value;
    }
}
