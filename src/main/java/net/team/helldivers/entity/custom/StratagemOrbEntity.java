package net.team.helldivers.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.helper.OrbitalBarrage;
import net.team.helldivers.sound.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class StratagemOrbEntity extends AbstractArrow {
    private static final EntityDataAccessor<String> STRATAGEM_TYPE =
            SynchedEntityData.defineId(StratagemOrbEntity.class, EntityDataSerializers.STRING);
    private float rotation;
    public Vec3 groundedOffset;
    private int groundedTicks = 0;
    public String stratagemType = "";

    public StratagemOrbEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StratagemOrbEntity(LivingEntity shooter, Level level, String stratagemType) {
        super(ModEntities.STRATAGEM_ORB.get(), shooter, level);
        setStratagemType(stratagemType);
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

        if (getStratagemType() == null) this.discard();
    }

    @Override
    public void tick() {
            super.tick();
        if (this.isGrounded()) {
            groundedTicks++;
        }
        // Orbital Precision Strike Entity stuff
        if (getStratagemType().equals("Orbital Precision Strike") && groundedTicks == 60 && !this.level().isClientSide) {
            float randomPosX = (Mth.randomBetween(this.level().getRandom(), 87.5f, 92.5f));
            float randomPosY = (Mth.randomBetween(this.level().getRandom(), -5.0f, 5.0f));

            MissileProjectileEntity explosive = new MissileProjectileEntity(((LivingEntity) this.getOwner()), this.level(), 17);
            explosive.setPos(this.getX() + randomPosX, 200 + randomPosY, this.getZ());
            explosive.setDeltaMovement(-1.6f, 0f, 0f);
            this.level().addFreshEntity(explosive);
            if (random.nextBoolean() && random.nextBoolean()) {
                this.playSound(ModSounds.FIRE_ORBITAL_STRIKE.get(), 10000000.0f, 1.0f);
            }
        }
        if (getStratagemType().equals("Orbital Precision Strike") && groundedTicks == 100) {
            this.discard();
            groundedTicks = 0;
        }

        // Hellbomb Entity stuff
        if (getStratagemType().equals("Hellbomb") && groundedTicks == 120 && !this.level().isClientSide) {
            HellpodProjectileEntity hellpod = new HellpodProjectileEntity(((LivingEntity) this.getOwner()), this.level());
            hellpod.setPos(this.getBlockX(), 200, this.getBlockZ());
            this.level().addFreshEntity(hellpod);
        }
        if (getStratagemType().equals("Hellbomb") && groundedTicks == 180) {
            this.discard();
            groundedTicks = 0;
        }

        // 120 Barrage Entity Stuff
        if (getStratagemType().equals("Orbital 120MM HE Barrage") && !this.level().isClientSide) {
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 25, 60,
                        groundedTicks, this, false));
            }
        }
        if (getStratagemType().equals("Orbital 120MM HE Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // 380 Barrage Entity Stuff
        if (getStratagemType().equals("Orbital 380MM HE Barrage") && !this.level().isClientSide) {
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 50, 60,
                        groundedTicks, this, true));
            }
        }
        if (getStratagemType().equals("Orbital 380MM HE Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // 500Kg Bomb Entity Stuff
        if (getStratagemType().equals("Eagle 500KG Bomb") && !this.level().isClientSide) {
            if (groundedTicks == 80) {
                EagleAirshipEntity eagleAirshipEntity = new EagleAirshipEntity(ModEntities.EAGLE_AIRSHIP.get(), this.level());
                eagleAirshipEntity.setPos(this.getX(), this.getY() + 5, this.getZ());
                this.level().addFreshEntity(eagleAirshipEntity);
            }
        }
        if (getStratagemType().equals("Eagle 500KG Bomb") && groundedTicks > 90) {
            this.discard();
            groundedTicks = 0;
        }

        // Expendable Anti-Tank Entity Stuff
        if (getStratagemType().equals("Expendable Anti-Tank") && !this.level().isClientSide) {
            if (groundedTicks == 100) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);
            }
        }
        if (getStratagemType().equals("Expendable Anti-Tank") && groundedTicks > 140) {
            this.discard();
            groundedTicks = 0;
        }

        // Orbital Laser Entity Stuff
        if (getStratagemType().equals("Orbital Laser") && !this.level().isClientSide) {
            if (groundedTicks == 60) {
                OrbitalLaserEntity laserEntity = new OrbitalLaserEntity(ModEntities.ORBITAL_LASER.get(), this.level());
                laserEntity.setOwner(this.getOwner());
                laserEntity.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(laserEntity);
            }
        }
        if (getStratagemType().equals("Orbital Laser") && groundedTicks > 60) {
            this.discard();
            groundedTicks = 0;
        }

        // Orbital Laser Entity Stuff
        if (getStratagemType().equals("Resupply") && !this.level().isClientSide) {
            if (groundedTicks == 300) {
                SupportHellpodEntity supportHellpodEntity = new SupportHellpodEntity(this.level(), getStratagemType());
                supportHellpodEntity.setPos(this.getX(), 200, this.getZ());
                this.level().addFreshEntity(supportHellpodEntity);
            }
        }
        if (getStratagemType().equals("Resupply") && groundedTicks > 320) {
            this.discard();
            groundedTicks = 0;
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.STRATAGEM_ORB_LAND.get();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRATAGEM_TYPE, "");
    }

    public String getStratagemType() {
        return this.entityData.get(STRATAGEM_TYPE);
    }

    public void setStratagemType(String type) {
        this.entityData.set(STRATAGEM_TYPE, type);
    }

}