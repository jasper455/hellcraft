package net.team.helldivers.client.renderer.block;

import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.team.helldivers.client.model.block.HellbombModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HellbombBlockRenderer extends GeoBlockRenderer<HellbombBlockEntity> {
    public HellbombBlockRenderer() {
        super(new HellbombModel());
    }
}
