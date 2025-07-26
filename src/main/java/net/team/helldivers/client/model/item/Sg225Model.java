package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.guns.Sg225Item;
import software.bernie.geckolib.model.GeoModel;

public class Sg225Model extends GeoModel<Sg225Item> {
    @Override
    public ResourceLocation getModelResource(Sg225Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/sg225.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Sg225Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/sg225.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Sg225Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/sg225.animation.json");
    }
}
