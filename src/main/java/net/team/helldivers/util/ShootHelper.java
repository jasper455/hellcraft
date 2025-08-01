package net.team.helldivers.util;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
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
      public static void shoot(LivingEntity shooter, Level level, boolean isShotgun, boolean isAmr){
        int dist = 128;
        double drift = 0.03;
        float dam = 5;
        double knockback = 0.3f;
        if(isShotgun) {
          dist = 28;
          drift = 0.2;
          dam = 1;
          knockback = 0.5f;

        }
        else  if(isAmr){
           dist = 256;
           drift = 0;
           dam = 10;
           knockback = 0.5f;
        }
        HitResult result = raycast(level, shooter, dist, drift);
        System.out.print(result);
        if(result.getType() == HitResult.Type.ENTITY){
            EntityHitResult resultE = ((EntityHitResult)result);
            Entity entity = resultE.getEntity();
            if(checkHeadShot(resultE)){
                entity.hurt(entity.damageSources().generic(), dam*0.6f);
                if(entity instanceof LivingEntity alive){
                    if(isShotgun) alive.invulnerableTime = 0;
                    Vec3 look = shooter.getLookAngle().normalize();
                    alive.knockback(knockback, -look.x, -look.z);
               }
            }
            else{
                entity.hurt(entity.damageSources().generic(), dam);
               if(entity instanceof LivingEntity alive){
                    if(isShotgun) alive.invulnerableTime = 0;
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
                for(int i=0;i<20;i++){
                    double rand1 = Math.random()*plusmin();
                    double rand2 = Math.random()*plusmin();          
                    double rand3 = Math.random()*plusmin();  
                    double rand4 = Math.random()*plusmin();  
                    ((ServerLevel)level).sendParticles(blockParticle,pos.getX(), pos.getY(), pos.getZ(), 2,rand1, rand2, rand3, rand4);  
                }
            }
        }
    }
    public static HitResult raycast(Level level, Entity shooter, double maxDistance, double drift) {
        double r1 = Math.random()*plusmin()*drift;
        double r2 = Math.random()*plusmin()*drift;          
        double r3 = Math.random()*plusmin()*drift; 
        Vec3 start = shooter.getEyePosition(1.0F);
        Vec3 look = shooter.getLookAngle().add(r1, r2, r3);
        Vec3 end = start.add(look.scale(maxDistance));
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, shooter));
        Vec3 blockHitPos = blockHit != null ? blockHit.getLocation() : end;
        double blockDist = blockHit != null ? blockHit.getLocation().distanceTo(start) : maxDistance;
        AABB box = shooter.getBoundingBox().expandTowards(look.scale(maxDistance)).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, shooter, start, end, box, e -> !e.isSpectator() && e.isPickable());

        if (entityHit != null) {
            double entityDist = entityHit.getLocation().distanceTo(start);
            if (entityDist < blockDist) {
                return entityHit;
            }
        }

        return blockHit;
}

    private static boolean checkHeadShot(EntityHitResult result) {
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
                System.out.println("Checking box: " + box);
                if(box.contains(result.getLocation())) return true;
                System.out.println("entity hit but not headshot");
            }
            System.out.println("entity not found");
        }
        return false;
    }
    private static AABB rotateHeadBox(Entity entity, AABB box){//TODO add head rotation logic here
        return box;
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
