package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.BrawlerEntity;
import net.team.helldivers.entity.custom.bots.CommissarEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BrawlerModel extends DefaultedEntityGeoModel<BrawlerEntity> {
    public BrawlerModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "brawler"));
    }

    @Override
    public ResourceLocation getModelResource(BrawlerEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/brawler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BrawlerEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/brawler/brawler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BrawlerEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/brawler.animation.json");
    }
}
