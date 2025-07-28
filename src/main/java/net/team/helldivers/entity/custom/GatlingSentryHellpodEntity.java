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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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
    public static final RawAnimation SHOOT = RawAnimation.begin().thenPlayAndHold("shoot");
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlayAndHold("idle");

    private int groundedTicks = 0;
    private int groundedTicksSinceEmpty = 0;
    private boolean hasBeenClicked = false;
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
        this.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, false,
                false, entity -> !(entity instanceof GatlingSentryHellpodEntity) && entity.isAlive()));
    }

    @Override
    public void tick() {
        // Check if we've landed by looking for solid blocks below
        BlockPos belowPos = this.blockPosition().below();
        boolean onSolidBlock = this.level().getBlockState(belowPos).isSolid();

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
//        if (this.getTarget() != null) {
//            BulletProjectileEntity bulletProjectile = new BulletProjectileEntity(this, this.level(), false, false);
//            bulletProjectile.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0f, 5f, 0f);
//            bulletProjectile.setYRot(this.getYRot());
//            bulletProjectile.setXRot(this.getXRot());
//            bulletProjectile.setNoGravity(true);
//            this.level().addFreshEntity(bulletProjectile);
//        }

        if (this.isGrounded()) {
            groundedTicks++;
        }
        if (this.isGrounded() && isShooting()) {
            groundedTicksSinceEmpty++;
        }

        if (this.isGrounded() && isShooting() && groundedTicksSinceEmpty >= 60) {
            this.discard();
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
//        if (isGrounded() && isShooting()) {
//            event.getController().setAnimation(SHOOT);
//            return PlayState.CONTINUE;
//        }
        if (isGrounded() && groundedTicks >= 20) {
            event.getController().setAnimation(DEPLOY);
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
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SHOOTING, false);
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
                .add(Attributes.MAX_HEALTH, Float.MAX_VALUE)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D); // Increased range
    }
}
