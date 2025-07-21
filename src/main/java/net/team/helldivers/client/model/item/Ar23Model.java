package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.guns.Ar23Item;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.model.GeoModel;

public class Ar23Model extends GeoModel<Ar23Item> {
    @Override
    public ResourceLocation getModelResource(Ar23Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/ar23.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Ar23Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, KeyBinding.AIM.isDown() ? "textures/item/ar23_aiming.png" : "textures/item/ar23_not_aiming.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Ar23Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/ar23.animation.json");
    }
}
