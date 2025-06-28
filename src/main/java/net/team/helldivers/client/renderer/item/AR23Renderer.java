package net.team.helldivers.client.renderer.item;

import net.team.helldivers.client.model.item.Ar23Model;
import net.team.helldivers.item.custom.Ar23Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AR23Renderer extends GeoItemRenderer<Ar23Item> {
    public AR23Renderer() {
        super(new Ar23Model());
    }
}
