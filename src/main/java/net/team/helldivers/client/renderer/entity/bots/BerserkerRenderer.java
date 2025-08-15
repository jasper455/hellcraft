package net.team.helldivers.client.renderer.entity.bots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.bots.BerserkerModel;
import net.team.helldivers.client.model.entity.bots.RangedHulkModel;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BerserkerRenderer extends GeoEntityRenderer<BerserkerEntity> {
    public BerserkerRenderer(EntityRendererProvider.Context context) {
        super(context, new BerserkerModel());
        this.shadowRadius = 0.75f;
    }

    @Override
    public void render(BerserkerEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

//        poseStack.scale(2f, 2f, 2f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));


        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(BerserkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/berserker/berserker.png");
    }
}