package net.team.helldivers.client.model.armor;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.armor.Fs05ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class Fs05ArmorModel extends GeoModel<Fs05ArmorItem> {
    @Override
    public ResourceLocation getModelResource(Fs05ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/armor/fs05_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Fs05ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/armor/fs05_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Fs05ArmorItem helldiverArmorItem) {
        return null;
    }
}