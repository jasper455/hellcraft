package net.team.helldivers.client.renderer.armor;

import net.team.helldivers.client.model.armor.B01ArmorModel;
import net.team.helldivers.item.custom.armor.B01ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class B01ArmorRenderer extends GeoArmorRenderer<B01ArmorItem> {
    public B01ArmorRenderer() {
        super(new B01ArmorModel());
    }
}