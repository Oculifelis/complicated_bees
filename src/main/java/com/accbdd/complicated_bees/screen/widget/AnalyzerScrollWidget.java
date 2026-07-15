package com.accbdd.complicated_bees.screen.widget;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.GeneEffect;
import com.accbdd.complicated_bees.bees.gene.GeneTolerant;
import com.accbdd.complicated_bees.bees.gene.IGene;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.item.PrincessItem;
import com.accbdd.complicated_bees.registry.GeneRegistration;
import com.accbdd.complicated_bees.screen.AbstractAnalyzerMenu;
import com.accbdd.complicated_bees.util.GuiHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;
import static com.accbdd.complicated_bees.util.GuiHelper.drawRightAlignedText;
import static com.accbdd.complicated_bees.util.GuiHelper.drawWrappedText;

public class AnalyzerScrollWidget extends AbstractScrollWidget {
    private static final ResourceLocation GUI = ResourceLocation.tryBuild(MODID, "textures/gui/analyzer.png");
    private static final int ACTIVE_COL = 84;
    private static final int INACTIVE_COL = 150;
    private static final int INDENT = 10;
    private static final int LINE_HEIGHT = 12;
    private static final int PADDING = 4;

    private final AbstractAnalyzerMenu menu;
    private int mouseX;
    private int mouseY;
    public ItemStack hoveredStack = null;
    public Component geneTooltip = null;
    private int nextLine;

    public AnalyzerScrollWidget(int pX, int pY, int pWidth, int pHeight, AbstractAnalyzerMenu menu) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.menu = menu;
        nextLine = 0;
    }

    @Override
    protected int getInnerHeight() {
        return menu.isBeeAnalyzed() ? 500 : 150;
    }

    @Override
    protected double scrollRate() {
        return 10;
    }

    @Override
    protected void renderBorder(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight) {

    }

    @Override
    protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (menu.isBeeAnalyzed())
            drawGeneInfo(graphics, menu.getSlot(1).getItem(), mouseX, mouseY);
        else {
            drawWrappedText(graphics,
                    2 + getX(),
                    2 + getY(),
                    0xFFFFFF,
                    LINE_HEIGHT,
                    getWidth(),
                    PADDING,
                    Component.translatable("gui.complicated_bees.analyzer_line_1"),
                    Component.translatable("gui.complicated_bees.analyzer_line_2"),
                    Component.translatable("gui.complicated_bees.analyzer_line_3"),
                    Component.translatable("gui.complicated_bees.analyzer_line_4"));
            if (scrollAmount() > getMaxScrollAmount())
                setScrollAmount(getMaxScrollAmount());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    private void drawGeneInfo(GuiGraphics graphics, ItemStack bee, int mouseX, int mouseY) {
        nextLine = PADDING;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        drawText(graphics, Component.translatable("gui.complicated_bees.analyzer.active"), ACTIVE_COL, nextLine, 0xFFFFFF);
        drawText(graphics, Component.translatable("gui.complicated_bees.analyzer.inactive"), INACTIVE_COL, nextLine, 0xFFFFFF);
        lineBreak();
        lineBreak();

        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.species_label"), bee, GeneRegistration.SPECIES.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.lifespan_label.short"), bee, GeneRegistration.LIFESPAN.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.productivity_label"), bee, GeneRegistration.PRODUCTIVITY.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.flower_label"), bee, GeneRegistration.FLOWER.get());
        lineBreak();

        drawTolerantGeneValues(graphics, Component.translatable("gene.complicated_bees.humidity_label"), bee, GeneRegistration.HUMIDITY.get());
        drawTolerantGeneValues(graphics, Component.translatable("gene.complicated_bees.temperature_label"), bee, GeneRegistration.TEMPERATURE.get());
        lineBreak();

        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.territory_label"), bee, GeneRegistration.TERRITORY.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.effect_label"), bee, GeneRegistration.EFFECT.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.fertility_label"), bee, GeneRegistration.FERTILITY.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.active_time_label"), bee, GeneRegistration.ACTIVE_TIME.get());
        lineBreak();

        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.cave_dwelling_label"), bee, GeneRegistration.CAVE_DWELLING.get());
        drawGeneValues(graphics, Component.translatable("gene.complicated_bees.weatherproof_label"), bee, GeneRegistration.WEATHERPROOF.get());
        lineBreak();
        lineBreak();

        drawProducts(graphics, bee);
        lineBreak();

        if (bee.is(ItemTagGenerator.ROYAL)) {
            drawTextCentered(graphics, Component.translatable("gui.complicated_bees.generations", PrincessItem.getGeneration(bee)), 107, nextLine, 0x8cf536);
            lineBreak();
            lineBreak();
        }

        lineBreak();

        drawTaxonomy(graphics, bee);
        lineBreak();
        drawFlavor(graphics, bee);
    }

    private void drawText(GuiGraphics graphics, Component component, int x, int color) {
        graphics.drawString(Minecraft.getInstance().font, component, x + getX(), nextLine + getY(), color, false);
        nextLine += LINE_HEIGHT;
    }

    private void drawText(GuiGraphics graphics, Component component, int x, int y, int color) {
        graphics.drawString(Minecraft.getInstance().font, component, x + getX(), y + getY(), color, false);
    }

    private void drawTextCentered(GuiGraphics graphics, Component component, int x, int y, int color) {
        graphics.drawCenteredString(Minecraft.getInstance().font, component, x + getX(), y + getY(), color);
    }

    private void lineBreak() {
        nextLine += LINE_HEIGHT / 2;
    }

    private int getAdjustedMouseY() {
        return (int) (mouseY + scrollAmount() - getY());
    }

    private int getAdjustedMouseX() {
        return mouseX - getX();
    }

    private void drawGeneValues(GuiGraphics graphics, Component label, ItemStack bee, IGene<?> gene) {
        IGene<?> active = GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), true);
        IGene<?> inactive = GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), false);
        drawText(graphics, label, PADDING, nextLine, 0xFFFFFF);
        drawText(graphics, active.getTranslationKey(), ACTIVE_COL, nextLine, active.isDominant() ? 0xE63225 : 0x257FE6);
        drawText(graphics, inactive.getTranslationKey(), INACTIVE_COL, nextLine, inactive.isDominant() ? 0xE63225 : 0x257FE6);
        if (gene instanceof GeneEffect) {
            if (getAdjustedMouseX() >= ACTIVE_COL && getAdjustedMouseX() <= ACTIVE_COL + Minecraft.getInstance().font.width(active.getTranslationKey()) && getAdjustedMouseY() >= nextLine && getAdjustedMouseY() <= nextLine + LINE_HEIGHT)
                geneTooltip = ((GeneEffect) active).getDescriptionKey();
            else if ((getAdjustedMouseX() >= INACTIVE_COL && getAdjustedMouseX() <= INACTIVE_COL + Minecraft.getInstance().font.width(active.getTranslationKey()) && getAdjustedMouseY() >= nextLine && getAdjustedMouseY() <= nextLine + Minecraft.getInstance().font.lineHeight))
                geneTooltip = ((GeneEffect) inactive).getDescriptionKey();
            else
                geneTooltip = null;
        }
        nextLine += LINE_HEIGHT;
    }

    private void drawGeneValues(GuiGraphics graphics, Component label, ItemStack bee, IGene<?> gene, int y) {
        IGene<?> active = GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), true);
        IGene<?> inactive = GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), false);
        drawText(graphics, label, PADDING, y, 0xFFFFFF);
        drawText(graphics, active.getTranslationKey(), ACTIVE_COL, y, active.isDominant() ? 0xE63225 : 0x257FE6);
        drawText(graphics, inactive.getTranslationKey(), INACTIVE_COL, y, inactive.isDominant() ? 0xE63225 : 0x257FE6);
    }

    private void drawGeneTolerance(GuiGraphics graphics, ItemStack bee, GeneTolerant<?> gene, int x, int x2, int y) {
        GeneTolerant<?> active = (GeneTolerant<?>) GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), true);
        GeneTolerant<?> inactive = (GeneTolerant<?>) GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), false);
        drawText(graphics, active.getTolerance().getTranslationKey(), x, y, active.isDominant() ? 0xE63225 : 0x257FE6);
        drawText(graphics, inactive.getTolerance().getTranslationKey(), x2, y, inactive.isDominant() ? 0xE63225 : 0x257FE6);
    }

    private void drawToleranceIcons(GuiGraphics graphics, ItemStack bee, GeneTolerant<?> gene, int x, int x2, int y) {
        x += getX();
        x2 += getX();
        y += getY() - 2;
        GeneTolerant<?> active = (GeneTolerant<?>) GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), true);
        GeneTolerant<?> inactive = (GeneTolerant<?>) GeneticHelper.getGene(bee, ComplicatedBees.GENE_REGISTRY.get().getKey(gene), false);
        if (active.getTolerance().down != 0 && active.getTolerance().up != 0) {
            graphics.blit(GUI, x, y, 14, 246, 7, 10);
        } else if (active.getTolerance().down != 0) {
            graphics.blit(GUI, x, y, 7, 246, 7, 10);
        } else if (active.getTolerance().up != 0) {
            graphics.blit(GUI, x, y, 0, 246, 7, 10);
        } else {
            graphics.blit(GUI, x, y, 21, 246, 7, 10);
        }

        if (inactive.getTolerance().down != 0 && inactive.getTolerance().up != 0) {
            graphics.blit(GUI, x2, y, 14, 246, 7, 10);
        } else if (inactive.getTolerance().down != 0) {
            graphics.blit(GUI, x2, y, 7, 246, 7, 10);
        } else if (inactive.getTolerance().up != 0) {
            graphics.blit(GUI, x2, y, 0, 246, 7, 10);
        } else {
            graphics.blit(GUI, x2, y, 21, 246, 7, 10);
        }
    }

    private void drawTolerantGeneValues(GuiGraphics graphics, Component label, ItemStack bee, GeneTolerant<?> gene) {
        drawGeneValues(graphics, label, bee, gene, nextLine);
        nextLine += LINE_HEIGHT;
        drawText(graphics, Component.translatable("gene.complicated_bees.tolerance_label"), PADDING + INDENT, nextLine, 0xFFFFFF);
        drawToleranceIcons(graphics, bee, gene, ACTIVE_COL, INACTIVE_COL, nextLine);
        drawGeneTolerance(graphics, bee, gene, ACTIVE_COL + 10, INACTIVE_COL + 10, nextLine);
        nextLine += LINE_HEIGHT;
    }

    private void drawTaxonomy(GuiGraphics graphics, ItemStack bee) {
        Species species = GeneticHelper.getSpecies(bee, true);
        drawText(graphics, Component.translatable("gui.complicated_bees.taxonomy"), PADDING, 0xFFFFFF);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0x66F2E7, Component.translatable("gui.complicated_bees.kingdom_label"));
        drawText(graphics, Component.translatable("gui.complicated_bees.kingdom"), PADDING, 0x66F2E7);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0x76E3AA, Component.translatable("gui.complicated_bees.class_label"));
        drawText(graphics, Component.translatable("gui.complicated_bees.class"), PADDING + INDENT, 0x76E3AA);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0x9BCE71, Component.translatable("gui.complicated_bees.order_label"));
        drawText(graphics, Component.translatable("gui.complicated_bees.order"), PADDING + INDENT * 2, 0x9BCE71);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0xC1B34B, Component.translatable("gui.complicated_bees.family_label"));
        drawText(graphics, Component.translatable("gui.complicated_bees.family"), PADDING + INDENT * 3, 0xC1B34B);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0xE19248, Component.translatable("gui.complicated_bees.genus_label"));
        drawText(graphics, GeneticHelper.getGenusTaxonomyKey(species), PADDING + INDENT * 4, 0xE19248);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0xF26D63, Component.translatable("gui.complicated_bees.species_taxonomy_label"));
        drawText(graphics, GeneticHelper.getSpeciesTaxonomyKey(species), PADDING + INDENT * 5, 0xF26D63);

        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0xFFFFFF, Component.translatable("gui.complicated_bees.authority_label").append(GeneticHelper.getAuthorityKey(species)));
        nextLine += LINE_HEIGHT;
    }

    private void drawFlavor(GuiGraphics graphics, ItemStack bee) {
        Species species = GeneticHelper.getSpecies(bee, true);
        graphics.pose().pushPose();
        graphics.pose().translate(getX(), getY(), 0);
        nextLine = GuiHelper.drawWrappedText(graphics,
                PADDING,
                nextLine,
                0xA4A4A4,
                LINE_HEIGHT,
                getWidth(),
                PADDING,
                GeneticHelper.getFlavorTextKey(species).withStyle(ChatFormatting.ITALIC));
        graphics.pose().popPose();
        nextLine -= LINE_HEIGHT / 2;
        drawRightAlignedText(graphics, getWidth() - PADDING + getX(), nextLine + getY(), 0xA4A4A4, Component.literal("-").append(GeneticHelper.getFlavorTextAuthorKey(species)));
        nextLine += LINE_HEIGHT;
    }

    private void drawProducts(GuiGraphics graphics, ItemStack bee) {
        Species species = GeneticHelper.getSpecies(bee, true);
        List<Product> products = species.getProducts();
        List<Product> specProducts = species.getSpecialtyProducts();
        drawText(graphics, Component.translatable("gui.complicated_bees.products_label"), PADDING, 0xFFFFFF);
        this.hoveredStack = null;
        for (int i = 0; i < products.size(); i++) {
            int x = PADDING + (51 * (i % 4)) + getX();
            int y = nextLine + (21 * (i / 4)) + getY();
            graphics.renderItem(products.get(i).getStack(), x, y);
            graphics.drawString(Minecraft.getInstance().font, Component.literal("- " + (int) (products.get(i).getChance() * 100) + "%"), x + 18, y + 5, 0xFFFFFF);
            if (mouseX >= x && mouseX <= x + 16 && mouseY + scrollAmount() >= y && mouseY + scrollAmount() <= y + 16) {
                this.hoveredStack = products.get(i).getStack();
            }
        }
        nextLine += 21 * (products.size() / 4 + 1);

        drawText(graphics, Component.translatable("gui.complicated_bees.specialty_products_label"), PADDING, 0xFFFFFF);
        for (int i = 0; i < specProducts.size(); i++) {
            int x = PADDING + (51 * (i % 4)) + getX();
            int y = nextLine + (21 * (i / 4)) + getY();
            graphics.renderItem(specProducts.get(i).getStack(), x, y);
            graphics.drawString(Minecraft.getInstance().font, Component.literal("- " + (int) (specProducts.get(i).getChance() * 100) + "%"), x + 18, y + 5, 0xFFFFFF);
            if (mouseX >= x && mouseX <= x + 16 && mouseY + scrollAmount() >= y && mouseY + scrollAmount() <= y + 16) {
                this.hoveredStack = specProducts.get(i).getStack();
            }
        }
        nextLine += 21 * (specProducts.size() / 4 + 1);
    }
}
