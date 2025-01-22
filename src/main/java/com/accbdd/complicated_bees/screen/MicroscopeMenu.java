package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.MicroscopeBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import com.accbdd.complicated_bees.genetics.tracking.ServerBreedingTracker;
import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.MicroscopeGamePacketClientbound;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class MicroscopeMenu extends AbstractContainerMenu {
    public static int SLOT_COUNT = 6;
    public static Random rand = new Random();

    private final BlockPos pos;
    private final Player player;
    private final byte[] researchCode;
    private final byte difficulty;
    protected List<Mutation> possibleMutations = List.of();
    protected int possibleMutationsCount = -1;
    protected List<Mutation> researchedMutations = List.of();
    protected  int researchedMutationsCount = -1;

    public MicroscopeMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.MICROSCOPE_MENU.get(), windowId);
        this.pos = pos;
        this.player = player;
        //todo: actually set this based on the bee
        this.difficulty = 3;
        this.researchCode = new byte[difficulty];
        for (byte i = 0; i < difficulty; i++) {
            researchCode[i] = i;
        }
        if (player.level().getBlockEntity(pos) instanceof MicroscopeBlockEntity microscope) {
            addSlot(new TagSlot(microscope.getItems(), 0, 225, 8, ItemTagGenerator.BEE) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    if (getItem().isEmpty())
                        clearGame();
                    queryTracker();
                }
            });
            for (int i = 0; i < 5; i++) {
                addSlot(new TagSlot(microscope.getItems(), i+1, 225, 40 + i * 18, ItemTagGenerator.BEE));
            }
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    if (possibleMutationsCount == -1) {
                        queryTracker();
                    }
                    return possibleMutationsCount;
                }

                @Override
                public void set(int pValue) {
                    possibleMutationsCount = pValue;
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    if (researchedMutationsCount == -1) {
                        queryTracker();
                    }
                    return researchedMutationsCount;
                }

                @Override
                public void set(int pValue) {
                    researchedMutationsCount = pValue;
                }
            });
            microscope.setLocked(true);
            layoutPlayerInventorySlots(player.getInventory(), 36, 134);
            shuffle();
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (pPlayer.level().getBlockEntity(pos) instanceof MicroscopeBlockEntity microscope) {
            microscope.setLocked(false);
        }
    }

    protected void startGame(ItemStack item) {
        shuffle();
        queryTracker();
        if (player instanceof ServerPlayer serverPlayer)
            if (item.isEmpty() || possibleMutationsCount == researchedMutationsCount)
                clearGame();
            else if (researchedMutations.size() < possibleMutations.size())
                PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MicroscopeGamePacketClientbound(MicroscopeGamePacketClientbound.GameState.START));
    }

    private void clearGame() {
        if (player instanceof ServerPlayer serverPlayer)
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MicroscopeGamePacketClientbound(MicroscopeGamePacketClientbound.GameState.CLEAR));
    }

    public byte[] getResearchCode() {
        return researchCode;
    }

    public BlockPos getPos() {
        return pos;
    }

    public byte getDifficulty() {
        return difficulty;
    }

    protected void queryTracker() {
        ServerBreedingTracker tracker = ServerBreedingTracker.getTracker(this.player);
        if (tracker == null)
            return;
        ItemStack bee = getSlot(0).getItem();
        if (bee.isEmpty()) {
            possibleMutations = List.of();
            possibleMutationsCount = -1;
            researchedMutations = List.of();
            researchedMutationsCount = -1;
            return;
        }
        ResourceLocation species = ResourceLocation.tryParse(bee.getTag().getString(GeneticHelper.SPECIES));
        Registry<Mutation> mutationRegistry = GeneticHelper.getRegistryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get();
        List<Mutation> mutations = mutationRegistry.stream().filter(
                mutation -> (mutation.getFirst().equals(species) || mutation.getSecond().equals(species))
        ).toList();
        List<Mutation> researched = tracker.getResearchedMutations().stream().filter(
                location -> mutationRegistry.get(location).getFirst().equals(species) || mutationRegistry.get(location).getSecond().equals(species)
        ).map(mutationRegistry::get).toList();

        possibleMutations = mutations;
        possibleMutationsCount = mutations.size();
        researchedMutations = researched;
        researchedMutationsCount = researched.size();
    }

    public void research() {
        ItemStack bee = getSlot(0).getItem();
        if (bee.isEmpty())
            return;
        ServerBreedingTracker tracker = ServerBreedingTracker.getTracker(this.player);
        ResourceLocation species = ResourceLocation.tryParse(bee.getTag().getString(GeneticHelper.SPECIES));
        Registry<Mutation> mutationRegistry = GeneticHelper.getRegistryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get();
        Set<ResourceLocation> researched = tracker.getResearchedMutations().stream().filter(
                location -> mutationRegistry.get(location).getFirst().equals(species) || mutationRegistry.get(location).getSecond().equals(species)
        ).collect(Collectors.toSet());
        List<Mutation> mutationsToDiscover = mutationRegistry.stream().filter(
                mutation -> ((mutation.getFirst().equals(species) || mutation.getSecond().equals(species) && !researched.contains(mutationRegistry.getKey(mutation))))
        ).toList();

        if (!mutationsToDiscover.isEmpty()) {
            tracker.research(mutationsToDiscover.get(rand.nextInt(mutationsToDiscover.size())));
            queryTracker();
        }
    }

    public void shuffle() {
        Random rnd = new Random();
        for (int i = researchCode.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i+1);
            int a = researchCode[index];
            researchCode[index] = researchCode[i];
            researchCode[i] = (byte)a;
        }
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private void addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
    }

    private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                } else {
                    researchedMutations = List.of();
                    possibleMutations = List.of();
                }
            }
            if (!this.moveItemStackTo(stack, 0, 2, false)) {
                if (index < 27 + SLOT_COUNT) {
                    if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + SLOT_COUNT && !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, BlocksRegistration.MICROSCOPE.get());
    }
}
