package net.team.helldivers.entity.custom.bots;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.team.helldivers.entity.custom.GatlingSentryHellpodEntity;
import net.team.helldivers.entity.goal.BotShootTargetGoal;
import net.team.helldivers.entity.goal.BotWalkAndShootGoal;
import net.team.helldivers.util.ShootHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractBotEntity extends Monster implements GeoEntity{
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean hasDeathAnim;
    private final boolean hasMeleeAttack;

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("death");

    public AbstractBotEntity(EntityType<? extends Monster> pEntityType, Level pLevel, boolean hasDeathAnim, boolean hasMeleeAttack) {
        super(pEntityType, pLevel);
        this.hasDeathAnim = hasDeathAnim;
        this.hasMeleeAttack = hasMeleeAttack;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(1, new BotWalkAndShootGoal(this, 1.0D, 10.0F, 40)); // speed, range, cooldown
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

//    private PlayState animations(AnimationState event) {
//        if (isGrounded() && groundedTicks >= 20 && hasBeenClicked) {
//            event.getController().setAnimation(EMPTY);
//            return PlayState.CONTINUE;
//        }
//        Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(this.isShooting())));
//        if (this.getTarget() != null) {
//            event.getController().setAnimation(SHOOT);
//            return PlayState.CONTINUE;
//        }
//        if (isGrounded() && groundedTicks >= 20) {
//            event.getController().setAnimation(DEPLOY);
//            return PlayState.CONTINUE;
//        }
//        return PlayState.CONTINUE;
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
//        AnimationController<?> animationController = new AnimationController<>(this, "animationController", 0, this::animations);

        AnimationController<?> walkIdleAnimController = new AnimationController<>(this, "walk/idle", 0,
                state -> state.setAndContinue(state.isMoving() ? WALK : IDLE));

        AnimationController<?> deathAnimController = new AnimationController<>(this, "death", 0,
                state -> state.getAnimatable().isDeadOrDying() ? state.setAndContinue(DEATH) : PlayState.STOP);

//        data.add(animationController);
        data.add(walkIdleAnimController);
        if (hasDeathAnim) {
            data.add(deathAnimController);
        }
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (target == null) return;

        this.getLookControl().setLookAt(target, 180.0F, 180.0F);
        double distanceSq = this.distanceToSqr(target.getX(), target.getY(), target.getZ());
        
        if (!(distanceSq > 100) && this.tickCount % 5==0) {
            target.sendSystemMessage(Component.literal("test"));
            ShootHelper.shoot(this, this.level(), 0, 5, 0.3, false);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean isPushable() {
        return false;  // Prevent the entity from being pushed by other entities
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {}

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }
}
