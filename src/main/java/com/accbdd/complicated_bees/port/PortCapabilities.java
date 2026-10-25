package com.accbdd.complicated_bees.port;

import java.util.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.transfer.*;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.*;
import com.accbdd.complicated_bees.block.entity.AdaptedEnergyStorage;
import com.accbdd.complicated_bees.block.entity.CombinedEnergyStorage;

/** Internal legacy views plus transactional capabilities for 26.x automation. */
public final class PortCapabilities {
    @SuppressWarnings("unchecked")
    public static <T> T find(net.minecraft.world.level.Level level,BlockCapability<T,Direction> capability,net.minecraft.core.BlockPos pos,net.minecraft.world.level.block.state.BlockState state,BlockEntity entity,Direction side){
        T internal=level.getCapability(capability,pos,state,entity,side);
        if(internal!=null)return internal;
        if(capability==ItemHandler.BLOCK){var nativeHandler=level.getCapability(Capabilities.Item.BLOCK,pos,state,entity,side);return nativeHandler==null?null:(T)IItemHandler.of(nativeHandler);}
        if(capability==EnergyStorage.BLOCK){var nativeHandler=level.getCapability(Capabilities.Energy.BLOCK,pos,state,entity,side);return nativeHandler==null?null:(T)IEnergyStorage.of(nativeHandler);}
        return null;
    }
    public static final class ItemHandler {
        public static final BlockCapability<IItemHandler,Direction> BLOCK=BlockCapability.createSided(Identifier.fromNamespaceAndPath("complicated_bees","legacy_item"),IItemHandler.class);
    }
    public static final class EnergyStorage {
        public static final BlockCapability<IEnergyStorage,Direction> BLOCK=BlockCapability.createSided(Identifier.fromNamespaceAndPath("complicated_bees","legacy_energy"),IEnergyStorage.class);
    }
    @SuppressWarnings("unchecked")
    public static <T,C,B extends BlockEntity> void registerBlockEntity(RegisterCapabilitiesEvent event,BlockCapability<T,C> capability,BlockEntityType<B> type,ICapabilityProvider<B,C,T> provider){
        event.registerBlockEntity(capability,type,provider);
        if(capability==ItemHandler.BLOCK) event.registerBlockEntity(Capabilities.Item.BLOCK,type,(be,side)->{
            IItemHandler handler=(IItemHandler)provider.getCapability(be,(C)side);
            return handler==null?null:new Items((IItemHandlerModifiable)handler);
        });
        if(capability==EnergyStorage.BLOCK) event.registerBlockEntity(Capabilities.Energy.BLOCK,type,(be,side)->{
            IEnergyStorage handler=(IEnergyStorage)provider.getCapability(be,(C)side);
            return handler==null?null:new Energy(handler);
        });
    }
    private static final class Items extends SnapshotJournal<List<ItemStack>> implements ResourceHandler<ItemResource>{
        private final IItemHandlerModifiable handler;
        Items(IItemHandlerModifiable handler){this.handler=handler;}
        public int size(){return handler.getSlots();}
        public ItemResource getResource(int slot){return ItemResource.of(handler.getStackInSlot(slot));}
        public long getAmountAsLong(int slot){return handler.getStackInSlot(slot).getCount();}
        public long getCapacityAsLong(int slot,ItemResource resource){return Math.min(handler.getSlotLimit(slot),resource.isEmpty()?99:resource.getMaxStackSize());}
        public boolean isValid(int slot,ItemResource resource){return handler.isItemValid(slot,resource.toStack(1));}
        public int insert(int slot,ItemResource resource,int amount,TransactionContext tx){
            if(amount<0)throw new IllegalArgumentException("Negative insertion");
            if(amount==0||resource.isEmpty())return 0;
            updateSnapshots(tx);
            return amount-handler.insertItem(slot,resource.toStack(amount),false).getCount();
        }
        public int extract(int slot,ItemResource resource,int amount,TransactionContext tx){
            if(amount<0)throw new IllegalArgumentException("Negative extraction");
            if(amount==0||resource.isEmpty()||!resource.matches(handler.getStackInSlot(slot)))return 0;
            updateSnapshots(tx);
            return handler.extractItem(slot,amount,false).getCount();
        }
        protected List<ItemStack> createSnapshot(){
            List<ItemStack> snapshot=new ArrayList<>();
            for(int slot=0;slot<size();slot++)snapshot.add(handler.getStackInSlot(slot).copy());
            return snapshot;
        }
        protected void revertToSnapshot(List<ItemStack> snapshot){for(int slot=0;slot<size();slot++)handler.setStackInSlot(slot,snapshot.get(slot));}
    }
    private static final class Energy extends SnapshotJournal<Map<com.accbdd.complicated_bees.port.EnergyStorage,Integer>> implements EnergyHandler{
        private final IEnergyStorage handler;
        Energy(IEnergyStorage handler){this.handler=handler;}
        public long getAmountAsLong(){return handler.getEnergyStored();}
        public long getCapacityAsLong(){return handler.getMaxEnergyStored();}
        public int insert(int amount,TransactionContext tx){if(amount<0)throw new IllegalArgumentException("Negative energy");updateSnapshots(tx);return handler.receiveEnergy(amount,false);}
        public int extract(int amount,TransactionContext tx){if(amount<0)throw new IllegalArgumentException("Negative energy");updateSnapshots(tx);return handler.extractEnergy(amount,false);}
        protected Map<com.accbdd.complicated_bees.port.EnergyStorage,Integer> createSnapshot(){
            Map<com.accbdd.complicated_bees.port.EnergyStorage,Integer> snapshot=new IdentityHashMap<>();
            snapshot(handler,snapshot);
            return snapshot;
        }
        private static void snapshot(IEnergyStorage storage,Map<com.accbdd.complicated_bees.port.EnergyStorage,Integer> result){
            if(storage instanceof com.accbdd.complicated_bees.port.EnergyStorage concrete)result.put(concrete,concrete.getEnergyStored());
            else if(storage instanceof AdaptedEnergyStorage adapted)snapshot(adapted.getInternalStorage(),result);
            else if(storage instanceof CombinedEnergyStorage combined)for(var part:combined.getInternalStorages())snapshot(part,result);
            else throw new IllegalArgumentException("Unsupported internal energy storage: "+storage.getClass());
        }
        protected void revertToSnapshot(Map<com.accbdd.complicated_bees.port.EnergyStorage,Integer> snapshot){snapshot.forEach(com.accbdd.complicated_bees.port.EnergyStorage::restoreStored);}
    }
}
