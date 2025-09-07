package net.team.helldivers.client.renderer.entity.bots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.bots.BerserkerModel;
import net.team.helldivers.client.model.entity.bots.DevastatorModel;
import net.team.helldivers.client.renderer.entity.bots.layer.BotEmissiveLayer;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import net.team.helldivers.entity.custom.bots.DevastatorEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DevastatorRenderer extends GeoEntityRenderer<DevastatorEntity> {
    public DevastatorRenderer(EntityRendererProvider.Context context) {
        super(context, new DevastatorModel());
        this.addRenderLayer(new BotEmissiveLayer<>(
                this,
                entity -> ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/entity/bots/devastator/devastator_e.png")
        ));
        this.shadowRadius = 0.75f;
    }

    @Override
    public void render(DevastatorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

//        poseStack.scale(2f, 2f, 2f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));


        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(DevastatorEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/devastator/devastator.png");
    }
}