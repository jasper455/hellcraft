package net.team.helldivers.client.renderer.item;

import net.team.helldivers.client.model.item.BotContactMineBlockItemModel;
import net.team.helldivers.client.model.item.ExtractionTerminalBlockItemModel;
import net.team.helldivers.item.custom.BotContactMineBlockItem;
import net.team.helldivers.item.custom.ExtractionTerminalBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BotContactMineBlockItemRenderer extends GeoItemRenderer<BotContactMineBlockItem> {
    public BotContactMineBlockItemRenderer() {
        super(new BotContactMineBlockItemModel());
    }
}
