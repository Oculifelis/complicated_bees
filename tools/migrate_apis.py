from pathlib import Path
import re

root = Path(__file__).resolve().parents[1]
for project in ('complicated_bees', 'genetic_industry'):
    for path in (root / project / 'src/main/java').rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        changes = {
            '.location()': '.identifier()',
            '.registryOrThrow(': '.lookupOrThrow(',
            '.getAllKeys()': '.keySet()',
            'net.minecraft.Util': 'net.minecraft.util.Util',
            'MobSpawnType': 'EntitySpawnReason',
            'DimensionDataStorage': 'SavedDataStorage',
            'net.minecraft.world.level.BlockAndTintGetter': 'net.minecraft.world.level.BlockAndLightGetter',
            'BlockAndTintGetter': 'BlockAndLightGetter',
            'net.minecraft.MethodsReturnNonnullByDefault': 'javax.annotation.ParametersAreNonnullByDefault',
            'MethodsReturnNonnullByDefault': 'ParametersAreNonnullByDefault',
            'FMLLoader.getDist()': 'FMLLoader.getCurrent().getDist()',
            '.getUnsafe()': '.get()',
            'Ingredient.CODEC_NONEMPTY': 'Ingredient.CODEC',
            'LootItemFunctionType': 'LootItemFunctionType',
            '.getDayTime()': '.getOverworldClockTime()',
            '.getMinBuildHeight()': '.getMinY()',
            '.getMaxBuildHeight()': '.getMaxY()',
            '.getToasts()': '.getToastManager()',
            'GuiGraphics': 'GuiGraphicsExtractor',
            '.drawString(': '.text(',
            '.drawCenteredString(': '.centeredText(',
            '.drawWordWrap(': '.textWithWordWrap(',
            '.renderItem(': '.item(',
            '.renderFakeItem(': '.fakeItem(',
            '.renderItemDecorations(': '.itemDecorations(',
            '.renderOutline(': '.outline(',
            '.pushPose()': '.pushMatrix()',
            '.popPose()': '.popMatrix()',
            'void renderBg(': 'void extractBackground(',
            'void renderLabels(': 'void extractLabels(',
            'void renderWidget(': 'void extractWidgetRenderState(',
            'void render(': 'void extractRenderState(',
            'super.render(': 'super.extractRenderState(',
            '.renderBackground(': '.extractBackground(',
            'void renderBackground(': 'void extractBackground(',
            'renderTooltip(': 'extractTooltip(',
            'ClickType': 'ContainerInput',
            '.getCompound(': '.getCompoundOrEmpty(',
        }
        for old, new in changes.items():
            text = text.replace(old, new)
        # Accessors whose old defaults are retained explicitly by the modern NBT API.
        for kind, default in [('String','""'),('Int','0'),('Long','0L'),('Float','0f'),('Double','0d'),('Boolean','false'),('Byte','(byte)0'),('Short','(short)0')]:
            text = re.sub(r'\.get'+kind+r'\(([^()]+)\)',lambda m:'.get'+kind+'Or('+m[1]+', '+default+')',text)
        text = re.sub(r'\.getList\(([^,()]+),\s*(?:Tag\.TAG_COMPOUND|10)\)',r'.getListOrEmpty(\1)',text)
        text = re.sub(r'\.isClientSide\b(?!\s*\()', '.isClientSide()', text)
        text = text.replace('BlockEntityType.Builder.of(', 'new BlockEntityType<>(').replace('.build(null)', '')
        if 'appendHoverText(' in text:
            text = re.sub(r'(appendHoverText\(ItemStack \w+, TooltipContext \w+, )List<Component> (\w+)',r'\1net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> \2',text)
            # Only tooltip builders change add -> accept.
            for var in re.findall(r'Consumer<Component> (\w+)', text):
                text = text.replace(var+'.add(', var+'.accept(')
            text = re.sub(r'super.appendHoverText\((\w+), (\w+), (\w+), (\w+)\)', r'super.appendHoverText(\1, \2, display, \3, \4)', text)
        path.write_text(text, encoding='utf-8')

# Remove accidentally duplicated copies created during the initial source split.
data = root/'complicated_bees/src/main/java/com/accbdd/complicated_bees/datagen'
for part in ('builtin','condition'):
    for path in (data/part/part).glob('*.java'):
        path.unlink()

for path in (root/'complicated_bees/src/main/java/com/accbdd/complicated_bees/recipe').rglob('*.java'):
    text = path.read_text(encoding='utf-8')
    text = text.replace('implements Recipe<', 'implements com.accbdd.complicated_bees.port.MachineRecipe<')
    # Modern Recipe is invariant in its input parameter.
    match = re.search(r'implements com\.accbdd\.complicated_bees\.port\.MachineRecipe<([^>]+)>', text)
    if match:
        text=text.replace('RecipeSerializer<?> getSerializer()',f'RecipeSerializer<? extends Recipe<{match[1]}>> getSerializer()')
        text=text.replace('RecipeType<?> getType()',f'RecipeType<? extends Recipe<{match[1]}>> getType()')
    path.write_text(text,encoding='utf-8')
