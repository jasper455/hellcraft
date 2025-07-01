package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.EagleAirshipEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class EagleAirshipModel extends DefaultedEntityGeoModel<EagleAirshipEntity> {
    public EagleAirshipModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "eagle_airship"));
    }
}
