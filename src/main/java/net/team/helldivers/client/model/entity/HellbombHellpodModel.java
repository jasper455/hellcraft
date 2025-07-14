package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class HellbombHellpodModel extends DefaultedEntityGeoModel<HellbombHellpodEntity> {
    public HellbombHellpodModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "hellbomb_hellpod"));
    }
}
