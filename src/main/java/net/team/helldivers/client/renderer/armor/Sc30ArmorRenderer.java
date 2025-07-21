package net.team.helldivers.client.renderer.armor;

import net.team.helldivers.client.model.armor.B01ArmorModel;
import net.team.helldivers.client.model.armor.Sc30ArmorModel;
import net.team.helldivers.item.custom.armor.B01ArmorItem;
import net.team.helldivers.item.custom.armor.Sc30ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class Sc30ArmorRenderer extends GeoArmorRenderer<Sc30ArmorItem> {
    public Sc30ArmorRenderer() {
        super(new Sc30ArmorModel());
    }
}