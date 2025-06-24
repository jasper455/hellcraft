package net.infinite1274.helldivers.client.renderer.block;

import net.infinite1274.helldivers.block.entity.custom.HellbombBlockEntity;
import net.infinite1274.helldivers.client.model.block.HellbombModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HellbombBlockRenderer extends GeoBlockRenderer<HellbombBlockEntity> {
    public HellbombBlockRenderer() {
        super(new HellbombModel());
    }
}
