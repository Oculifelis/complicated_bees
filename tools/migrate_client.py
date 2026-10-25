from pathlib import Path
import re

root=Path(__file__).resolve().parents[1]
base=root/'complicated_bees/src/main/java/com/accbdd/complicated_bees'

def write(name, text):
    (base/name).write_text(text,encoding='utf-8')

write('client/BeeModel.java', '''package com.accbdd.complicated_bees.client;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.mojang.serialization.MapCodec;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4fc;

/** Bakes resource-pack bee geometry and selects it using the stack's primary species. */
public record BeeModel() implements ItemModel.Unbaked {
    public static final MapCodec<BeeModel> CODEC = MapCodec.unit(BeeModel::new);
    private static Set<Identifier> models() {
        Set<Identifier> result = new HashSet<>();
        Minecraft.getInstance().getResourceManager().listResources("models/bee", id -> id.getPath().endsWith(".json")).keySet().forEach(id ->
            result.add(id.withPath(id.getPath().substring(7, id.getPath().length()-5))));
        return result;
    }
    public MapCodec<BeeModel> type() { return CODEC; }
    public void resolveDependencies(ResolvableModel.Resolver resolver) { models().forEach(resolver::markDependency); }
    public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
        Map<Identifier,ItemModel> baked=new HashMap<>();
        for (Identifier id: models()) baked.put(id, new CuboidItemModelWrapper.Unbaked(id, Optional.empty(),
            List.of(new ColorHandlers.Tint(0),new ColorHandlers.Tint(1))).bake(context,transformation));
        return (output,stack,resolver,display,level,owner,seed) -> {
            var species=GeneticHelper.getSpecies(stack,true);
            int index=stack.is(ItemsRegistration.QUEEN.get())?2:stack.is(ItemsRegistration.PRINCESS.get())?1:0;
            ItemModel model=species==null?null:baked.get(species.getModels().get(index));
            if(model==null) model=context.missingItemModel(transformation);
            model.update(output,stack,resolver,display,level,owner,seed);
        };
    }
}
''')

write('client/ColorHandlers.java','''package com.accbdd.complicated_bees.client;
import com.accbdd.complicated_bees.block.BeeNestBlock;
import com.accbdd.complicated_bees.block.entity.BeeNestBlockEntity;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import java.util.List;

public final class ColorHandlers {
    public record Tint(int index) implements ItemTintSource {
        public static final MapCodec<Tint> CODEC=Codec.INT.optionalFieldOf("index",0).xmap(Tint::new,Tint::index);
        public MapCodec<Tint> type(){return CODEC;}
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity owner){
            return ARGB.opaque(stack.getItem() instanceof BeeItem?BeeItem.getItemColor(stack,index):
                stack.getItem() instanceof CombItem?CombItem.getItemColor(stack,index):BeeNestBlock.getItemColor(stack,index));
        }
    }
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.ItemTintSources event){
        event.register(Identifier.fromNamespaceAndPath("complicated_bees","color"),Tint.CODEC);
    }
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.BlockTintSources event){
        event.register(List.of(new BlockTintSource(){
            public int color(BlockState state){return -1;}
            public int colorInWorld(BlockState state,BlockAndTintGetter level,BlockPos pos){
                return ARGB.opaque(BeeNestBlockEntity.getNestColor(state,level,pos,0));
            }
        }),BlocksRegistration.BEE_NEST.get());
    }
}
''')

# Obsolete model-loader code is superseded by BeeModel; retain it as port reference.
for name in ['OptimizedBeeModelBuilder.java','OptimizedBeeModelLoader.java']:
    source=base/'client'/name
    dest=root/'complicated_bees/src/legacy-client/java/com/accbdd/complicated_bees/client'/name
    dest.parent.mkdir(parents=True,exist_ok=True)
    source.replace(dest)

path=base/'ComplicatedBees.java'
text=path.read_text(encoding='utf-8')
text=text.replace('import com.accbdd.complicated_bees.client.OptimizedBeeModelLoader;\n','').replace('import net.minecraft.client.resources.model.ModelIdentifier;\n','')
text=text.replace('SignRenderer','StandingSignRenderer').replace('HangingStandingSignRenderer','HangingSignRenderer')
start=text.index('        @SubscribeEvent\n        public static void registerGeometryLoaders')
end=text.index('        @SubscribeEvent\n        public static void registerParticleProviders',start)
text=text[:start]+'''        @SubscribeEvent
        public static void registerItemModels(net.neoforged.neoforge.client.event.RegisterItemModelsEvent event) {
            event.register(Identifier.fromNamespaceAndPath(MODID, "bee"), BeeModel.CODEC);
        }

'''+text[end:]
text=re.sub(r'        @SubscribeEvent\s+public static void clearTextureCache\(ModelEvent.ModifyBakingResult event\) \{\s*BeeModel.clearCaches\(\);\s*\}', '', text)
path.write_text(text,encoding='utf-8')

path=base/'particle/BeeParticle.java'
text=path.read_text(encoding='utf-8').replace('extends TextureSheetParticle','extends SingleQuadParticle')
text=text.replace('pXSpeed, pYSpeed, pZSpeed);','pXSpeed, pYSpeed, pZSpeed, spriteSet.get(net.minecraft.util.RandomSource.create()));',1)
text=text.replace('        setSpriteFromAge(spriteSet);','')
text=text.replace('ParticleRenderType getRenderType()', 'SingleQuadParticle.Layer getLayer()').replace('ParticleRenderType.PARTICLE_SHEET_OPAQUE','SingleQuadParticle.Layer.OPAQUE')
text=text.replace('double xSpeed, double ySpeed, double zSpeed)', 'double xSpeed, double ySpeed, double zSpeed, net.minecraft.util.RandomSource random)')
path.write_text(text,encoding='utf-8')

for name in ['DiscoverToast','ResearchToast']:
    path=base/'client'/(name+'.java')
    text=path.read_text(encoding='utf-8').replace('import net.minecraft.client.gui.components.toasts.ToastComponent;','import net.minecraft.client.gui.components.toasts.ToastManager;\nimport net.minecraft.client.gui.Font;')
    start=text.index('    public ',text.index('    public '+name+'(')+12)
    # Replace the old render/update combined callback with the native split lifecycle.
    icon='species.toStack(ItemsRegistration.DRONE.get())' if name=='DiscoverToast' else 'mutation.getResultSpecies().toStack(ItemsRegistration.DRONE.get())'
    text=text[:start]+'''    private Visibility wanted = Visibility.SHOW;
    public Visibility getWantedVisibility(){return wanted;}
    public void update(ToastManager manager,long visibleMs){
        wanted=visibleMs>=5000*manager.getNotificationDisplayTimeMultiplier()?Visibility.HIDE:Visibility.SHOW;
    }
    public void extractRenderState(GuiGraphicsExtractor graphics,Font font,long visibleMs){
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,TEXTURE,0,0,0,0,width(),height(),256,256);
        graphics.text(font,TITLE_TEXT,30,7,-11534256,false);
        graphics.text(font,descriptionText,30,18,-16777216,false);
        graphics.fakeItem('''+icon+''',8,8);
    }
}
'''
    path.write_text(text,encoding='utf-8')
