package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.ExtractionTerminalBlockItem;
import net.team.helldivers.item.custom.backpacks.JumpPackItem;
import software.bernie.geckolib.model.GeoModel;

public class JumpPackModel extends GeoModel<JumpPackItem> {
    @Override
    public ResourceLocation getModelResource(JumpPackItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/item/jump_pack.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JumpPackItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/item/jump_pack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JumpPackItem animatable) {
        return null;
    }
}
