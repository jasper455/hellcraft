package net.team.helldivers.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SupportHellpodLayer extends GeoRenderLayer<SupportHellpodEntity> {
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/support_hellpod/support_hellpod_e.png");

    public SupportHellpodLayer(GeoRenderer<SupportHellpodEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, SupportHellpodEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable,
                glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
