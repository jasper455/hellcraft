package net.team.helldivers.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.helper.OrbitalBarrage;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.worldgen.dimension.ModDimensions;

public class FragGrenadeEntity extends AbstractArrow {
    private float rotation;
    public Vec3 groundedOffset;
    private int groundedTicks = 0;
    public FragGrenadeEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FragGrenadeEntity(LivingEntity shooter, Level level) {
        super(ModEntities.FIRE_GRENADE.get(), shooter, level);
    }

    public float getRenderingRotation() {
            rotation += 0.5f;
            if (rotation >= 360) {
                rotation = 0;
            }
        return rotation;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide && result.getEntity() instanceof LivingEntity) {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }


    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(pResult.getDirection() == Direction.SOUTH) {
            groundedOffset = new Vec3(180f, 180f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.NORTH) {
            groundedOffset = new Vec3(180f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.EAST) {
            groundedOffset = new Vec3(180f, -90f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.WEST) {
            groundedOffset = new Vec3(180f, 90f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }

        if(pResult.getDirection() == Direction.DOWN) {
            groundedOffset = new Vec3(0f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.UP) {
            groundedOffset = new Vec3(270f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
    }

    @Override
    public void tick() {
            super.tick();
        if (this.isGrounded()) {
            groundedTicks++;
        }
        if (this.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            this.discard();
        }

        if (groundedTicks == 15 && !this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1, false, Level.ExplosionInteraction.BLOCK);
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1, false, Level.ExplosionInteraction.BLOCK);
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(3.0)).forEach(entity -> {
                entity.hurt(level().damageSources().explosion(null), 30.0F);
            });
            this.discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.SUSPICIOUS_GRAVEL_STEP;
    }

}