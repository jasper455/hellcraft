package net.team.helldivers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.team.helldivers.client.model.entity.SupportHellpodModel;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SupportHellpodRenderer extends GeoEntityRenderer<SupportHellpodEntity> {
    public SupportHellpodRenderer(EntityRendererProvider.Context context) {
        super(context, new SupportHellpodModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SupportHellpodEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("helldivers", "textures/entity/support_hellpod/support_hellpod.png");
    }

    @Override
    public boolean shouldRender(SupportHellpodEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}