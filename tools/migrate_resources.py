from pathlib import Path
import json,re
root=Path(__file__).resolve().parents[1]

def ingredient(value):
    if isinstance(value,dict) and set(value)=={'item'}:return value['item']
    if isinstance(value,dict) and set(value)=={'tag'}:return '#'+value['tag']
    if isinstance(value,list):return [ingredient(x) for x in value]
    return value

for project in ['complicated_bees','genetic_industry']:
    for folder in ['src/main/resources','src/generated/resources']:
        base=root/project/folder
        if not base.exists():continue
        # Singular resource directory names introduced after the addon's 1.20.1 baseline.
        for path in list(base.rglob('*.json')):
            parts=path.relative_to(base).parts
            if not parts or parts[0]!='data':continue
            rename={'recipes':'recipe','loot_tables':'loot_table','advancements':'advancement','structures':'structure','blocks':'block','items':'item','entity_types':'entity_type'}
            newparts=[rename.get(x,x) if i>=2 else x for i,x in enumerate(parts)]
            dest=base.joinpath(*newparts)
            if dest!=path:
                dest.parent.mkdir(parents=True,exist_ok=True);path.replace(dest)
        for path in base.rglob('*.json'):
            value=json.loads(path.read_text(encoding='utf-8-sig'))
            parts=path.relative_to(base).parts
            if parts[0]=='data' and 'recipe' in parts:
                for key in ['input','ingredient','ingredients']:
                    if key in value:value[key]=ingredient(value[key])
                if 'key' in value:value['key']={k:ingredient(v) for k,v in value['key'].items()}
                if isinstance(value.get('result'),dict) and 'item' in value['result']:
                    value['result']['id']=value['result'].pop('item')
                if value.get('type')=='genindustry_bees:furnace_recycling':
                    item=ingredient(value.pop('item'))
                    value.update(ingredient=item,result={'id':item},experience=0.1,cookingtime=value.pop('time',200))
            if parts[0]=='assets' and 'models' in parts:
                if 'base_model' in value:value=value['base_model']
                if value.get('loader') in ['complicated_bees:bee_model','complicated_bees:optimized_bee_model']:value.pop('loader')
            path.write_text(json.dumps(value,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
        # Every item needs a modern item definition. Preserve existing geometry/textures.
        for model in base.glob('assets/*/models/item/**/*.json'):
            parts=model.relative_to(base).parts
            namespace=parts[1];itempath='/'.join(parts[4:])[:-5]
            definition=base/'assets'/namespace/'items'/(itempath+'.json')
            if definition.exists():continue
            node={'type':'minecraft:model','model':namespace+':item/'+itempath}
            if namespace=='complicated_bees' and itempath in ['drone','queen','princess']:
                node={'type':'complicated_bees:bee'}
            elif namespace=='complicated_bees' and itempath in ['comb','bee_nest']:
                node['tints']=[{'type':'complicated_bees:color','index':i} for i in range(2)]
            elif namespace=='genindustry_bees' and itempath=='gene_sample':
                node['tints']=[{'type':'genindustry_bees:gene'},{'type':'minecraft:constant','value':-1}]
            if namespace=='genindustry_bees' and itempath=='genetic_template':
                old=json.loads(model.read_text(encoding='utf-8'))
                entries=[]
                for override in old.get('overrides',[]):
                    threshold=override.get('predicate',{}).get('genindustry_bees:gene_count')
                    if threshold is not None:entries.append({'threshold':threshold,'model':{'type':'minecraft:model','model':override['model']}})
                if entries:node={'type':'minecraft:range_dispatch','property':'genindustry_bees:gene_count','entries':entries,'fallback':node}
            definition.parent.mkdir(parents=True,exist_ok=True)
            definition.write_text(json.dumps({'model':node},indent=2)+'\n',encoding='utf-8')
        # Modern equipment texture locations retain the original art.
        for source in base.glob('assets/*/textures/models/armor/*_layer_*.png'):
            ns=source.relative_to(base).parts[1]
            part='humanoid_leggings' if '_layer_2' in source.stem else 'humanoid'
            dest=base/'assets'/ns/'textures/equipment'/part/'apiarist.png'
            dest.parent.mkdir(parents=True,exist_ok=True);dest.write_bytes(source.read_bytes())

base=root/'complicated_bees/src/main/resources'
for name,obj in {
    'assets/complicated_bees/equipment/apiarist.json':{'layers':{'humanoid':[{'texture':'complicated_bees:apiarist'}],'humanoid_leggings':[{'texture':'complicated_bees:apiarist'}]}},
    'data/complicated_bees/tags/item/repairs_apiarist.json':{'values':['complicated_bees:woven_mesh']},
}.items():
    p=base/name;p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(obj,indent=2)+'\n',encoding='utf-8')

# The descriptor for the old Forge runtime must not appear in the NeoForge JAR.
old=root/'genetic_industry/src/main/resources/META-INF/mods.toml'
if old.exists():old.unlink()
