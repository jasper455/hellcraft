package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SupportHellpodModel extends DefaultedEntityGeoModel<SupportHellpodEntity> {
    public SupportHellpodModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "support_hellpod"));
    }
}
