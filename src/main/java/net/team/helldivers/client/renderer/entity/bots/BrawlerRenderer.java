package net.team.helldivers.client.renderer.entity.bots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.bots.BrawlerModel;
import net.team.helldivers.client.model.entity.bots.CommissarModel;
import net.team.helldivers.entity.custom.bots.BrawlerEntity;
import net.team.helldivers.entity.custom.bots.CommissarEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrawlerRenderer extends GeoEntityRenderer<BrawlerEntity> {
    public BrawlerRenderer(EntityRendererProvider.Context context) {
        super(context, new BrawlerModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(BrawlerEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

//        poseStack.scale(2f, 2f, 2f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));


        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(BrawlerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/brawler/brawler.png");
    }
}