package net.team.helldivers.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.GatlingSentryHellpodEntity;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GatlingSentryHellpodModel extends DefaultedEntityGeoModel<GatlingSentryHellpodEntity> {
    public GatlingSentryHellpodModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "gatling_sentry_hellpod"));
    }
}
