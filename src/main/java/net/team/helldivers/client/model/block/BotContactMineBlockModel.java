package net.team.helldivers.client.model.block;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.custom.BotContactMineBlockEntity;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class BotContactMineBlockModel extends DefaultedBlockGeoModel<BotContactMineBlockEntity> {
    public BotContactMineBlockModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "bot_contact_mine"));
    }

    @Override
    public ResourceLocation getModelResource(BotContactMineBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/block/bot_contact_mine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BotContactMineBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/block/bot_contact_mine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BotContactMineBlockEntity animatable) {
        return null;
    }
}
