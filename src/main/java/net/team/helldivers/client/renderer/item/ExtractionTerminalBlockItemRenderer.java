package net.team.helldivers.client.renderer.item;

import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.client.model.block.ExtractionTerminalBlockModel;
import net.team.helldivers.client.model.item.ExtractionTerminalBlockItemModel;
import net.team.helldivers.item.custom.ExtractionTerminalBlockItem;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ExtractionTerminalBlockItemRenderer extends GeoItemRenderer<ExtractionTerminalBlockItem> {
    public ExtractionTerminalBlockItemRenderer() {
        super(new ExtractionTerminalBlockItemModel());
    }
}
