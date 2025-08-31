package net.team.helldivers.entity.custom.bots;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.team.helldivers.damage.ModDamageTypes;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.ShootHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractBotEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean hasMeleeAttack;
    private boolean isShooting;

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("death");
    public static final RawAnimation SHOOT = RawAnimation.begin().thenLoop("shoot");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenLoop("melee_attack");

    public AbstractBotEntity(EntityType<? extends Monster> pEntityType, Level pLevel, boolean hasMeleeAttack) {
        super(pEntityType, pLevel);
        this.hasMeleeAttack = hasMeleeAttack;
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

        AnimationController<?> shootAnimController = new AnimationController<>(this, "shoot", 5,
                state -> {
            if (isShooting) {
                return state.setAndContinue(SHOOT);
            }
            return PlayState.STOP;
        });

        AnimationController<?> meleeAttackAnimController = new AnimationController<>(this, "melee_attack", 0, state -> {
            if (this.swinging) {
                return state.setAndContinue(ATTACK);
            }

//            state.getController().forceAnimationReset();

            return PlayState.STOP;
        });

        data.add(walkIdleAnimController);
        data.add(deathAnimController);
        data.add(shootAnimController);
        data.add(meleeAttackAnimController);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        isShooting = false; // reset every tick

        if (target == null) return;

        this.getLookControl().setLookAt(target, 180.0F, 180.0F);
        double distanceSq = this.distanceToSqr(target.getX(), target.getY(), target.getZ());

        if (distanceSq <= 100 && this.tickCount % 5 == 0) {
            if (!hasMeleeAttack) {
                ShootHelper.shoot(this, this.level(), 0, 5, 0.3, false);
                isShooting = true;
            }
        }
    }
    @Override
    protected void tickDeath() {
        // Increment deathTime without applying default rotation
        ++this.deathTime;
        if (this.deathTime >= 50 && !this.level().isClientSide()) {
            this.remove(RemovalReason.KILLED);
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
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(ModDamageTypes.RAYCAST) ||
                pSource.is(ModDamageTypes.ORBITAL_LASER) ||
                pSource.is(DamageTypes.EXPLOSION) ||
                pSource.is(DamageTypes.GENERIC_KILL) ||
                pSource.is(DamageTypes.PLAYER_ATTACK)) {
            super.hurt(pSource, pAmount);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GENERIC_AUTOMATON_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.GENERIC_AUTOMATON_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    public abstract AABB getDamageHitbox();
}