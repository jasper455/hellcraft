package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.team.helldivers.damage.ModDamageSources;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.sound.custom.MovingSoundInstance;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.List;

public class OrbitalLaserEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int damageTimer = 0;
    private int targetUpdateTimer = 0;
    private static final int DAMAGE_INTERVAL = 10;
    private static final int TARGET_UPDATE_INTERVAL = 20; // Update targets every second
    private static final float DAMAGE_AMOUNT = 10.0F;
    private static final double DAMAGE_RADIUS = 3.0D;
    private LivingEntity currentTarget = null;
    private Entity owner = null;

    public OrbitalLaserEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (onGround()) {
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                    this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            if (this.tickCount % 20 == 0) {
                // Instead of the explosion causing damage, just set the area on fire
                BlockPos pos = this.blockPosition();
                for(int x = -2; x <= 2; x++) {
                    for(int z = -2; z <= 2; z++) {
                        BlockPos firePos = pos.offset(x, 0, z);
                        if (this.level().getBlockState(firePos).isAir() &&
                                this.level().getBlockState(firePos.below()).isSolidRender(this.level(), firePos.below())) {
                            this.level().setBlockAndUpdate(firePos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState());
                        }
                    }
                }
            }
            if (this.tickCount % 8 == 0) {
                Minecraft.getInstance().getSoundManager()
                        .play(new MovingSoundInstance(this, ModSounds.ORBITAL_LASER_IDLE.get(), 2.25f));
            }
        }


        if (this.tickCount >= 600) {
            this.discard();
        }

        // Update target periodically
        if (++targetUpdateTimer >= TARGET_UPDATE_INTERVAL) {
            targetUpdateTimer = 0;
            findNewTarget();
        }

        // Block detection and teleportation code...
        BlockPos currentPos = this.blockPosition();
        BlockPos highestBlock = null;
        for (int y = level().getMaxBuildHeight(); y > currentPos.getY(); y--) {
            BlockPos checkPos = new BlockPos(currentPos.getX(), y, currentPos.getZ());
            if (!level().getBlockState(checkPos).isAir()) {
                highestBlock = checkPos.below();
                break;
            }
        }

        if (highestBlock != null) {
            this.setPos(highestBlock.getX() + 0.5, highestBlock.getY() + 2, highestBlock.getZ() + 0.5);
        }

        // Damage code
        if (++damageTimer >= DAMAGE_INTERVAL) {
            damageTimer = 0;
            dealDamageToNearbyEntities();
        }
    }

    private void findNewTarget() {
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB searchBox = new AABB(
                this.getX() - followRange, this.getY() - followRange, this.getZ() - followRange,
                this.getX() + followRange, this.getY() + followRange, this.getZ() + followRange
        );

        List<LivingEntity> potentialTargets = level().getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                entity -> entity instanceof Monster &&
                        !(entity instanceof OrbitalLaserEntity) &&
                        entity.isAlive() &&
                        hasPathToSky(entity.blockPosition())
        );

        if (!potentialTargets.isEmpty()) {
            currentTarget = potentialTargets.stream()
                    .min(Comparator.comparingDouble(entity ->
                            entity.distanceToSqr(this.getX(), this.getY(), this.getZ())))
                    .orElse(null);

            if (currentTarget != null) {
                // Get the position above the target with clear sky access
                BlockPos targetPos = getHighestAccessiblePosition(currentTarget.blockPosition());
                this.getNavigation().moveTo(
                        targetPos.getX() + 0.5,
                        targetPos.getY(),
                        targetPos.getZ() + 0.5,
                        1.2D
                );
            }
        }
    }

    private boolean hasPathToSky(BlockPos pos) {
        for (int y = pos.getY(); y < level().getMaxBuildHeight(); y++) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = level().getBlockState(checkPos);
            if (!state.isAir() && !state.liquid()) {
                return false;
            }
        }
        return true;
    }

    private BlockPos getHighestAccessiblePosition(BlockPos targetPos) {
        BlockPos highestPos = targetPos;

        // Find the first solid block above the target
        for (int y = targetPos.getY(); y < level().getMaxBuildHeight(); y++) {
            BlockPos checkPos = new BlockPos(targetPos.getX(), y, targetPos.getZ());
            BlockState state = level().getBlockState(checkPos);
            if (!state.isAir() && !state.liquid()) {
                // Return the position below the solid block
                return new BlockPos(targetPos.getX(), y, targetPos.getZ());
            }
        }

        return highestPos;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level) {
            @Override
            protected boolean canUpdatePath() {
                return true;
            }

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return hasPathToSky(pos);
            }

            @Override
            protected PathFinder createPathFinder(int followRange) {
                this.nodeEvaluator = new WalkNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                this.nodeEvaluator.setCanOpenDoors(true);
                this.nodeEvaluator.setCanFloat(true);
                return new PathFinder(this.nodeEvaluator, followRange);
            }
        };
    }


    private void dealDamageToNearbyEntities() {
        AABB damageArea = new AABB(
                this.getX() - DAMAGE_RADIUS, this.getY() - 1, this.getZ() - DAMAGE_RADIUS,
                this.getX() + DAMAGE_RADIUS, this.getY() + 3, this.getZ() + DAMAGE_RADIUS
        );

        List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(
                LivingEntity.class,
                damageArea,
                entity -> entity instanceof LivingEntity && !(entity instanceof OrbitalLaserEntity)
        );

        for (LivingEntity entity : nearbyEntities) {
            if (owner != null) {
                entity.hurt(ModDamageSources.orbitalLaser(owner), DAMAGE_AMOUNT);
                entity.setSecondsOnFire(3);
            }
        }
    }

    @Override
    protected void registerGoals() {
        // We're handling targeting manually, so we only need movement goals
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.2D, true) {
            @Override
            protected void checkAndPerformAttack(LivingEntity target, double distToEnemySqr) {
                // Damage is handled in tick()
            }
        });
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, false,
                true, entity -> !(entity instanceof OrbitalLaserEntity) && entity.isAlive()
                && hasPathToSky(entity.blockPosition())
        ));
    }

    // Keep existing attribute methods but modify movement speed and follow range
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, Float.MAX_VALUE)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D); // Increased range
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return entity.hurt(ModDamageSources.orbitalLaser(this), 3.0F);
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }
    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }
    @Override
    public boolean canBreatheUnderwater() {return true;}
    @Override
    public boolean onClimbable() {
        return false;
    }
    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }
    @Override
    public boolean fireImmune() {
        return true;
    }
    @Override
    public boolean ignoreExplosion() {
        return true;
    }
    @Override
    public boolean displayFireAnimation() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}