package net.team.helldivers.client.model.entity.bots;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.bots.AutomatonCannonEntity;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AutomatonCannonModel extends DefaultedEntityGeoModel<AutomatonCannonEntity> {
    public AutomatonCannonModel() {
        super(ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "cannon"));
    }

    @Override
    public ResourceLocation getModelResource(AutomatonCannonEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "geo/entity/bots/cannon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AutomatonCannonEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/entity/bots/cannon/cannon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AutomatonCannonEntity botEntity) {
        return ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "animations/entity/bots/cannon.animation.json");
    }

    @Override
    public void setCustomAnimations(AutomatonCannonEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("barrel");
        if (head != null) {
            EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
