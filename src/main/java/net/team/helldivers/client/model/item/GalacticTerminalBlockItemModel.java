package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.ExtractionTerminalBlockItem;
import net.team.helldivers.item.custom.GalacticTerminalBlockItem;
import software.bernie.geckolib.model.GeoModel;

public class GalacticTerminalBlockItemModel extends GeoModel<GalacticTerminalBlockItem> {
    @Override
    public ResourceLocation getModelResource(GalacticTerminalBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/block/galactic_terminal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GalacticTerminalBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/block/galactic_terminal.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GalacticTerminalBlockItem animatable) {
        return null;
    }
}
