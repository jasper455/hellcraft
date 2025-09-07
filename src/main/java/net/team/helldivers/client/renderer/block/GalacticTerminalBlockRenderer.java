package net.team.helldivers.client.renderer.block;

import net.minecraft.world.phys.Vec3;
import net.team.helldivers.block.entity.custom.GalacticTerminalBlockEntity;
import net.team.helldivers.client.model.block.GalacticTerminalBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GalacticTerminalBlockRenderer extends GeoBlockRenderer<GalacticTerminalBlockEntity> {
    public GalacticTerminalBlockRenderer() {
        super(new GalacticTerminalBlockModel());
    }

    @Override
    public boolean shouldRender(GalacticTerminalBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(GalacticTerminalBlockEntity pBlockEntity) {
        return true;
    }
}
