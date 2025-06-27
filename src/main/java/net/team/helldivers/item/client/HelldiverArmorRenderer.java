package net.team.helldivers.item.client;

import net.team.helldivers.item.custom.HelldiverArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HelldiverArmorRenderer extends GeoArmorRenderer<HelldiverArmorItem> {
    public HelldiverArmorRenderer() {
        super(new HelldiverArmorModel());
    }
}