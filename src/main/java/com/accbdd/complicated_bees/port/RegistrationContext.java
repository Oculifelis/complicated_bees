package com.accbdd.complicated_bees.port;
import java.util.function.*;
import net.minecraft.resources.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.*;

/** Supplies explicit registry keys to the upstream constructors during deferred creation. */
public final class RegistrationContext {
    private static final ThreadLocal<Identifier> CURRENT=new ThreadLocal<>();
    private static <T> T construct(Identifier id,Supplier<T> supplier){
        Identifier previous=CURRENT.get();CURRENT.set(id);
        try{return supplier.get();}finally{if(previous==null)CURRENT.remove();else CURRENT.set(previous);}
    }
    public static Item.Properties itemProperties(){return new Item.Properties().setId(ResourceKey.create(Registries.ITEM,java.util.Objects.requireNonNull(CURRENT.get(),"Item constructed outside registration")));}
    public static BlockBehaviour.Properties blockProperties(){
        var properties=BlockBehaviour.Properties.of();
        return CURRENT.get()==null?properties:properties.setId(blockKey());
    }
    public static ResourceKey<Block> blockKey(){return ResourceKey.create(Registries.BLOCK,java.util.Objects.requireNonNull(CURRENT.get(),"Block constructed outside registration"));}
    public static final class Items extends DeferredRegister.Items {
        public Items(String namespace){super(namespace);}
        @Override public <I extends Item> DeferredItem<I> register(String name,Function<Identifier,? extends I> factory){return super.register(name,id->construct(id,()->factory.apply(id)));}
    }
    public static final class Blocks extends DeferredRegister.Blocks {
        public Blocks(String namespace){super(namespace);}
        @Override public <B extends Block> DeferredBlock<B> register(String name,Function<Identifier,? extends B> factory){return super.register(name,id->construct(id,()->factory.apply(id)));}
    }
}
