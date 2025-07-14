package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.CClusterBombExplosionParticlesPacket;
import net.team.helldivers.network.CSmallExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.sound.custom.MovingSoundInstance;

public class ClusterBombProjectileEntity extends AbstractArrow {
    public Vec2 groundedOffset;
    private int soundTicks = 0;
    private int power = 0;

    public ClusterBombProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ClusterBombProjectileEntity(LivingEntity shooter, Level level, int power) {
        super(ModEntities.CLUSTER_BOMB.get(), shooter, level);
        this.power = power;
    }

    public boolean isGrounded() {
        return onGround();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        PacketHandler.sendToServer(new SExplosionPacket(result.getEntity().blockPosition(), this.power));
        this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(6)).forEach(entity -> {
            entity.hurt(level().damageSources().explosion(null), this.power * 4f);
        });
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, true,
                    this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        PacketHandler.sendToServer(new SExplosionPacket(result.getBlockPos(), this.power));
        PacketHandler.sendToAllClients(new CClusterBombExplosionParticlesPacket(result.getBlockPos()));
        this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(6)).forEach(entity -> {
            entity.hurt(level().damageSources().explosion(null), this.power * 4f);
        });
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, true,
                    this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        this.discard();
    }

    @Override
    public void tick() {
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(5f));
        if (this.level().isClientSide) {
            for (int i = 0; i < 10; i++) {
                float XOffset = Mth.randomBetween(this.random, -2.5f, 2.5f);
                float ZOffset = Mth.randomBetween(this.random, -2.5f, 2.5f);
                DustParticleOptions dustParticle = new DustParticleOptions(
                        Vec3.fromRGB24(0x000000).toVector3f(), 5F);
                this.level().addParticle(dustParticle, true,
                        this.getX() + XOffset, this.getY(), this.getZ() + ZOffset, 0, 0, 0);
            }
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, false,
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        soundTicks++;
        if (soundTicks == 120) soundTicks = 0;
        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GENERIC_EXPLODE;
    }
}
