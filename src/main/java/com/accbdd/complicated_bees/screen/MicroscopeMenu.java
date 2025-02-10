package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.MicroscopeBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import com.accbdd.complicated_bees.genetics.tracking.BreedingTracker;
import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.MicroscopeGameClientbound;
import com.accbdd.complicated_bees.network.packet.MicroscopeHintClientbound;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

public class MicroscopeMenu extends AbstractBaseInventoryMenu {
    public static int SLOT_COUNT = 6;
    public static Random rand = new Random();
    private static int INV_X = 36;
    private static int INV_Y = 134;

    private final BlockPos pos;
    private final Player player;
    private byte[] researchCode;
    private byte[] guessCode;
    private MicroscopeGameClientbound.GameState state;
    protected int difficulty;
    protected List<Mutation> possibleMutations = List.of();
    protected int possibleMutationsCount = -1;
    protected List<Mutation> researchedMutations = List.of();
    protected  int researchedMutationsCount = -1;

    public MicroscopeMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.MICROSCOPE_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.pos = pos;
        this.player = player;
        this.state = MicroscopeGameClientbound.GameState.CLEAR;
        if (player.level().getBlockEntity(pos) instanceof MicroscopeBlockEntity microscope) {
            addSlot(new TagSlot(microscope.getItems(), 0, 225, 8, ItemTagGenerator.BEE) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    clearGame();
                    setDifficulty();
                    queryTracker();
                }
            });
            for (int i = 0; i < 5; i++) {
                addSlot(new TagSlot(microscope.getItems(), i + 1, 225, 40 + i * 18, ItemTagGenerator.RESEARCH_MATERIAL) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        if (difficulty < this.getSlotIndex() + 1 || getState() != MicroscopeGameClientbound.GameState.ONGOING)
                            return false;
                        return super.mayPlace(stack);
                    }
                });
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
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return difficulty;
                }

                @Override
                public void set(int pValue) {
                    difficulty = pValue;
                }
            });
            microscope.setLocked(true);
            layoutPlayerInventorySlots(player.getInventory());
            setDifficulty();
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (pPlayer.level().getBlockEntity(pos) instanceof MicroscopeBlockEntity microscope) {
            microscope.setLocked(false);
        }
    }

    public void setGuess(byte[] guess) {
        this.guessCode = guess.clone();
    }

    public void trySendHint() {
        if (player instanceof ServerPlayer serverPlayer && !player.level().isClientSide && state != MicroscopeGameClientbound.GameState.WON && state != MicroscopeGameClientbound.GameState.FAILED) {
            for (int i = 1; i < Math.min(difficulty, 6); i++) {
                if (!getSlot(i).hasItem())
                    return;
            }
            for (int i = 1; i < Math.min(difficulty, 6); i++) {
                getSlot(i).getItem().shrink(1);
            }
            List<Integer> unguessed = new ArrayList<>();
            for (int i = 0; i < guessCode.length; i++) {
                if (guessCode[i] == -1) {
                    unguessed.add(i);
                }
            }
            byte index = unguessed.get(rand.nextInt(unguessed.size())).byteValue();
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MicroscopeHintClientbound(index, researchCode[index]));
        }
    }

    private void setDifficulty() {
        if (getSlot(0).getItem().isEmpty())
            this.difficulty = 1;
        else
            this.difficulty = (int) (5 * Math.log10(SpeciesRegistration.getComplexity(GeneticHelper.getSpecies(getSlot(0).getItem(), true))) + 3);
        this.difficulty = Math.min(difficulty, 8);
        this.researchCode = new byte[difficulty];
        for (byte i = 0; i < difficulty; i++) {
            researchCode[i] = i;
        }
        shuffle();
    }

    private void clearGame() {
        if (player instanceof ServerPlayer serverPlayer)
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.CLEAR));
        this.setState(MicroscopeGameClientbound.GameState.CLEAR);
    }

    public byte[] getResearchCode() {
        return researchCode;
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getDifficulty() {
        return difficulty;
    }

    protected void queryTracker() {
        BreedingTracker tracker = BreedingTracker.getTracker(this.player);
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
        BreedingTracker tracker = BreedingTracker.getTracker(this.player);
        ResourceLocation species = ResourceLocation.tryParse(bee.getTag().getString(GeneticHelper.SPECIES));
        Registry<Mutation> mutationRegistry = GeneticHelper.getRegistryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get();
        Set<ResourceLocation> researched = tracker.getResearchedMutations().stream().filter(
                location -> mutationRegistry.get(location).getFirst().equals(species) || mutationRegistry.get(location).getSecond().equals(species)
        ).collect(Collectors.toSet());
        List<Mutation> mutationsToDiscover = mutationRegistry.stream().filter(
                mutation -> (!researched.contains(mutationRegistry.getKey(mutation)) && ((mutation.getFirst().equals(species) || mutation.getSecond().equals(species))))
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
        guessCode = new byte[difficulty];
        Arrays.fill(guessCode, (byte) -1);
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
            if (!this.moveItemStackTo(stack, 0, SLOT_COUNT, false)) {
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

    public MicroscopeGameClientbound.GameState getState() {
        return state;
    }

    public void setState(MicroscopeGameClientbound.GameState state) {
        this.state = state;
    }
}
