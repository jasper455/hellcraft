package net.team.helldivers.client.renderer.block;

import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.client.model.block.ExtractionTerminalBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ExtractionTerminalBlockRenderer extends GeoBlockRenderer<ExtractionTerminalBlockEntity> {
    public ExtractionTerminalBlockRenderer() {
        super(new ExtractionTerminalBlockModel());
    }
}
