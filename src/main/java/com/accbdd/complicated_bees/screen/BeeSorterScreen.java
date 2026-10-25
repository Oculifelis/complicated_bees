package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.network.packet.UpdateSorterServerbound;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.screen.slot.FakeSpeciesSlot;
import com.accbdd.complicated_bees.screen.widget.BeeTypeWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeSorterScreen extends AbstractContainerScreen<BeeSorterMenu> {
    private static final Identifier GUI = Identifier.fromNamespaceAndPath(MODID, "textures/gui/bee_sorter.png");
    private byte[] beeTypes;
    public BeeSorterScreen(BeeSorterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, 176, 232);
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
    public void extractBackground(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY, float pPartialTick) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        com.accbdd.complicated_bees.port.PortGui.blit(pGuiGraphicsExtractor,GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY, float pPartialTick) {
        super.extractRenderState(pGuiGraphicsExtractor, pMouseX, pMouseY, pPartialTick);
        extractTooltip(pGuiGraphicsExtractor, pMouseX, pMouseY);
    }

    @Override
    public void onClose() {
        super.onClose();
        List<String> speciesSlots = new ArrayList<>();
        for (int i = 0; i < BeeSorterMenu.SLOT_COUNT; i++) {
            if (!menu.getSlot(i).hasItem())
                speciesSlots.add("");
            else
                speciesSlots.add(menu.getSlot(i).getItem().getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).species().toString());
        }
        net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new UpdateSorterServerbound(menu.getPos(), beeTypes, speciesSlots));
    }

    @Override
    protected List<Component> getTooltipFromContainerItem(ItemStack pStack) {
        if (hoveredSlot instanceof FakeSpeciesSlot)
            return List.of(GeneticHelper.getTranslationKey(GeneticHelper.getSpecies(pStack, true)));
        return super.getTooltipFromContainerItem(pStack);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        double pMouseX=event.x(), pMouseY=event.y(); int pButton=event.button();
        if (hoveredSlot instanceof FakeSpeciesSlot fakeSlot) {
            fakeSlot.set(menu.getCarried(), pButton == 0);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }
}
