package net.team.helldivers.client.renderer.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.client.model.entity.EagleAirshipModel;
import net.team.helldivers.entity.custom.EagleAirshipEntity;
import net.team.helldivers.entity.custom.OrbitalLaserEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class EagleAirshipRenderer extends GeoEntityRenderer<EagleAirshipEntity> {
    public EagleAirshipRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleAirshipModel());
    }

    @Override
    public ResourceLocation getTextureLocation(EagleAirshipEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("helldivers", "textures/entity/eagle_airship/eagle_airship.png");
    }

    @Override
    public boolean shouldRender(EagleAirshipEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    protected float getDeathMaxRotation(EagleAirshipEntity animatable) {
        return 0.0f;
    }

}
