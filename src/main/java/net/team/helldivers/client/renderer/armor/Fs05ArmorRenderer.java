package net.team.helldivers.client.renderer.armor;

import net.team.helldivers.client.model.armor.Fs05ArmorModel;
import net.team.helldivers.item.custom.armor.Fs05ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class Fs05ArmorRenderer extends GeoArmorRenderer<Fs05ArmorItem> {
    public Fs05ArmorRenderer() {
        super(new Fs05ArmorModel());
    }
}