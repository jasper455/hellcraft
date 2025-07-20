package net.team.helldivers.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;

public class HellbombHellpodLayer extends GeoRenderLayer<HellbombHellpodEntity> {
    private static final ResourceLocation EMMISSIVE = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/hellbomb_hellpod/hellbomb_hellpod_e.png");

    public HellbombHellpodLayer(GeoRenderer<HellbombHellpodEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, HellbombHellpodEntity hellpodEntity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(EMMISSIVE);
        poseStack.pushPose();
        getRenderer().reRender(getDefaultBakedModel(hellpodEntity), poseStack, bufferSource, hellpodEntity,
                LodestoneRenderTypeRegistry.TRANSPARENT_SOLID, bufferSource.getBuffer(glowRenderType), partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
