package net.team.helldivers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.GatlingSentryHellpodModel;
import net.team.helldivers.client.model.entity.HellbombHellpodModel;
import net.team.helldivers.client.renderer.entity.layer.HellbombHellpodLayer;
import net.team.helldivers.entity.custom.GatlingSentryHellpodEntity;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GatlingSentryHellpodRenderer extends GeoEntityRenderer<GatlingSentryHellpodEntity> {
    public GatlingSentryHellpodRenderer(EntityRendererProvider.Context context) {
        super(context, new GatlingSentryHellpodModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(GatlingSentryHellpodEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(GatlingSentryHellpodEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/gatling_sentry_hellpod/gatling_sentry_hellpod.png");
    }

    @Override
    public boolean shouldRender(GatlingSentryHellpodEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}