package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.PortableHellbombEntity;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class PortableHellbombEntityModel extends DefaultedEntityGeoModel<PortableHellbombEntity> {
    public PortableHellbombEntityModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "portable_hellbomb_entity"));
    }
}
