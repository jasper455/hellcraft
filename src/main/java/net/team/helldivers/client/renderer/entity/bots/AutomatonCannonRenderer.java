package net.team.helldivers.client.renderer.entity.bots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.bots.AutomatonCannonModel;
import net.team.helldivers.client.model.entity.bots.BerserkerModel;
import net.team.helldivers.entity.custom.bots.AutomatonCannonEntity;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AutomatonCannonRenderer extends GeoEntityRenderer<AutomatonCannonEntity> {
    public AutomatonCannonRenderer(EntityRendererProvider.Context context) {
        super(context, new AutomatonCannonModel());
        this.shadowRadius = 0.75f;
    }

    @Override
    public void render(AutomatonCannonEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

//        poseStack.scale(2f, 2f, 2f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));


        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(AutomatonCannonEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/cannon/cannon.png");
    }
}