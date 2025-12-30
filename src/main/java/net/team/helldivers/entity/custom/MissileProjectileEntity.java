package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.CSmallExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class MissileProjectileEntity extends AbstractArrow {
    private int power = 0;
    private boolean isNapalm;
    private boolean isCluster;

    public MissileProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MissileProjectileEntity(LivingEntity shooter, Level level, int power, boolean isNapalm, boolean isCluster) {
        super(ModEntities.MISSILE_PROJECTILE.get(), shooter, level);
        this.power = power;
        this.isNapalm = isNapalm;
        this.isCluster = isCluster;
    }

    public boolean isGrounded() {
        return onGround();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!isCluster) {
            PacketHandler.sendToAllClients(new CSmallExplosionParticlesPacket(result.getEntity().blockPosition()));
            PacketHandler.sendToServer(new SExplosionPacket(result.getEntity().blockPosition(), this.power, isNapalm));
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(6.0)).forEach(entity -> {
                entity.hurt(level().damageSources().explosion(null), this.power * 2.5f);
            });
            this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!isCluster) {
            PacketHandler.sendToAllClients(new CSmallExplosionParticlesPacket(result.getBlockPos()));
            PacketHandler.sendToServer(new SExplosionPacket(result.getBlockPos(), this.power, isNapalm));
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(6.0)).forEach(entity -> {
                entity.hurt(level().damageSources().explosion(null), this.power * 2.5f);
            });
            this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
        }
        this.discard();
    }

    @Override
    public void tick() {
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(6f));
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        if (!this.level().isClientSide() && isCluster) {
            BlockState blockBeneath = this.level().getBlockState(new BlockPos(this.getBlockX(), this.getBlockY() - 20, this.getBlockZ()));
            if (!blockBeneath.is(Blocks.AIR)) {
                for (int i = 0; i < 10; i++) {
                    spawnClusterBomb();
                }
                PacketHandler.sendToServer(new SExplosionPacket(this.blockPosition(), 3, false));
                this.discard();
            }
        }
        super.tick();
    }

    private void spawnClusterBomb() {
        float randomPosX = (Mth.randomBetween(this.level().getRandom(), -2.5f, 2.5f));
        float randomPosZ = (Mth.randomBetween(this.level().getRandom(), -2.5f, 2.5f));

        ClusterBombProjectileEntity explosive = new ClusterBombProjectileEntity(this.level(), 2);
        explosive.setPos(this.getX() + randomPosX, this.getY() - 2, this.getZ() - randomPosZ);
        this.level().addFreshEntity(explosive);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.DRAGON_FIREBALL_EXPLODE;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return false;
    }
}
