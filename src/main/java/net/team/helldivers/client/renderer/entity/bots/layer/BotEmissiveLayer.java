package net.team.helldivers.client.renderer.entity.bots.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.renderer.ModRenderTypes;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.bots.AbstractBotEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;

import java.util.function.Function;

public class BotEmissiveLayer<T extends AbstractBotEntity> extends GeoRenderLayer<T> {
    private final Function<T, ResourceLocation> emissiveLayerProvider;

    public BotEmissiveLayer(GeoRenderer<T> entityRendererIn, Function<T, ResourceLocation> emissiveLayerProvider) {
        super(entityRendererIn);
        this.emissiveLayerProvider = emissiveLayerProvider;
    }

    @Override
    public void render(PoseStack poseStack, T botEntity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ResourceLocation emissiveLocation = emissiveLayerProvider.apply(botEntity);
        RenderType glowRenderType = RenderType.eyes(emissiveLocation);
        poseStack.pushPose();
        getRenderer().reRender(getDefaultBakedModel(botEntity), poseStack, bufferSource, botEntity,
                RenderType.energySwirl(emissiveLocation, 0, 0), bufferSource.getBuffer(glowRenderType), partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
