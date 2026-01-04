package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.AutomatonTrooperEntity;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AutomatonTrooperModel extends DefaultedEntityGeoModel<AutomatonTrooperEntity> {
    public AutomatonTrooperModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "automaton_trooper"));
    }

    @Override
    public ResourceLocation getModelResource(AutomatonTrooperEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/automaton_trooper.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AutomatonTrooperEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/automaton_trooper/automaton_trooper.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AutomatonTrooperEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/automaton_trooper.animation.json");
    }
}
