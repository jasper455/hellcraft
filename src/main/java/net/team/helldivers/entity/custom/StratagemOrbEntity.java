package net.team.helldivers.entity.custom;

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
    private float rotation;
    public Vec3 groundedOffset;
    private int groundedTicks = 0;
    public String stratagemType = "";

    public StratagemOrbEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StratagemOrbEntity(LivingEntity shooter, Level level) {
        super(ModEntities.STRATAGEM_ORB.get(), shooter, level);
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

        if (stratagemType == null) this.discard();
    }

    @Override
    public void tick() {
            super.tick();
        if (this.isGrounded()) {
            groundedTicks++;
        }
        // Orbital Precision Strike Entity stuff
        if (stratagemType.equals("Orbital Precision Strike") && groundedTicks == 60 && !this.level().isClientSide) {
            float randomPosX = (Mth.randomBetween(this.level().getRandom(), 87.5f, 92.5f));
            float randomPosY = (Mth.randomBetween(this.level().getRandom(), -5.0f, 5.0f));

            MissileProjectileEntity explosive = new MissileProjectileEntity(((LivingEntity) this.getOwner()), this.level());
            explosive.setPos(this.getX() + randomPosX, 200 + randomPosY, this.getZ());
            explosive.setDeltaMovement(-1.6f, 0f, 0f);
            this.level().addFreshEntity(explosive);
            this.playSound(ModSounds.FIRE_ORBITAL_STRIKE.get(), 10000000.0f, 1.0f);
        }
        if (stratagemType.equals("Orbital Precision Strike") && groundedTicks == 180) {
            this.discard();
            groundedTicks = 0;
        }

        // Hellbomb Entity stuff
        if (stratagemType.equals("Hellbomb") && groundedTicks == 120 && !this.level().isClientSide) {
            HellpodProjectileEntity hellpod = new HellpodProjectileEntity(((LivingEntity) this.getOwner()), this.level());
            hellpod.setPos(this.getBlockX(), 200, this.getBlockZ());
            this.level().addFreshEntity(hellpod);
        }
        if (stratagemType.equals("Hellbomb") && groundedTicks == 180) {
            this.discard();
            groundedTicks = 0;
        }

        // 120 Barrage Entity Stuff
        if (stratagemType.equals("Orbital 120MM HE Barrage") && !this.level().isClientSide) {
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 25, 60,
                        groundedTicks, this, false));
            }
        }
        if (stratagemType.equals("Orbital 120MM HE Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }

        // 380 Barrage Entity Stuff
        if (stratagemType.equals("Orbital 380MM HE Barrage") && !this.level().isClientSide) {
            if (groundedTicks > 75) {
                MinecraftForge.EVENT_BUS.register(new OrbitalBarrage(this.level(), this.blockPosition(), 50, 60,
                        groundedTicks, this, true));
            }
        }
        if (stratagemType.equals("Orbital 380MM HE Barrage") && groundedTicks > 750) {
            this.discard();
            groundedTicks = 0;
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }
}