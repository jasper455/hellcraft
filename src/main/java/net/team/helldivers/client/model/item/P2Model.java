package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.Ar23Item;
import net.team.helldivers.item.custom.P2Item;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.model.GeoModel;

public class P2Model extends GeoModel<P2Item> {
    @Override
    public ResourceLocation getModelResource(P2Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/p2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(P2Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/p2.png");
    }

    @Override
    public ResourceLocation getAnimationResource(P2Item object) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/item/p2.animation.json");
    }
}
