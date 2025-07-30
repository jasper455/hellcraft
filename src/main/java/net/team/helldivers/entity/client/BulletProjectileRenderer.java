package net.team.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.renderer.ModRenderTypes;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.lodestone.systems.rendering.VFXBuilders;

public class BulletProjectileRenderer extends EntityRenderer<BulletProjectileEntity> {
    private BulletProjectileModel model;

    public BulletProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new BulletProjectileModel(pContext.bakeLayer(ModModelLayers.BULLET));
    }


    @Override
    public void render(BulletProjectileEntity pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(pEntity)),false, false);
        poseStack.translate(0, -1.2, 0);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(pEntity.tickCount * 10));

        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.WorldVFXBuilder.createWorld();
        builder.setColor(1.0f, 1.0f, 1.0f)
                .setAlpha(1f)
                .setUV(0f, 0f, 1f, 1f) // important!
                .setLight(0xF000F0)
                .replaceBufferSource(buffer)
                .setRenderType(ModRenderTypes.CUSTOM_SKY)
//                .renderCylinder(vertexconsumer, poseStack, -10, 50, 1)
                .renderSphere(vertexconsumer, poseStack, -50, 50, 50);
        poseStack.popPose();
        super.render(pEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BulletProjectileEntity bulletProjectileEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bullet/bullet.png");
    }

}