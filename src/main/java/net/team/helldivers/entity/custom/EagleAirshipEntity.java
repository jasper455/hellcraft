package net.team.helldivers.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.team.helldivers.entity.ModEntities;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EagleAirshipEntity extends FlyingMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation FLY_BY = RawAnimation.begin().thenLoop("swoop");
    private int ticksLeft = 30;

    public EagleAirshipEntity(EntityType<? extends FlyingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                // Add our flying animation controller
                new AnimationController<>(this, 10, state ->
                        state.setAndContinue(FLY_BY))
                        // Handle the custom instruction keyframe that is part of our animation json
                        .setCustomInstructionKeyframeHandler(state -> {
                            Player player = ClientUtils.getClientPlayer();

                            if (player != null)
                                player.displayClientMessage(Component.literal("KeyFraming"), true);
                        })
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return FlyingMob.createLivingAttributes().add(Attributes.MAX_HEALTH, 1000d)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 8d);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        super.tick();
        ticksLeft--;
        this.setInvisible(ticksLeft > 30);

        if (ticksLeft == 10) {
            Eagle500KgEntity explosive = new Eagle500KgEntity(this, this.level());
            explosive.setPos(this.getX(), this.getY() + 44, this.getZ() - 10);
            explosive.setDeltaMovement(0, -2, 0.5f);
            this.level().addFreshEntity(explosive);
        }
        if (ticksLeft <= 0) {
            this.discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isNoAi() {
        return true;
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;  // Prevents the entity from being pushed by other entities
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;  // Prevents other entities from colliding with this entity
    }
}
