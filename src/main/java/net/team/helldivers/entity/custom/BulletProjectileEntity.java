package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.entity.ModEntities;

public class BulletProjectileEntity extends AbstractArrow {
    public Vec2 groundedOffset;
    private Vec3 previousPos;
    private int lifetime = 0;
    private boolean isShotgun;
    private boolean isAmr;

    public BulletProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BulletProjectileEntity(LivingEntity shooter, Level level, boolean isShotgun, boolean isAmr) {
        super(ModEntities.BULLET.get(), shooter, level);
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());
        this.isShotgun = isShotgun;
        this.isAmr = isAmr;
    }


    @Override
    protected ItemStack getPickupItem() {
        return Items.AIR.getDefaultInstance();
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().arrow(this, this.getOwner()), 3);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        BlockState block = Minecraft.getInstance().level.getBlockState(pos);
        if (block.is(BlockTags.IMPERMEABLE) || block.getBlock() instanceof IronBarsBlock) {
            this.level().destroyBlock(pos, false);
        }
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
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());

        super.tick();
        lifetime++;
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(this.isAmr ? 10 : 5));
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
                            Vec3.fromRGB24(0x000000).toVector3f(), 0.5F);

                    this.level().addParticle(dustParticle,
                            pos.x, pos.y, pos.z,
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (lifetime >= 40) {
            this.discard();
        } else if (this.isShotgun && lifetime == 10) {
            this.discard();
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.BONE_BLOCK_HIT;
    }
}