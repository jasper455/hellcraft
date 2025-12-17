package net.team.helldivers.client.renderer.item;

import net.team.helldivers.client.model.item.JumpPackModel;
import net.team.helldivers.client.model.item.PortableHellbombModel;
import net.team.helldivers.item.custom.backpacks.JumpPackItem;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PortableHellbombRenderer extends GeoItemRenderer<PortableHellbombItem> {
    public PortableHellbombRenderer() {
        super(new PortableHellbombModel());
    }
}
