package net.team.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.network.chat.Component;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.StratagemOrbEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Arrays;

import static net.minecraft.client.renderer.blockentity.BeaconRenderer.BEAM_LOCATION;

public class StratagemOrbProjectileRenderer extends EntityRenderer<StratagemOrbEntity> {
    private StratagemOrbProjectileModel model;
    public StratagemOrbProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new StratagemOrbProjectileModel(pContext.bakeLayer(ModModelLayers.STRATAGEM_ORB));
    }

    @Override
    public void render(StratagemOrbEntity pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (pEntity.isGrounded()) {
            poseStack.pushPose();
            poseStack.translate(-0.5, 0, -0.5);

            float[] color = getBeamColor(pEntity.getStratagemType());

            BeaconRenderer.renderBeaconBeam(poseStack, buffer, BEAM_LOCATION, partialTicks, 1,
                    Minecraft.getInstance().level.getGameTime(), 0, 999999,
                    color, 0.1f, 0.125f);

            poseStack.popPose();
        } else {
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

    private float[] getBeamColor(String stratagemType) {
        return switch (stratagemType) {
            // Other
            case "Hellbomb" -> new float[]{0.51f, 0.996f, 1.0f, 1.0f}; // Support blue color
            // Support
            case "Resupply" -> new float[]{0.51f, 0.996f, 1.0f, 1.0f}; // Support blue color
            case "Expendable Anti-Tank" -> new float[]{0.51f, 0.996f, 1.0f, 1.0f}; // Support blue color
            // Orbital
            case "Orbital Precision Strike" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            case "Orbital 120MM HE Barrage" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            case "Orbital 380MM HE Barrage" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            case "Orbital Laser" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            case "Orbital Napalm Barrage" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            case "Orbital Walking Barrage" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            //Eagle
            case "Eagle 500KG Bomb" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            case "Eagle Cluster Bomb" -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
            default -> new float[]{0.922f, 0.251f, 0.204f, 1.0f}; // Default red color
        };
    }

    private boolean isBlueBeam(String stratagemType) {
        return Arrays.equals(getBeamColor(stratagemType), new float[]{0.51f, 0.996f, 1.0f, 1.0f});
    }


    @Override
    public ResourceLocation getTextureLocation(StratagemOrbEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, isBlueBeam(pEntity.getStratagemType()) ?
                "textures/entity/stratagem_orb/stratagem_support_orb_3d.png" : "textures/entity/stratagem_orb/stratagem_offense_orb_3d.png");
    }

    @Override
    public boolean shouldRender(StratagemOrbEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
