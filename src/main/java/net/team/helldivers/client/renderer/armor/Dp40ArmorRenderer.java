package net.team.helldivers.client.renderer.armor;

import net.team.helldivers.client.model.armor.Dp40ArmorModel;
import net.team.helldivers.item.custom.armor.Dp40ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class Dp40ArmorRenderer extends GeoArmorRenderer<Dp40ArmorItem> {
    public Dp40ArmorRenderer() {
        super(new Dp40ArmorModel());
    }
}