package net.team.helldivers.entity.custom;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.damage.ModDamageSources;
import net.team.helldivers.damage.ModDamageTypes;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.CHitMarkPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.worldgen.dimension.ModDimensions;

public class HeatedGasProjectileEntity extends AbstractArrow {
    public Vec2 groundedOffset;
    private Vec3 previousPos;
    private int lifetime = 0;
    private boolean hasParticle;

    public HeatedGasProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HeatedGasProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.HEATED_GAS.get(), shooter, level);
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());
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
        ParticleEmitterInfo hit = EffekLoader.PLASMA_HIT.clone().position(this.position());
        AAALevel.addParticle(this.level(), true, hit);
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        BlockPos pos = entity.blockPosition();
        entity.hurt(this.damageSources().arrow(this, this.getOwner()), 30);
        if (!this.level().isClientSide && this.getOwner() instanceof ServerPlayer player) {
                    PacketHandler.sendToPlayer(new CHitMarkPacket(), player);
        }
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
        }
        //this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
               // pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
        this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(1.5)).forEach(entity1 -> {
            entity1.hurt(level().damageSources().explosion(null), 20.0F);
             if (!this.level().isClientSide && this.getOwner() instanceof ServerPlayer player && entity1 !=null) {
                PacketHandler.sendToPlayer(new CHitMarkPacket(), player);
            }
            if (this.getOwner() != null) {
                entity1.hurt(ModDamageSources.raycast(this.getOwner()), 20.0F);
            }
        });
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
        ParticleEmitterInfo hit = EffekLoader.PLASMA_HIT.clone().position(this.position());
        AAALevel.addParticle(this.level(), true, hit);
       // this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
              //  pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
        this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(1.5)).forEach(entity -> {
            entity.hurt(level().damageSources().explosion(null), 10.0F);
             if (!this.level().isClientSide && this.getOwner() instanceof ServerPlayer player && entity !=null) {
                PacketHandler.sendToPlayer(new CHitMarkPacket(), player);
            }
            if (this.getOwner() != null) {
                entity.hurt(ModDamageSources.raycast(this.getOwner()), 10.0F);
            }
        });
        if (block.getBlock() instanceof BotContactMineBlock) {
            this.level().setBlockAndUpdate(result.getBlockPos(), Blocks.AIR.defaultBlockState());
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(result.getBlockPos()).inflate(3.0)).forEach(entity -> {
                if (this.getOwner() != null) {
                    entity.hurt(ModDamageSources.raycast(this.getOwner()), 12.5F);
                }
            });
        }
        this.discard();
    }

    @Override
    public void tick() {
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(5f));
        if(!hasParticle){
            ParticleEmitterInfo trail = EffekLoader.PLASMA.clone().bindOnEntity(this);
            AAALevel.addParticle(this.level(), true, trail);
            hasParticle = true;
        }
        this.previousPos = new Vec3(this.getX(), this.getY(), this.getZ());

        if (this.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) {
            this.discard();
        }

        super.tick();
        lifetime++;
        if (this.level().isClientSide) {
            Entity owner = this.getOwner();
            /*if (owner instanceof Player player) {
                // Create particles along the path
                Vec3 current = new Vec3(this.getX(), this.getY(), this.getZ());
                Vec3 direction = current.subtract(previousPos);
                int particleCount = 5; // Adjust based on speed

                for (int i = 0; i < particleCount; i++) {
                    double factor = i / (double) particleCount;
                    Vec3 pos = previousPos.add(direction.scale(factor));

                    DustParticleOptions dustParticle = new DustParticleOptions(
                            Vec3.fromRGB24(0x05f3ff).toVector3f(), 0.5F);

                    this.level().addParticle(dustParticle,
                            pos.x, pos.y, pos.z,
                            0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.ENCHANTED_HIT,
                            pos.x, pos.y, pos.z, 0, 0, 0);
                }
            }*/
        }
        if (lifetime >= 200) {
            this.discard();
            return;
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.CALCITE_HIT;
    }
}