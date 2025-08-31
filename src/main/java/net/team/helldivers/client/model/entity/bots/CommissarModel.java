package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.AutomatonTrooperEntity;
import net.team.helldivers.entity.custom.bots.CommissarEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CommissarModel extends DefaultedEntityGeoModel<CommissarEntity> {
    public CommissarModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "commissar"));
    }

    @Override
    public ResourceLocation getModelResource(CommissarEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/commissar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CommissarEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/commissar/commissar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CommissarEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/commissar.animation.json");
    }
}
