package net.team.helldivers.client.renderer.item;

import net.team.helldivers.client.model.item.GalacticTerminalBlockItemModel;
import net.team.helldivers.client.model.item.JumpPackModel;
import net.team.helldivers.item.custom.GalacticTerminalBlockItem;
import net.team.helldivers.item.custom.backpacks.JumpPackItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class JumpPackRenderer extends GeoItemRenderer<JumpPackItem> {
    public JumpPackRenderer() {
        super(new JumpPackModel());
    }
}
