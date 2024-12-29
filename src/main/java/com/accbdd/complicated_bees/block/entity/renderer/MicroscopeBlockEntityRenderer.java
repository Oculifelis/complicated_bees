package com.accbdd.complicated_bees.block.entity.renderer;

import com.accbdd.complicated_bees.block.entity.MicroscopeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MicroscopeBlockEntityRenderer implements BlockEntityRenderer<MicroscopeBlockEntity> {
    public MicroscopeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MicroscopeBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack stack = be.getItem(0);
        poseStack.pushPose();
        switch (be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST -> {
                poseStack.translate(0.66f, 0.27f, 0.5f);
                poseStack.scale(0.15f, 0.15f, 0.15f);
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            }
            case WEST -> {
                poseStack.translate(0.34f, 0.27f, 0.5f);
                poseStack.scale(0.15f, 0.15f, 0.15f);
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
                poseStack.mulPose(Axis.ZP.rotationDegrees(270));
            }
            case NORTH -> {
                poseStack.translate(0.5f, 0.27f, 0.34f);
                poseStack.scale(0.15f, 0.15f, 0.15f);
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            case SOUTH -> {
                poseStack.translate(0.5f, 0.27f, 0.66f);
                poseStack.scale(0.15f, 0.15f, 0.15f);
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
            }
        }

        itemRenderer.renderStatic(stack, ItemDisplayContext.GUI, getLightLevel(be.getLevel(), be.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, be.getLevel(), 1);
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);

        return LightTexture.pack(bLight, sLight);
    }
}
