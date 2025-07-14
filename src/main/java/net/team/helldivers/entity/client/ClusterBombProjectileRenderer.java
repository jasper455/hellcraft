package net.team.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.entity.custom.ClusterBombProjectileEntity;

public class ClusterBombProjectileRenderer extends EntityRenderer<ClusterBombProjectileEntity> {
    private ClusterBombProjectileModel model;

    public ClusterBombProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new ClusterBombProjectileModel(pContext.bakeLayer(ModModelLayers.CLUSTER_BOMB));
    }


    @Override
    public void render(ClusterBombProjectileEntity pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(pEntity)),false, false);
        poseStack.translate(0, -1.2, 0);
        poseStack.scale(5f, 2.75f, 5f);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
        super.render(pEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ClusterBombProjectileEntity bulletProjectileEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/rocket/eagle_rocket.png");
    }

}