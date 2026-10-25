from pathlib import Path
import re
root=Path(__file__).resolve().parents[1]
base=root/'complicated_bees/src/main/java/com/accbdd/complicated_bees'
for p in base.rglob('*.java'):
 s=p.read_text(encoding='utf-8')
 s=s.replace('net.minecraft.world.item.component.consume_effects','net.minecraft.world.item.consume_effects')
 s=s.replace('loadAdditional(pkt.get(), lookupProvider)','loadWithComponents(input)')
 s=s.replace('getCraftingRemainder().isEmpty()','getCraftingRemainder().create().isEmpty()')
 s=s.replace('net.minecraft.world.level.storage.loot.BuiltInLootTables.EMPTY','net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,net.minecraft.resources.Identifier.withDefaultNamespace("empty"))')
 s=s.replace('level == null ? net.minecraft.core.registries.BuiltInRegistries.REGISTRY : level.registryAccess()','level == null ? net.minecraft.core.RegistryAccess.fromRegistryOfRegistries(net.minecraft.core.registries.BuiltInRegistries.REGISTRY) : level.registryAccess()')
 s=s.replace('protected void extractBackground(GuiGraphicsExtractor','public void extractBackground(GuiGraphicsExtractor')
 s=s.replace('playerInv.selected','playerInv.getSelectedSlot()')
 if p.name=='MeterItem.java':s=s.replace(',true);',');')
 if p.name=='EntitiesRegistration.java':s=s.replace('Identifier.fromNamespaceAndPath','net.minecraft.resources.Identifier.fromNamespaceAndPath')
 if p.name=='DiscoverCommands.java':
  s=s.replace('context -> context.hasPermission(2)','Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)')
  s=s.replace('IdentifierArgument.id()).suggests(DiscoverCommands::getMutationSuggest)','net.minecraft.commands.arguments.ResourceKeyArgument.key(Registries.RECIPE)).suggests(DiscoverCommands::getMutationSuggest)')
  s=s.replace('IdentifierArgument.getRecipe(', 'net.minecraft.commands.arguments.ResourceKeyArgument.getRecipe(')
 if p.name=='ApiaryComponentProvider.java':s=s.replace('config.get(', 'iPluginConfig.get(')
 if 'screen' in p.parts:
  # Actual event entrypoints; keep the old local coordinates inside each handler.
  for method in ['mouseClicked','mouseReleased']:
   pattern=r'public boolean '+method+r'\(double (\w+), double (\w+), int (\w+)\) \{'
   def repl(m):
    return 'public boolean '+method+'(net.minecraft.client.input.MouseButtonEvent event'+(', boolean doubleClick' if method=='mouseClicked' else '')+') {\n        double '+m[1]+'=event.x(), '+m[2]+'=event.y(); int '+m[3]+'=event.button();'
   s=re.sub(pattern,repl,s)
   s=re.sub(r'super\.'+method+r'\(\w+, \w+, \w+\)', 'super.'+method+'(event'+(', doubleClick' if method=='mouseClicked' else '')+')',s)
  s=re.sub(r'public void onClick\(double (\w+), double (\w+)\) \{',r'public void onClick(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {\n        double \1=event.x(), \2=event.y();',s)
  s=re.sub(r'super.onClick\(\w+, \w+\)', 'super.onClick(event, doubleClick)',s)
  s=re.sub(r'this.onClick\(\w+, \w+\)', 'this.onClick(event, doubleClick)',s)
  s=s.replace('this.clicked(', 'this.isMouseOver(').replace('this.isValidClickButton(pButton)','this.isValidClickButton(event.buttonInfo())')
  if p.name in ['MicroscopeScreen.java','BeeTypeWidget.java']:s=s.replace('void extractWidgetRenderState(', 'void extractContents(')
  s=s.replace('void onPress()', 'void onPress(net.minecraft.client.input.InputWithModifiers input)')
  s=s.replace('graphics.pose().rotateAround(Axis.ZP.rotationDegrees((float) (Math.atan2(x2-x, y2-y) * -180 / Math.PI)), x, y, 0)','graphics.pose().rotateAbout((float)-Math.atan2(x2-x,y2-y), x, y)')
 if 'compat' in p.parts and 'jei' in p.parts:
  s=s.replace('void extractRenderState(GuiGraphicsExtractor guiGraphics,', 'void render(GuiGraphicsExtractor guiGraphics,')
  s=s.replace('RecipeManager manager = com.accbdd.complicated_bees.port.PortRecipes.manager(Minecraft.getInstance().getConnection())','var manager = com.accbdd.complicated_bees.port.PortRecipes.manager(Minecraft.getInstance().level)')
  s=s.replace('.registry(FlowerRegistration.FLOWER_REGISTRY_KEY)', '.lookup(FlowerRegistration.FLOWER_REGISTRY_KEY)')
  s=s.replace('RecipeIngredientRole.CATALYST','RecipeIngredientRole.RENDER_ONLY')
  s=s.replace('    @Override\n    public String getLegacyStringSubtypeInfo','    public String getLegacyStringSubtypeInfo')
  s=s.replace('    @Override\n    public String getUniqueId','    public String getUniqueId')
  if 'implements IRecipeCategory<' in s:
   s=s.replace('    @Override\n    public IDrawable getBackground()', '    public int getWidth(){return getBackground().getWidth();}\n    public int getHeight(){return getBackground().getHeight();}\n    public IDrawable getBackground()')
   recipe_type=re.search(r'implements IRecipeCategory<(.+)> \{',s)[1]
   if 'void draw(' not in s:
    insert='\n    public void draw('+recipe_type+' recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView slots, net.minecraft.client.gui.GuiGraphicsExtractor graphics, double mouseX, double mouseY){getBackground().draw(graphics);}\n'
    s=s.replace('    public int getWidth()',insert+'    public int getWidth()')
   else:s=re.sub(r'(public void draw\([^{}]+\) \{)',r'\1\n        getBackground().draw(guiGraphics);',s)
  s=re.sub(r'\s*RenderSystem\.(?:disableDepthTest|enableDepthTest|enableBlend|disableBlend)\(\);','',s)
  if p.name=='BlockIngredientRenderer.java':
   s=s.replace('import net.neoforged.neoforge.client.model.data.ModelData;\n','')
   start=s.index('                    TextureAtlasSprite sprite =')
   end=s.index('\n                } else',start)
   s=s[:start]+'''                    TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(block.block().defaultBlockState()).particleIcon();
                    guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,sprite,0,0,16,16);
'''+s[end:]
 p.write_text(s,encoding='utf-8')
p=base/'port/ScrollWidget.java';s=p.read_text(encoding='utf-8').replace('    protected int innerPadding()', '    protected void renderDecorations(GuiGraphicsExtractor graphics) {}\n    protected int innerPadding()');p.write_text(s,encoding='utf-8')
