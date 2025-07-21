package net.team.helldivers.client.model.armor;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.armor.Dp40ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class Dp40ArmorModel extends GeoModel<Dp40ArmorItem> {
    @Override
    public ResourceLocation getModelResource(Dp40ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/armor/dp40_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Dp40ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/armor/dp40_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Dp40ArmorItem helldiverArmorItem) {
        return null;
    }
}