package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.guns.AmrItem;
import net.team.helldivers.item.custom.guns.Ar23Item;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.model.GeoModel;

public class AmrModel extends GeoModel<AmrItem> {
    @Override
    public ResourceLocation getModelResource(AmrItem object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/amr.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AmrItem object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, KeyBinding.AIM.isDown() ? "textures/item/amr_aiming.png" : "textures/item/amr_not_aiming.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AmrItem object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/ar23.animation.json");
    }
}
