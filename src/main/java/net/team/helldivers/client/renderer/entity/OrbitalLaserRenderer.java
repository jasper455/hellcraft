package net.team.helldivers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.client.model.entity.EagleAirshipModel;
import net.team.helldivers.client.model.entity.OrbitalLaserModel;
import net.team.helldivers.entity.custom.EagleAirshipEntity;
import net.team.helldivers.entity.custom.OrbitalLaserEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static net.minecraft.client.renderer.blockentity.BeaconRenderer.BEAM_LOCATION;

public class OrbitalLaserRenderer extends GeoEntityRenderer<OrbitalLaserEntity> {
    public OrbitalLaserRenderer(EntityRendererProvider.Context context) {
        super(context, new OrbitalLaserModel());
    }

    @Override
    public ResourceLocation getTextureLocation(OrbitalLaserEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("helldivers", "textures/entity/orbital_laser/orbital_laser.png");
    }

    @Override
    public boolean shouldRender(OrbitalLaserEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(OrbitalLaserEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);

        float[] color = new float[]{1f, 0.671f, 0f, 1f};

        BeaconRenderer.renderBeaconBeam(poseStack, bufferSource, BEAM_LOCATION, partialTick, 1,
                Minecraft.getInstance().level.getGameTime(), 0, 999999,
                color, 0.5f, 1.525f);

        poseStack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(OrbitalLaserEntity animatable) {
        return 0.0f;
    }
}
