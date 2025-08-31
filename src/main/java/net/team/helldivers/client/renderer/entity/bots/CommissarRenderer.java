package net.team.helldivers.client.renderer.entity.bots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.bots.AutomatonTrooperModel;
import net.team.helldivers.client.model.entity.bots.CommissarModel;
import net.team.helldivers.client.renderer.entity.bots.layer.BotEmissiveLayer;
import net.team.helldivers.entity.custom.bots.AutomatonTrooperEntity;
import net.team.helldivers.entity.custom.bots.CommissarEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CommissarRenderer extends GeoEntityRenderer<CommissarEntity> {
    public CommissarRenderer(EntityRendererProvider.Context context) {
        super(context, new CommissarModel());
        this.addRenderLayer(new BotEmissiveLayer<>(
                this,
                entity -> ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/entity/bots/commissar/commissar_e.png")
        ));
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(CommissarEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

//        poseStack.scale(2f, 2f, 2f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));


        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(CommissarEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/commissar/commissar.png");
    }
}