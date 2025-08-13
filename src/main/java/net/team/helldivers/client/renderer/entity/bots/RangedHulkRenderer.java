package net.team.helldivers.client.renderer.entity.bots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.HellbombHellpodModel;
import net.team.helldivers.client.model.entity.bots.RangedHulkModel;
import net.team.helldivers.client.renderer.entity.layer.HellbombHellpodLayer;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RangedHulkRenderer extends GeoEntityRenderer<RangedHulkEntity> {
    public RangedHulkRenderer(EntityRendererProvider.Context context) {
        super(context, new RangedHulkModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public void render(RangedHulkEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.scale(2f, 2f, 2f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));


        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(RangedHulkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/hulk/hulk.png");
    }
}