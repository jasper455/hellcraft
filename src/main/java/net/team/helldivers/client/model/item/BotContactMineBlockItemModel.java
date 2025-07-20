package net.team.helldivers.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.BotContactMineBlockItem;
import net.team.helldivers.item.custom.ExtractionTerminalBlockItem;
import software.bernie.geckolib.model.GeoModel;

public class BotContactMineBlockItemModel extends GeoModel<BotContactMineBlockItem> {
    @Override
    public ResourceLocation getModelResource(BotContactMineBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/block/bot_contact_mine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BotContactMineBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/block/bot_contact_mine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BotContactMineBlockItem animatable) {
        return null;
    }
}
