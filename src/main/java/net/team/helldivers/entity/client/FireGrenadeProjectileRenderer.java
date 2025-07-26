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
import net.team.helldivers.entity.custom.FireGrenadeEntity;
import net.team.helldivers.entity.custom.FragGrenadeEntity;

public class FireGrenadeProjectileRenderer extends EntityRenderer<FireGrenadeEntity> {
    private FireGrenadeProjectileModel model;
    public FireGrenadeProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new FireGrenadeProjectileModel(pContext.bakeLayer(ModModelLayers.FIRE_GRENADE));
    }

    @Override
    public void render(FireGrenadeEntity pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!pEntity.isGrounded()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, pEntity.yRotO, pEntity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getRenderingRotation() * 5f));
        }
        poseStack.pushPose();
        poseStack.translate(0, 1, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);

        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();

        super.render(pEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FireGrenadeEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/fire_grenade/fire_grenade.png");
    }

    @Override
    public boolean shouldRender(FireGrenadeEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
