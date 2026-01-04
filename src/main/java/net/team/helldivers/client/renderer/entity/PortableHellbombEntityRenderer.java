package net.team.helldivers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.model.entity.HellpodModel;
import net.team.helldivers.client.model.entity.PortableHellbombEntityModel;
import net.team.helldivers.client.renderer.entity.layer.HellpodLayer;
import net.team.helldivers.entity.custom.HellpodEntity;
import net.team.helldivers.entity.custom.PortableHellbombEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static net.minecraft.client.renderer.blockentity.BeaconRenderer.BEAM_LOCATION;

public class PortableHellbombEntityRenderer extends GeoEntityRenderer<PortableHellbombEntity> {
    public PortableHellbombEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new PortableHellbombEntityModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(PortableHellbombEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, -0.25, 0.4);

        if (!entity.isGrounded()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.getRenderingRotation() * 5f));
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(PortableHellbombEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/portable_hellbomb_entity/portable_hellbomb_entity.png");
    }

    @Override
    public boolean shouldRender(PortableHellbombEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}