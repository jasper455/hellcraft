package net.team.helldivers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.SupportHellpodModel;
import net.team.helldivers.client.renderer.entity.layer.SupportHellpodLayer;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SupportHellpodRenderer extends GeoEntityRenderer<SupportHellpodEntity> {
    public SupportHellpodRenderer(EntityRendererProvider.Context context) {
        super(context, new SupportHellpodModel());
        this.shadowRadius = 0.5f;
        this.addRenderLayer(new SupportHellpodLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SupportHellpodEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/support_hellpod/support_hellpod.png");
    }

    @Override
    public void render(SupportHellpodEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        // Rotate the model 180 degrees around the Y axis
        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(SupportHellpodEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}