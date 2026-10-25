package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.entity.BeeStaffProjectile;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BeeStaffItem extends DisableableItem {
    public BeeStaffItem(Properties pProperties, ModConfigSpec.ConfigValue<Boolean> configValue) {
        super(pProperties.repairable(ItemsRegistration.ROYAL_JELLY.get()).rarity(Rarity.UNCOMMON).durability(100), configValue);
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Vec3 vector = pPlayer.getLookAngle().scale(0.1f);
        Vec3 position = pPlayer.position();
        BeeStaffProjectile projectile = new BeeStaffProjectile(pLevel, pPlayer, vector.x, vector.y, vector.z);
        projectile.snapTo(position.x, position.y + 1.5f, position.z);
        pLevel.addFreshEntity(projectile);
        pPlayer.getItemInHand(pUsedHand).hurtAndBreak(1, pPlayer, pUsedHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
        pPlayer.playSound(SoundEvents.BEE_HURT);
        return super.use(pLevel, pPlayer, pUsedHand);
    }


}
