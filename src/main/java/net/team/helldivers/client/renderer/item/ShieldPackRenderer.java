package net.team.helldivers.client.renderer.item;

import net.team.helldivers.client.model.item.PortableHellbombModel;
import net.team.helldivers.client.model.item.ShieldPackModel;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;
import net.team.helldivers.item.custom.backpacks.ShieldPackItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ShieldPackRenderer extends GeoItemRenderer<ShieldPackItem> {
    public ShieldPackRenderer() {
        super(new ShieldPackModel());
    }
}
