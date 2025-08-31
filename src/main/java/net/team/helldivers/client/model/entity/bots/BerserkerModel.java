package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BerserkerModel extends DefaultedEntityGeoModel<BerserkerEntity> {
    public BerserkerModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "berserker"));
    }

    @Override
    public ResourceLocation getModelResource(BerserkerEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/berserker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BerserkerEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/berserker/berserker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BerserkerEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/berserker.animation.json");
    }
}
