package net.infinite1274.helldivers.item.client;

import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.item.custom.HelldiverArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HelldiverArmorModel extends GeoModel<HelldiverArmorItem> {
    @Override
    public ResourceLocation getModelResource(HelldiverArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/armor/helldiver_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HelldiverArmorItem helldiverArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/armor/helldiver_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HelldiverArmorItem helldiverArmorItem) {
        return null;
    }
}