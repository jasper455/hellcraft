package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.goal.LookAtTargetGoal;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SHellpodDestroyBlocksPacket;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;


public class GatlingSentryHellpodEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Boolean> IS_SHOOTING =
            SynchedEntityData.defineId(GatlingSentryHellpodEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation DEPLOY = RawAnimation.begin().thenPlayAndHold("deploy");
    public static final RawAnimation EMPTY = RawAnimation.begin().thenPlayAndHold("empty");
    public static final RawAnimation FALL = RawAnimation.begin().thenLoop("fall");
    public static final RawAnimation LAND = RawAnimation.begin().thenLoop("land");
    public static final RawAnimation SHOOT = RawAnimation.begin().thenLoop("shoot");
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private int groundedTicks = 0;
    private boolean hasLanded = false;

    public GatlingSentryHellpodEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(ModEntities.GATLING_SENTRY.get(), pLevel);
    }

    public GatlingSentryHellpodEntity(Level level) {
        super(ModEntities.GATLING_SENTRY.get(), level);
    }

    public boolean isGrounded() {
        return hasLanded;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false,
                false, entity -> (entity instanceof Monster) && entity.isAlive() && !(entity instanceof GatlingSentryHellpodEntity)));
//        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Monster.class, 25, 100f));
    }

    @Override
    public void tick() {
        // Check if we've landed by looking for solid blocks below
        BlockPos belowPos = this.blockPosition().below();
        boolean onSolidBlock = this.level().getBlockState(belowPos).isSolid();

//        Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(this.isShooting())));

        if (this.getTarget() != null) {
            this.lookControl.setLookAt(this.getTarget());
            setShooting(true);
        } else {
            setShooting(false);
        }

        if (onSolidBlock && this.getDeltaMovement().y <= 0) {
            if (!hasLanded) {
                hasLanded = true;
                // Play landing sound if needed
                this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                this.setDeltaMovement(Vec3.ZERO);
            }
            groundedTicks++;
        }

        if (!hasLanded) {
            // Apply gravity
            Vec3 movement = this.getDeltaMovement();
            this.setDeltaMovement(movement.x, movement.y - 0.1, movement.z);
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (this.level().isClientSide && !this.isGrounded() && Minecraft.getInstance().player.getPersistentData()
                .getBoolean("helldivers.useLodestone")) {
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setScaleData(GenericParticleData.create(3f, 0f).build())
                        .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                        .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39, 200)).build())
                        .addMotion(Mth.randomBetween(RandomSource.create(), -1, 1),
                                this.getDeltaMovement().y * -1,
                                Mth.randomBetween(RandomSource.create(), -1, 1))
                        .setLifetime(20)
                        .setForceSpawn(true)
                        .spawn(this.level(), this.getX(), this.getY() - 3.5, this.getZ());


        }
        if (this.getTarget() != null) {
            BulletProjectileEntity bulletProjectile = new BulletProjectileEntity(this, this.level(), false, false);
            bulletProjectile.shootFromRotation(this, this.getViewXRot(1), this.getViewYRot(1), 0.0f, 5f, 0f);
            bulletProjectile.setYRot(this.getViewXRot(1));
            bulletProjectile.setXRot(this.getViewYRot(1));
            bulletProjectile.setNoGravity(true);
            this.level().addFreshEntity(bulletProjectile);
        }

        if (this.isGrounded()) {
            groundedTicks++;
        }

        if (!this.isGrounded()) {
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(1.0)).forEach(entity -> {
                entity.hurt(level().damageSources().explosion(null), 9999.0F);
            });
            BlockPos pos = new BlockPos((int) this.getX(), (int) (this.getY() - 5), (int) this.getZ());
            PacketHandler.sendToServer(new SHellpodDestroyBlocksPacket(pos, 2));
        }

        if (this.isGrounded() && groundedTicks <= 10 && !this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.getX(), this.getY(), this.getZ(),
                    5, 0f, 0f, 0f, 0.25f);
        }
        super.tick();
    }

    private PlayState animations(AnimationState event) {
//        if (isGrounded() && groundedTicks >= 20 && hasBeenClicked) {
//            event.getController().setAnimation(EMPTY);
//            return PlayState.CONTINUE;
//        }
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(this.isShooting())));
        if (this.getTarget() != null) {
            event.getController().setAnimation(SHOOT);
            return PlayState.CONTINUE;
        }
        if (isGrounded() && groundedTicks >= 20) {
            event.getController().setAnimation(DEPLOY);
            return PlayState.CONTINUE;
        } else if (isGrounded() && groundedTicks >= 21) {
            event.getController().setAnimation(IDLE);
            return PlayState.CONTINUE;
        }
        if (!isGrounded()) {
            event.getController().setAnimation(FALL);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(LAND);
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController animationController = new AnimationController(this, "animationController", 0, this::animations);
        data.add(animationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;  // Allow the entity to be collided with, enabling interaction
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SHOOTING, false);
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {}

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public boolean isShooting() {
        return this.entityData.get(IS_SHOOTING);
    }

    public void setShooting(boolean shooting) {
        this.entityData.set(IS_SHOOTING, shooting);
    }

    // Keep existing attribute methods but modify movement speed and follow range
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50f)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 25); // Increased range
    }
}
