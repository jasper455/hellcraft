package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.guns.FLAM40Item;
import software.bernie.geckolib.model.GeoModel;

public class FLAM40Model extends GeoModel<FLAM40Item> {
    @Override
    public ResourceLocation getModelResource(FLAM40Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/flam40.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FLAM40Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/flam40.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FLAM40Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/flam40.animation.json");
    }
}
