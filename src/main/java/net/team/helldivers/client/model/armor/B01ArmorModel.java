package net.team.helldivers.client.model.armor;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.B01ArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class B01ArmorModel extends GeoModel<B01ArmorItem> {
    @Override
    public ResourceLocation getModelResource(B01ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/armor/b01_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(B01ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/armor/b01_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(B01ArmorItem helldiverArmorItem) {
        return null;
    }
}