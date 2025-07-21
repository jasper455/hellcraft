package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.OrbitalLaserEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class OrbitalLaserModel extends DefaultedEntityGeoModel<OrbitalLaserEntity> {
    public OrbitalLaserModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "orbital_laser"));
    }

    @Override
    public ResourceLocation getModelResource(OrbitalLaserEntity orbitalLaserEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/orbital_laser.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OrbitalLaserEntity orbitalLaserEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/orbital_laser/orbital_laser.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OrbitalLaserEntity orbitalLaserEntity) {
        return null;
    }
}
