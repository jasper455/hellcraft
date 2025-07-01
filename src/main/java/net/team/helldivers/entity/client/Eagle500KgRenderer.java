package net.team.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.Eagle500KgEntity;
import net.team.helldivers.entity.custom.MissileProjectileEntity;

public class Eagle500KgRenderer extends EntityRenderer<Eagle500KgEntity> {
    private Eagle500KgModel model;
    public Eagle500KgRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new Eagle500KgModel(pContext.bakeLayer(ModModelLayers.MISSILE));
    }

    @Override
    public ResourceLocation getTextureLocation(Eagle500KgEntity missile) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/missile/missile.png");
    }

    @Override
    public void render(Eagle500KgEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(-45f));

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(entity)), false, false);
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(Eagle500KgEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}