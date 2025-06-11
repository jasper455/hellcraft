package net.jasper.lodestonemod.entity.custom;

import net.jasper.lodestonemod.entity.ModEntities;
import net.jasper.lodestonemod.network.PacketHandler;
import net.jasper.lodestonemod.network.SSphereExplosionPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;

public class ExplosiveProjectileEntity extends AbstractArrow{
    public Vec2 groundedOffset;

    public ExplosiveProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ExplosiveProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.EXPLOSIVE_PROJECTILE.get(), shooter, level);
    }

    public boolean isGrounded() {
        return onGround();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
//        PacketHandler.sendToServer(new SSphereExplosionPacket(result.getBlockPos(), 30));
        this.kill();
    }

//    public static void triggerExplosionEffect(Level level, Vec3 pos) {
//        RandomSource random = level.getRandom();
//
//        // Smoke
//        for (int i = 0; i < 30; i++) {
//            float angle = random.nextFloat() * ((float) Math.PI * 2);
//            float speed = 0.1f + random.nextFloat() * 0.05f;
//            float dx = Mth.cos(angle) * speed;
//            float dz = Mth.sin(angle) * speed;
//            float dy = 0.05f + random.nextFloat() * 0.1f;
////            for (int j = 0; j < 100; j++) {
////                WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
////                        .setScaleData(GenericParticleData.create(5f, 20f).build())
////                        .setTransparencyData(GenericParticleData.create(0.5f, 0f).build())
////                        .setColorData(ColorParticleData.create(
////                                new Color(83, 64, 48),
////                                new Color(83, 64, 48)).build())
////                        .setLifetime(700)
////                        .addMotion(dx / 5, dy / 1.5, dz / 4)
////                        .enableNoClip()
////                        .spawn(level, pos.x, pos.y - 10, pos.z);
////            }
//
//            // Blast particle outer
//            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
//                    .setScaleData(GenericParticleData.create(20f, 40f).build())
//                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
//                    .setColorData(ColorParticleData.create(new Color(255, 216, 20), new Color(255, 70, 45, 200)).build())
//                    .addMotion(dx / 2, dy / 4, dz / 2)
//                    .setLifetime(600)
//                    .spawn(level, pos.x, pos.y - 10, pos.z);
//            // Blast particle inner
//            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
//                    .setScaleData(GenericParticleData.create(10f, 20f).build())
//                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
//                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
//                    .setLifetime(600)
//                    .addMotion(dx / 2.5f, dy / 5, dz / 2.5f)
//                    .spawn(level, pos.x, pos.y - 10, pos.z);
//
//            // Flash Particle
//            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
//                    .setScaleData(GenericParticleData.create(30f, 175f).build())
//                    .setTransparencyData(GenericParticleData.create(0.05f, 0.02f).build())
//                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39)).build())
//                    .setLifetime(600)
//                    .addMotion(dx * 2, dy / 5, dz * 2)
//                    .spawn(level, pos.x, pos.y - 10, pos.z);
//
//        }
//        // Light Flash Particle (yk what I mean)
//        WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
//                .setScaleData(GenericParticleData.create(250f, 2.5f).build())
//                .setTransparencyData(GenericParticleData.create(1f, 0f).build())
//                .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
//                .setLifetime(40)
//                .addMotion(0, 0, 0)
//                .spawn(level, pos.x, pos.y - 10, pos.z);
//
//        ModClientEvents.triggerFlashEffect(0.00075f);
//        TintPostProcessor.INSTANCE.setActive(false);
//    }


    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}
