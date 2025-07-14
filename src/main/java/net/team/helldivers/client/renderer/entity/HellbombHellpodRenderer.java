package net.team.helldivers.client.renderer.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.client.model.entity.HellbombHellpodModel;
import net.team.helldivers.client.model.entity.SupportHellpodModel;
import net.team.helldivers.client.renderer.entity.layer.SupportHellpodLayer;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HellbombHellpodRenderer extends GeoEntityRenderer<HellbombHellpodEntity> {
    public HellbombHellpodRenderer(EntityRendererProvider.Context context) {
        super(context, new HellbombHellpodModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(HellbombHellpodEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("helldivers", "textures/entity/hellbomb_hellpod/hellbomb_hellpod.png");
    }

    @Override
    public boolean shouldRender(HellbombHellpodEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}