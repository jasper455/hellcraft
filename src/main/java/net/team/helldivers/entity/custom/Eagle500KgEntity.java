package net.team.helldivers.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.CLargeExplosionParticlesPacket;
import net.team.helldivers.network.CSmallExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.sound.ModSounds;

public class Eagle500KgEntity extends AbstractArrow {
    private int soundTicks = 0;
    private int groundedTicks = 0;

    public Eagle500KgEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Eagle500KgEntity(LivingEntity shooter, Level level) {
        super(ModEntities.EAGLE_500KG_BOMB.get(), shooter, level);
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        PacketHandler.sendToAllClients(new CSmallExplosionParticlesPacket(result.getEntity().blockPosition()));
        PacketHandler.sendToServer(new SExplosionPacket(result.getEntity().blockPosition(), 10));
        this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(6.0)).forEach(entity -> {
            entity.hurt(level().damageSources().explosion(null), 30.0F);
        });
        this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }

        if (this.isGrounded()) {
            groundedTicks++;
        }

        if (groundedTicks >= 20) {
            PacketHandler.sendToAllClients(new CLargeExplosionParticlesPacket(this.getOnPos()));
            PacketHandler.sendToServer(new SExplosionPacket(this.getOnPos(), 20));
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(6.0)).forEach(entity -> {
                entity.hurt(level().damageSources().explosion(null), 30.0F);
            });
            this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
            this.discard();
        }
        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.DRAGON_FIREBALL_EXPLODE;
    }
}
