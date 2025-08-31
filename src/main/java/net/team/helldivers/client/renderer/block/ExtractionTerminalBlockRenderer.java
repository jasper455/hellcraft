package net.team.helldivers.client.renderer.block;

import net.minecraft.world.phys.Vec3;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.client.model.block.ExtractionTerminalBlockModel;
import net.team.helldivers.client.renderer.block.layer.ExtractionTerminalBeamLayer;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ExtractionTerminalBlockRenderer extends GeoBlockRenderer<ExtractionTerminalBlockEntity> {
    public ExtractionTerminalBlockRenderer() {
        super(new ExtractionTerminalBlockModel());
        this.addRenderLayer(new ExtractionTerminalBeamLayer(this));
    }

    @Override
    public boolean shouldRender(ExtractionTerminalBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(ExtractionTerminalBlockEntity pBlockEntity) {
        return true;
    }
}
