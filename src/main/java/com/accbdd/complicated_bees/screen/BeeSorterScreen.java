package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.network.packet.UpdateSorterServerbound;
import com.accbdd.complicated_bees.screen.slot.FakeSpeciesSlot;
import com.accbdd.complicated_bees.screen.widget.BeeTypeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeSorterScreen extends AbstractContainerScreen<BeeSorterMenu> {
    private static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/bee_sorter.png");
    private byte[] beeTypes;
    public BeeSorterScreen(BeeSorterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 232;
        this.inventoryLabelY = this.imageHeight - 94;
        beeTypes = new byte[6];
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 6; i++) {
            beeTypes[i] = menu.getFilters()[i];
            int finalI = i;
            addRenderableWidget(new BeeTypeWidget(leftPos + 9, topPos + 19 + i*18, 16, 16, beeTypes[i], state -> beeTypes[finalI] = (byte) state.ordinal()));
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void onClose() {
        super.onClose();
        List<String> speciesSlots = new ArrayList<>();
        for (int i = 0; i < BeeSorterMenu.SLOT_COUNT; i++) {
            if (!menu.getSlot(i).hasItem())
                speciesSlots.add("");
            else
                speciesSlots.add(menu.getSlot(i).getItem().getTag().getString(GeneticHelper.SPECIES));
        }
        PacketDistributor.sendToServer(new UpdateSorterServerbound(menu.getPos(), beeTypes, speciesSlots));
    }

    @Override
    protected List<Component> getTooltipFromContainerItem(ItemStack pStack) {
        if (hoveredSlot instanceof FakeSpeciesSlot)
            return List.of(GeneticHelper.getTranslationKey(GeneticHelper.getSpecies(pStack, true)));
        return super.getTooltipFromContainerItem(pStack);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (hoveredSlot instanceof FakeSpeciesSlot fakeSlot) {
            fakeSlot.set(menu.getCarried(), pButton == 0);
            return true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
