package net.infinite1274.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.entity.custom.StratagemOrbEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.renderer.blockentity.BeaconRenderer.BEAM_LOCATION;

public class StratagemOrbProjectileRenderer extends EntityRenderer<StratagemOrbEntity> {
    private StratagemOrbProjectileModel model;
    public StratagemOrbProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new StratagemOrbProjectileModel(pContext.bakeLayer(ModModelLayers.STRATAGEM_ORB));
    }

    @Override
    public void render(StratagemOrbEntity pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        if (pEntity.isGrounded()) {


            poseStack.pushPose();


            poseStack.translate(-0.5, 0, -0.5);

            float[] color = new float[] {235, 64, 52};

            BeaconRenderer.renderBeaconBeam(poseStack, buffer, BEAM_LOCATION, partialTicks, 1,
                    Minecraft.getInstance().level.getGameTime(), 0, 999999,
                    color, 0.2f, 0.25f);

            poseStack.popPose();
        }

        poseStack.translate(0, -1.2, 0);

        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);

        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();

        super.render(pEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(StratagemOrbEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/stratagem_orb/stratagem_orb.png");
    }

    @Override
    public boolean shouldRender(StratagemOrbEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
