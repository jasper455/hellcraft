package net.team.helldivers.client.renderer.block;

import net.team.helldivers.block.entity.custom.BotContactMineBlockEntity;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.client.model.block.BotContactMineBlockModel;
import net.team.helldivers.client.model.block.ExtractionTerminalBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BotContactMineBlockRenderer extends GeoBlockRenderer<BotContactMineBlockEntity> {
    public BotContactMineBlockRenderer() {
        super(new BotContactMineBlockModel());
    }
}
