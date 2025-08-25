package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.backpacks.JumpPackItem;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;
import software.bernie.geckolib.model.GeoModel;

public class PortableHellbombModel extends GeoModel<PortableHellbombItem> {
    @Override
    public ResourceLocation getModelResource(PortableHellbombItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/portable_hellbomb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PortableHellbombItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/portable_hellbomb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PortableHellbombItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/portable_hellbomb.animation.json");
    }
}
