package net.team.helldivers.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.HellpodEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;

public class HellpodLayer extends GeoRenderLayer<HellpodEntity> {
    private static final ResourceLocation EMMISSIVE = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/hellpod/hellpod_e.png");

    public HellpodLayer(GeoRenderer<HellpodEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, HellpodEntity hellpodEntity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.energySwirl(EMMISSIVE, 0, 0);
        poseStack.pushPose();
        getRenderer().reRender(getDefaultBakedModel(hellpodEntity), poseStack, bufferSource, hellpodEntity,
                RenderType.energySwirl(EMMISSIVE, 0, 0), bufferSource.getBuffer(glowRenderType), partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
