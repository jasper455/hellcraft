package net.team.helldivers.entity.custom;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.sound.ModSounds;

public class SmallGrenadeEntity extends AbstractArrow{
    private int maxLife = 1000;
    private int lifetime = 0;
    public int strength = 1;
    private boolean hasParticle;
    public SmallGrenadeEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public SmallGrenadeEntity(LivingEntity shooter, Level level, int strength) {
        super(ModEntities.SMALL_GRENADE.get(), shooter, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity() instanceof LivingEntity alive){
            alive.hurt( alive.damageSources().generic(), strength+2);
        }
        BlockPos pos = new BlockPos(((int)result.getLocation().x), ((int)result.getLocation().y), ((int)result.getLocation().z));
        PacketHandler.sendToServer(new SExplosionPacket(pos, 1, false));
        this.playSound(ModSounds.EXPLOSION.get(), 5.0f, 1.0f); 
    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
        PacketHandler.sendToServer(new SExplosionPacket(result.getBlockPos(), 1, false));
        this.playSound(ModSounds.EXPLOSION.get(), 5.0f, 1.0f);
        this.discard();
    }
    @Override
    public void tick() {
        if(!hasParticle){
            ParticleEmitterInfo trail = EffekLoader.GRENADE.clone().bindOnEntity(this);
            AAALevel.addParticle(this.level(), true, trail);
            hasParticle = true;
        }
        super.tick();
        lifetime++;
        if (lifetime >= maxLife) {
            BlockPos pos = new BlockPos(((int)this.position().x), ((int)position().y), ((int)position().z));
            this.onHitBlock(new BlockHitResult(this.position(), getDirection(), pos, false));
        }
    }
     @Override
    protected float getWaterInertia() {
        return 0.8F; // friction in water
    }
     @Override
     protected ItemStack getPickupItem() {
        return Items.AIR.getDefaultInstance();
     }
    
}
