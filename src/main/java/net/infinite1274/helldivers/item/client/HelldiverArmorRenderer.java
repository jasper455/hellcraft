package net.infinite1274.helldivers.item.client;

import net.infinite1274.helldivers.item.custom.HelldiverArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HelldiverArmorRenderer extends GeoArmorRenderer<HelldiverArmorItem> {
    public HelldiverArmorRenderer() {
        super(new HelldiverArmorModel());
    }
}