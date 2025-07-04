package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.P2Item;
import net.team.helldivers.item.custom.Plas1Item;
import software.bernie.geckolib.model.GeoModel;

public class Plas1Model extends GeoModel<Plas1Item> {
    @Override
    public ResourceLocation getModelResource(Plas1Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/plas1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Plas1Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/plas1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Plas1Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/plas1.animation.json");
    }
}
