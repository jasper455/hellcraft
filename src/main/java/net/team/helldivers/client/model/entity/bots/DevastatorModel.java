package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.DevastatorEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DevastatorModel extends DefaultedEntityGeoModel<DevastatorEntity> {
    public DevastatorModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "devastator"));
    }

    @Override
    public ResourceLocation getModelResource(DevastatorEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/devastator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DevastatorEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/devastator/devastator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DevastatorEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/devastator.animation.json");
    }
}
