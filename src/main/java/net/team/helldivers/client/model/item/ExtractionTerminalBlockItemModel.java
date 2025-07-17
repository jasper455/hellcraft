package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.item.custom.ExtractionTerminalBlockItem;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class ExtractionTerminalBlockItemModel extends GeoModel<ExtractionTerminalBlockItem> {
    @Override
    public ResourceLocation getModelResource(ExtractionTerminalBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/block/extraction_terminal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ExtractionTerminalBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/block/extraction_terminal.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ExtractionTerminalBlockItem animatable) {
        return null;
    }
}
