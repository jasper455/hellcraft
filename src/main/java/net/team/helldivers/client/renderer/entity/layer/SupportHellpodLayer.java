package net.team.helldivers.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import net.team.helldivers.item.ModItems;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;

public class SupportHellpodLayer extends GeoRenderLayer<SupportHellpodEntity> {
    private static final ResourceLocation EMMISSIVE = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/support_hellpod/support_hellpod_e.png");
    private static final ResourceLocation SUPPLY1 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/support_hellpod/support_hellpod_s1.png");
    private static final ResourceLocation SUPPLY2 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/support_hellpod/support_hellpod_s2.png");
    private static final ResourceLocation SUPPLY3 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/support_hellpod/support_hellpod_s3.png");
    private static final ResourceLocation SUPPLY4 = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID,
            "textures/entity/support_hellpod/support_hellpod_s4.png");

    public SupportHellpodLayer(GeoRenderer<SupportHellpodEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, SupportHellpodEntity hellpodEntity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(EMMISSIVE);
        RenderType supply1RenderType = RenderType.armorCutoutNoCull(SUPPLY1);
        RenderType supply2RenderType = RenderType.armorCutoutNoCull(SUPPLY2);
        RenderType supply3RenderType = RenderType.armorCutoutNoCull(SUPPLY3);
        RenderType supply4RenderType = RenderType.armorCutoutNoCull(SUPPLY4);
        poseStack.pushPose();
        getRenderer().reRender(getDefaultBakedModel(hellpodEntity), poseStack, bufferSource, hellpodEntity,
                RenderType.energySwirl(EMMISSIVE, 0, 0), bufferSource.getBuffer(glowRenderType), partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
