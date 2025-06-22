package net.infinite1274.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.entity.custom.MissileProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class MissileProjectileRenderer extends EntityRenderer<MissileProjectileEntity> {
    private MissileProjectileModel model;
    public MissileProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new MissileProjectileModel(pContext.bakeLayer(ModModelLayers.MISSILE));
    }

    @Override
    public ResourceLocation getTextureLocation(MissileProjectileEntity ratEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/missile/missile.png");
    }

    @Override
    public void render(MissileProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Interpolated current and previous positions
        double px = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double py = Mth.lerp(partialTicks, entity.yOld, entity.getY());
        double pz = Mth.lerp(partialTicks, entity.zOld, entity.getZ());

        double lx = entity.xOld;
        double ly = entity.yOld;
        double lz = entity.zOld;

        // Velocity direction = current - last
        double dx = px - lx;
        double dy = py - ly;
        double dz = pz - lz;

        // Compute yaw and pitch from direction vector
        float yaw = (float)(Math.toDegrees(Math.atan2(dx, dz)));
        float pitch = (float)(Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));

        // Apply rotations
        poseStack.mulPose(Axis.XP.rotationDegrees(yaw));    // Horizontal facing
        poseStack.mulPose(Axis.YP.rotationDegrees(pitch));   // Tilting down/up
        poseStack.mulPose(Axis.ZP.rotationDegrees(-90));     // Align with model front

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(entity)), false, false);
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(MissileProjectileEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}