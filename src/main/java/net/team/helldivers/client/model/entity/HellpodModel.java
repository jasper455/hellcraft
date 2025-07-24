package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.HellpodEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class HellpodModel extends DefaultedEntityGeoModel<HellpodEntity> {
    public HellpodModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "hellpod"));
    }
}
