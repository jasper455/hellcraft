package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RangedHulkModel extends DefaultedEntityGeoModel<RangedHulkEntity> {
    public RangedHulkModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "hulk"));
    }

    @Override
    public ResourceLocation getModelResource(RangedHulkEntity orbitalLaserEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/hulk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RangedHulkEntity orbitalLaserEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/hulk/hulk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RangedHulkEntity orbitalLaserEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/hulk.animation.json");
    }
}
