package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.Ar23Item;
import net.team.helldivers.item.custom.EAT17Item;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.model.GeoModel;

public class EAT17Model extends GeoModel<EAT17Item> {
    @Override
    public ResourceLocation getModelResource(EAT17Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/eat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EAT17Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/eat_17.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EAT17Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/eat.animation.json");
    }
}
