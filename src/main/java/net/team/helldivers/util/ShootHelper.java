package net.team.helldivers.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.team.helldivers.damage.ModDamageSources;
import net.team.helldivers.entity.custom.bots.AbstractBotEntity;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;
import net.team.helldivers.particle.ModParticles;
import org.checkerframework.checker.units.qual.h;

import com.mojang.datafixers.util.Pair;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import mod.chloeprime.aaaparticles.client.registry.EffectDefinition;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.network.CApplyRecoilPacket;
import net.team.helldivers.network.CHitMarkPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.util.Headshots.HeadHitbox;
import net.team.helldivers.util.Headshots.HeadHitboxRegistry;

public class ShootHelper {

      public static void shoot(LivingEntity shooter, Level level, double drift, float dam, double knockback, boolean ignoreIframes){
        Pair<HitResult, Vec3> pair = raycast(level, shooter, drift, true);
        HitResult result = pair.getFirst();
        Vec3 hitPos = pair.getSecond();
        ParticleEmitterInfo hit = EffekLoader.HIT.clone().position(hitPos).scale(0.1f);//.parameter(0, dist/2);
        AAALevel.addParticle(shooter.level(), true, hit);
        if(result.getType() == HitResult.Type.ENTITY){
            EntityHitResult resultE = ((EntityHitResult)result);
            Entity entity = resultE.getEntity();
            if (!level.isClientSide) {
                if(shooter instanceof ServerPlayer player){
                    PacketHandler.sendToPlayer(new CHitMarkPacket(), player);
                }
                 ((ServerLevel) level).sendParticles(ParticleTypes.CRIT, hitPos.x, hitPos.y, hitPos.z, 10, 0, 0, 0, 0);
            }
            /*if(checkHeadShot(resultE, hitPos)){
                entity.hurt(ModDamageSources.raycast(shooter), dam*2);
                System.out.println("HEADSHOT");
                if(entity instanceof LivingEntity alive){
                    if(ignoreIframes) alive.invulnerableTime = 0;
                    Vec3 look = shooter.getLookAngle().normalize();
                    alive.knockback(knockback, -look.x, -look.z);
               }
            }*/ //TODO fix this stuff... the obb is just not working and if we are going to upload this I would rather have a fully working headshot system then a buggy mess
            //else{
                entity.hurt(entity.damageSources().generic(), dam);
               if(entity instanceof LivingEntity alive){
                    if(ignoreIframes) alive.invulnerableTime = 0;
                    Vec3 look = shooter.getLookAngle().normalize();
                    alive.knockback(knockback, -look.x, -look.z);
               }
            //}
        }
        if(result.getType() == HitResult.Type.BLOCK){
            BlockHitResult resultB = ((BlockHitResult)result);
            BlockPos pos = resultB.getBlockPos();
            BlockState block = level.getBlockState(pos);
            if (block.getBlock() instanceof BotContactMineBlock) {
            shooter.level().setBlockAndUpdate(resultB.getBlockPos(), Blocks.AIR.defaultBlockState());
            shooter.level().getEntitiesOfClass(LivingEntity.class, new AABB(resultB.getBlockPos()).inflate(3.0)).forEach(entity -> {
                entity.hurt(shooter.level().damageSources().explosion(null), 12.5F);
            });
        }
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
    public static Pair<HitResult, Vec3> raycast(Level level, Entity shooter, double drift, boolean spawnParticles) {//TODO sync the hit and shoot particles
        double r1 = Math.random()*plusmin()*drift;
        double r2 = Math.random()*plusmin()*drift;          
        double r3 = Math.random()*plusmin()*drift; 
        Vec3 start = shooter.getEyePosition(1.0F);
        Vec3 look = shooter.getLookAngle().add(r1, r2, r3);
        Vec3 end = start.add(look.scale(128));
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, shooter));
        double blockDist = blockHit != null ? blockHit.getLocation().distanceTo(start) : 128;
        AABB box = shooter.getBoundingBox().expandTowards(look.scale(128)).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, shooter, start, end, box, e -> !e.isSpectator() && e.isPickable());
        if(spawnParticles){
             HitResult result = blockHit != null ? blockHit : entityHit;
            float dist = ((float)result.distanceTo(shooter));
            if(result.getType() == HitResult.Type.MISS ){
            dist = 50f;
            }
            float yaw = (float)(Math.atan2(look.z, look.x)-Math.PI/2);
            float pitch = (float)(Math.asin(look.y));
             ParticleEmitterInfo trail = EffekLoader.TRAIL.clone().parameter(0, (dist/2)-12).position(shooter.getEyePosition().add(0, -0.1, 0)).rotation(-pitch, -yaw, 0);
             AAALevel.addParticle(shooter.level(), true, trail);

        }
        if (entityHit != null) {
            double entityDist = entityHit.getLocation().distanceTo(start);
            if (entityDist < blockDist) {
                Optional<Vec3> clipped = entityHit.getEntity().getBoundingBox().clip(start, end);//returning the correct coords
                Vec3 hitPos = clipped.orElse(end);
                return new Pair<>(entityHit, hitPos);
            }
        }
        return new Pair<HitResult,Vec3>(blockHit, blockHit.getLocation());
    }

    private static boolean checkHeadShot(EntityHitResult result, Vec3 pos) {
        Map<String, HeadHitbox> hitboxes = HeadHitboxRegistry.getAll();

        if (result.getEntity() instanceof EnderDragonPart part) {
            if ("head".equals(part.getName().getString())) {
                return true;
            }
        } else if (hitboxes != null) {
            Entity entity = result.getEntity();
            ResourceLocation id = EntityType.getKey(entity.getType());

            HeadHitbox headHitbox = hitboxes.get(id.toString());
            if (headHitbox == null) {
                System.out.println("No head hitbox registered for: " + id);
                return false;
            }

            AABB box = headHitbox.getBox(entity.getBoundingBox());
            if (!headHitbox.isVert()) {
                OBB rotated = rotateHeadOBB(entity, box);
                if (rotated.contains(pos)) return true;
            } else {
                AABB rotated = rotateAABB(box, entity);
                rotated = rotated.expandTowards(0.3, 0, 0.3);
                if (entity instanceof AgeableMob mob && mob.isBaby()) {
                    rotated = rotated.move(0, -3, 0);
                }
                if (rotated.contains(pos)) return true;
            }
        }

        return false;
    }

    public static OBB rotateHeadOBB(Entity entity, AABB box) {
        OBB rotated = new OBB(box);
        rotated.rotateYaw(-entity.getYRot(), entity.getBoundingBox().getCenter());
        if (entity instanceof AgeableMob mob && mob.isBaby()) {
            rotated.moveY(-3);
        }
        return rotated;
    }
    public  static AABB rotateAABB(AABB box, Entity entity){
        float headYaw = entity.getYRot();
        double angle = Math.toRadians(-headYaw);
        Vec3 pivot = entity.position();
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
        AABB end = new AABB(minX, minY, minZ, maxX, maxY, maxZ).inflate(0.3, 0, 0.3);
        if(entity instanceof AgeableMob mob){
            if(mob.isBaby()){
                end.move(0, -2, 0);
            }
        }
        return end;
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