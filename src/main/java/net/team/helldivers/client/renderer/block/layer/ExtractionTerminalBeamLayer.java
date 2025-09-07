package net.team.helldivers.client.renderer.block.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import static net.minecraft.client.renderer.blockentity.BeaconRenderer.BEAM_LOCATION;

public class ExtractionTerminalBeamLayer extends GeoRenderLayer<ExtractionTerminalBlockEntity> {

    public ExtractionTerminalBeamLayer(GeoRenderer<ExtractionTerminalBlockEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ExtractionTerminalBlockEntity hellpodEntity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(-0.5, 3.2, -0.5);

        float[] color = new float[]{0.51f, 0.996f, 1.0f, 1.0f};

        BeaconRenderer.renderBeaconBeam(poseStack, bufferSource, BEAM_LOCATION, partialTick, 1,
                Minecraft.getInstance().level.getGameTime(), 0, 999999,
                color, 0.1f, 0.125f);

        poseStack.popPose();
    }
}
