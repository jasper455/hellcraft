package net.team.helldivers.util;

import java.util.Map;
import java.util.Optional;

import org.checkerframework.checker.units.qual.h;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.util.Headshots.HeadHitbox;
import net.team.helldivers.util.Headshots.HeadHitboxRegistry;

public class ShootHelper {
      public static void shoot(LivingEntity shooter, Level level, int dist, double drift, float dam, double knockback, boolean ignoreIframes){
        Pair<HitResult, Vec3> pair = raycast(level, shooter, dist, drift);
        HitResult result = pair.getFirst();
        Vec3 hitPos = pair.getSecond();
        if(result.getType() == HitResult.Type.ENTITY){
            EntityHitResult resultE = ((EntityHitResult)result);
            Entity entity = resultE.getEntity();
            if(checkHeadShot(resultE, hitPos)){
                entity.hurt(entity.damageSources().generic(), dam*2);
                System.out.println("HEADSHOT");
                if(entity instanceof LivingEntity alive){
                    if(ignoreIframes) alive.invulnerableTime = 0;
                    Vec3 look = shooter.getLookAngle().normalize();
                    alive.knockback(knockback, -look.x, -look.z);
               }
            }
            else{
                entity.hurt(entity.damageSources().generic(), dam);
               if(entity instanceof LivingEntity alive){
                    if(ignoreIframes) alive.invulnerableTime = 0;
                    Vec3 look = shooter.getLookAngle().normalize();
                    alive.knockback(knockback, -look.x, -look.z);
               }
            }
        }
        if(result.getType() == HitResult.Type.BLOCK){
            BlockHitResult resultB = ((BlockHitResult)result);
            BlockPos pos = resultB.getBlockPos();
            BlockState block = Minecraft.getInstance().level.getBlockState(pos);
            if (block.is(BlockTags.IMPERMEABLE) || block.getBlock() instanceof IronBarsBlock) {
                level.destroyBlock(pos, false);
            }
            if (block.getBlock() instanceof BotContactMineBlock) {
                level.setBlockAndUpdate(resultB.getBlockPos(), Blocks.AIR.defaultBlockState());
                level.getEntitiesOfClass(LivingEntity.class, new AABB(resultB.getBlockPos()).inflate(3.0)).forEach(entity -> {
                    entity.hurt(level.damageSources().explosion(null), 12.5F);
                });
            }
            //add particles on hit
            if(!level.isClientSide){
                BlockParticleOption blockParticle = new BlockParticleOption(ParticleTypes.BLOCK, block);
                ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 0);
                for(int i=0;i<20;i++){
                    double rand1 = Math.random()*plusmin();
                    double rand2 = Math.random()*plusmin();          
                    double rand3 = Math.random()*plusmin();  
                    double rand4 = Math.random()*plusmin();  
                    ((ServerLevel)level).sendParticles(blockParticle, pos.getX(), pos.getY(), pos.getZ(), 2,rand1, rand2, rand3, rand4); 
                }
            }
        }
    }
    public static Pair<HitResult, Vec3> raycast(Level level, Entity shooter, double maxDistance, double drift) {
        double r1 = Math.random()*plusmin()*drift;
        double r2 = Math.random()*plusmin()*drift;          
        double r3 = Math.random()*plusmin()*drift; 
        Vec3 start = shooter.getEyePosition(1.0F);
        Vec3 look = shooter.getLookAngle().add(r1, r2, r3);
        Vec3 end = start.add(look.scale(maxDistance));
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, shooter));
        double blockDist = blockHit != null ? blockHit.getLocation().distanceTo(start) : maxDistance;
        AABB box = shooter.getBoundingBox().expandTowards(look.scale(maxDistance)).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, shooter, start, end, box, e -> !e.isSpectator() && e.isPickable());
        if (entityHit != null) {
            double entityDist = entityHit.getLocation().distanceTo(start);
            if (entityDist < blockDist) {
                Optional<Vec3> clipped = entityHit.getEntity().getBoundingBox().clip(start, end);//returning the correct coords
                Vec3 hitPos = clipped.orElse(end);
                if (!level.isClientSide) {
                 ((ServerLevel) level).sendParticles(ParticleTypes.CRIT, hitPos.x, hitPos.y, hitPos.z, 10, 0, 0, 0, 0);
                }
                return new Pair<>(entityHit, hitPos);
            }
        }
        return new Pair<HitResult,Vec3>(blockHit, blockHit.getLocation());
    }

    private static boolean checkHeadShot(EntityHitResult result, Vec3 pos) {
        Map<String, HeadHitbox> hitboxes = HeadHitboxRegistry.getAll();
        if (result.getEntity() instanceof EnderDragonPart part) {
            if("head".equals(part.getName().getString())){
                return true;
            }
        }
        else if (hitboxes != null) {
            Entity entity = result.getEntity();
            ResourceLocation id = EntityType.getKey(entity.getType());
            HeadHitbox headHitbox = hitboxes.get(id.toString());//TODO: add the rest of the entities to the HeadLocations json
            if (headHitbox != null) {
                AABB box = headHitbox.getBox(entity.getBoundingBox());
                box =rotateHeadBox(entity, box);
                if(box.contains(pos)) return true;
            }
        }
        return false;
    }
    public static AABB rotateHeadBox(Entity entity, AABB box){
        float headYaw = entity.getYHeadRot();
        double angle = Math.toRadians(-headYaw);
        Vec3 pivot = new Vec3(
            (box.minX + box.maxX) / 2.0,
            (box.minY + box.maxY) / 2.0,
            (box.minZ + box.maxZ) / 2.0
        );
        Vec3[] corners = new Vec3[] {
            new Vec3(box.minX, box.minY, box.minZ),
            new Vec3(box.minX, box.minY, box.maxZ),
            new Vec3(box.minX, box.maxY, box.minZ),
            new Vec3(box.minX, box.maxY, box.maxZ),
            new Vec3(box.maxX, box.minY, box.minZ),
            new Vec3(box.maxX, box.minY, box.maxZ),
            new Vec3(box.maxX, box.maxY, box.minZ),
            new Vec3(box.maxX, box.maxY, box.maxZ)
        };

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Vec3 corner : corners) {
            double dx = corner.x - pivot.x;
            double dz = corner.z - pivot.z;

            double rx = dx * cos - dz * sin;
            double rz = dx * sin + dz * cos;

            double x = pivot.x + rx;
            double y = corner.y;
            double z = pivot.z + rz;
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
        }
        if(entity instanceof AgeableMob mob){
            if(mob.isBaby()){
                minY = minY - 2;//TODO edit these
                maxY = maxY - 2;
            }
        }
        AABB end = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
        return end.inflate(0.03, 0, 0.03);
    }
    private static int plusmin(){
        if(Math.random()>0.5){
            return -1;
        }
        else{
            return 1;
        }
    }
}