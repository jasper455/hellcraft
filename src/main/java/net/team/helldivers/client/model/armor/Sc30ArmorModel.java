package net.team.helldivers.client.model.armor;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.armor.B01ArmorItem;
import net.team.helldivers.item.custom.armor.Sc30ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class Sc30ArmorModel extends GeoModel<Sc30ArmorItem> {
    @Override
    public ResourceLocation getModelResource(Sc30ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/armor/sc30_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Sc30ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/armor/sc30_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Sc30ArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/armor/sc30.animation.json");
    }
}