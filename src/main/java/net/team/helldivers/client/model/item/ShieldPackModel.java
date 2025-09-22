package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;
import net.team.helldivers.item.custom.backpacks.ShieldPackItem;
import software.bernie.geckolib.model.GeoModel;

public class ShieldPackModel extends GeoModel<ShieldPackItem> {
    @Override
    public ResourceLocation getModelResource(ShieldPackItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/shield_pack.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShieldPackItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/shield_pack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShieldPackItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/shield_pack.animation.json");
    }
}
