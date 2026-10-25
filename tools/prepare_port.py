from pathlib import Path
import shutil

root = Path(__file__).resolve().parents[1]
for name in ('complicated_bees', 'genetic_industry'):
    source = root / 'reference' / name
    destination = root / name
    shutil.copytree(source / 'src', destination / 'src', dirs_exist_ok=True)
    shutil.copyfile(source / 'LICENSE.txt', destination / 'LICENSE.txt')
for name in ('gradlew', 'gradlew.bat'):
    shutil.copyfile(root / 'reference/complicated_bees' / name, root / name)
shutil.copytree(root / 'reference/complicated_bees/gradle', root / 'gradle', dirs_exist_ok=True)
wrapper = root / 'gradle/wrapper/gradle-wrapper.properties'
wrapper.write_text(wrapper.read_text().replace('gradle-9.4.1', 'gradle-9.2.1'))

# Pre-generated data is preserved; generators are maintained separately from runtime code.
source = root / 'complicated_bees/src/main/java/com/accbdd/complicated_bees/datagen'
destination = root / 'complicated_bees/src/datagen/java/com/accbdd/complicated_bees/datagen'
destination.parent.mkdir(parents=True, exist_ok=True)
shutil.copytree(source, destination, dirs_exist_ok=True)
for name in ('condition', 'builtin'):
    shutil.move(str(destination / name), str(source / name)) if source.exists() else None
    if not source.exists():
        source.mkdir(parents=True)
        shutil.move(str(destination / name), str(source / name))
for path in (root / 'genetic_industry/src/main/java').rglob('*.java'):
    text = path.read_text(encoding='utf-8')
    replacements = {
        'net.minecraftforge.eventbus.api': 'net.neoforged.bus.api',
        'net.minecraftforge': 'net.neoforged.neoforge',
        'net.neoforged.neoforge.api.distmarker': 'net.neoforged.api.distmarker',
        'net.neoforged.neoforge.fml': 'net.neoforged.fml',
        'net.neoforged.neoforge.event.': 'net.neoforged.neoforge.event.',
        'MinecraftForge': 'NeoForge',
        'ForgeConfigSpec': 'ModConfigSpec',
        'ForgeRegistries': 'BuiltInRegistries',
        'net.neoforged.neoforge.registries.BuiltInRegistries': 'net.minecraft.core.registries.BuiltInRegistries',
        'new ResourceLocation(': 'ResourceLocation.parse(',
    }
    for old, new in replacements.items():
        text = text.replace(old, new)
    path.write_text(text, encoding='utf-8')

for project in ('complicated_bees', 'genetic_industry'):
    for path in (root / project / 'src/main/java').rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        text = text.replace('ResourceLocation', 'Identifier').replace('net.minecraft.resources.Identifier', 'net.minecraft.resources.Identifier')
        text = text.replace('FastColor.ARGB32.', 'ARGB.').replace('net.minecraft.util.FastColor', 'net.minecraft.util.ARGB')
        text = text.replace('bus = EventBusSubscriber.Bus.MOD, ', '').replace(', bus = EventBusSubscriber.Bus.MOD', '')
        path.write_text(text, encoding='utf-8')

path = root / 'complicated_bees/src/main/java/com/accbdd/complicated_bees/ComplicatedBees.java'
text = path.read_text(encoding='utf-8').replace('import com.accbdd.complicated_bees.datagen.DataGenerators;\n', '').replace('        modEventBus.addListener(DataGenerators::generate);\n', '')
path.write_text(text, encoding='utf-8')
