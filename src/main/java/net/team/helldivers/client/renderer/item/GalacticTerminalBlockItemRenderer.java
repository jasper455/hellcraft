package net.team.helldivers.client.renderer.item;

import net.team.helldivers.client.model.item.GalacticTerminalBlockItemModel;
import net.team.helldivers.item.custom.GalacticTerminalBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GalacticTerminalBlockItemRenderer extends GeoItemRenderer<GalacticTerminalBlockItem> {
    public GalacticTerminalBlockItemRenderer() {
        super(new GalacticTerminalBlockItemModel());
    }
}
