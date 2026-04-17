package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.block.entity.BeeNestBlockEntity;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeNestBlock extends BaseEntityBlock {
    private static final MapCodec<BeeNestBlock> CODEC = simpleCodec(props -> new BeeNestBlock());

    public BeeNestBlock() {
        super(BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .lightLevel(state -> 15)
                .strength(0.6f)
                .sound(SoundType.WOOD));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static ItemStack stackNest(ItemStack stack, Species species) {
        stack.update(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY, data -> data.update(tag -> tag.putString("species", SpeciesRegistration.getResourceLocation(species).toString())));
        return stack;
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 1) {
            Species species = SpeciesRegistration.getFromResourceLocation(ResourceLocation.tryParse(stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).getUnsafe().getString("species")));
            if (species != null) {
                return species.getNestColor();
            }
            return 0;
        }
        return 0xFFFFFF;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pPlayer.getMainHandItem().is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "scoop_tool"))) && pPlayer.canBeSeenAsEnemy()) {
            pPlayer.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BeeNestBlockEntity(pPos, pState);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack nest = new ItemStack(ItemsRegistration.BEE_NEST.get());
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BeeNestBlockEntity ne)
            nest.update(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY, data -> data.update(tag -> tag.putString("species", SpeciesRegistration.getResourceLocation(ne.getSpecies()).toString())));
        return nest;
    }
}
