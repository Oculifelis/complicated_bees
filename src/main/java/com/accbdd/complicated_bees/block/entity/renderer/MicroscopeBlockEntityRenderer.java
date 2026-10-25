package com.accbdd.complicated_bees.block.entity.renderer;
import com.accbdd.complicated_bees.block.entity.MicroscopeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class MicroscopeBlockEntityRenderer implements BlockEntityRenderer<MicroscopeBlockEntity,MicroscopeBlockEntityRenderer.State> {
    public static class State extends BlockEntityRenderState {
        public final ItemStackRenderState item=new ItemStackRenderState();
        public Direction facing=Direction.SOUTH;
    }
    private final ItemModelResolver resolver;
    public MicroscopeBlockEntityRenderer(BlockEntityRendererProvider.Context context){resolver=context.itemModelResolver();}
    public State createRenderState(){return new State();}
    public void extractRenderState(MicroscopeBlockEntity be,State state,float partialTick,Vec3 camera,ModelFeatureRenderer.CrumblingOverlay breaking){
        BlockEntityRenderer.super.extractRenderState(be,state,partialTick,camera,breaking);
        state.facing=be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        resolver.updateForTopItem(state.item,be.getItems().getStackInSlot(0),ItemDisplayContext.GUI,be.getLevel(),null,1);
    }
    public void submit(State state,PoseStack poseStack,SubmitNodeCollector collector,CameraRenderState camera){
        poseStack.pushPose();
        switch (state.facing) {
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

        state.item.submit(poseStack,collector,state.lightCoords,OverlayTexture.NO_OVERLAY,0);
        poseStack.popPose();
    }
}
