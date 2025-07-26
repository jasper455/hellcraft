package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.guns.Ar23Item;
import net.team.helldivers.item.custom.guns.StalwartItem;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.model.GeoModel;

public class StalwartModel extends GeoModel<StalwartItem> {
    @Override
    public ResourceLocation getModelResource(StalwartItem object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/stalwart.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StalwartItem object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/stalwart.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StalwartItem object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/stalwart.animation.json");
    }
}
