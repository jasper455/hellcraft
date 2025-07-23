package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.sound.ModSounds;

public class RocketProjectileEntity extends AbstractArrow {
    private int lifetime = 0;
    private Vec3 previousPos;

    public RocketProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RocketProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.ROCKET.get(), shooter, level);
    }

    public boolean isGrounded() {
        return onGround();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        PacketHandler.sendToServer(new SExplosionPacket(result.getEntity().blockPosition(), 3, false));
        this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        BlockState block = Minecraft.getInstance().level.getBlockState(pos);
        PacketHandler.sendToServer(new SExplosionPacket(result.getBlockPos(), 3, false));
        this.playSound(ModSounds.EXPLOSION.get(), 10.0f, 1.0f);
        if (block.getBlock() instanceof BotContactMineBlock) {
            this.level().setBlockAndUpdate(result.getBlockPos(), Blocks.AIR.defaultBlockState());
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(result.getBlockPos()).inflate(3.0)).forEach(entity -> {
                entity.hurt(this.level().damageSources().explosion(null), 12.5F);
            });
        }
        this.discard();
    }

    @Override
    public void tick() {
        if (lifetime == 0 && this.getOwner() != null) {
            this.setXRot(this.getOwner().getXRot());
            this.setYRot(this.getOwner().getYRot());
        }
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());
        super.tick();
        lifetime++;
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(3f));
        if (this.level().isClientSide) {
            Entity owner = this.getOwner();
            if (owner instanceof Player player) {
                // Create particles along the path
                Vec3 current = new Vec3(this.getX(), this.getY(), this.getZ());
                Vec3 direction = current.subtract(previousPos);
                int particleCount = 5; // Adjust based on speed

                for (int i = 0; i < particleCount; i++) {
                    double factor = i / (double) particleCount;
                    Vec3 pos = previousPos.add(direction.scale(factor));

                    DustParticleOptions dustParticle = new DustParticleOptions(
                            Vec3.fromRGB24(0x000000).toVector3f(), 5F);

                    this.level().addParticle(dustParticle,
                            pos.x, pos.y, pos.z,
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (lifetime >= 400) {
            this.discard();
            return;
        }
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
