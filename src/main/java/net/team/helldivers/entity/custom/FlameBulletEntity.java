package net.team.helldivers.entity.custom;

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

public class FlameBulletEntity extends AbstractArrow{
    private int maxLife = 15;
    private int lifetime = 0;
    public FlameBulletEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public FlameBulletEntity(LivingEntity shooter, Level level) {
        super(ModEntities.FIRE_BULLET.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity() instanceof LivingEntity alive){
            alive.setSecondsOnFire(5);
            alive.hurt( alive.damageSources().inFire(), 2);
        }
        this.discard();
    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = this.level();
        BlockPos hitPos = result.getBlockPos();
        Direction hitFace = result.getDirection();
        BlockPos firePos = hitPos.relative(hitFace);
        if (level.isEmptyBlock(firePos)) {
            level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 11);
        }
        this.discard();
    }
    @Override
    public void tick() {
        super.tick();
        lifetime++;
        if (lifetime >= maxLife) {
            this.discard();
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
