package net.team.helldivers.network;

import net.team.helldivers.client.shader.post.tint.TintPostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;
import java.util.function.Supplier;

public class CSmallExplosionParticlesPacket {
    private final BlockPos position;

    public CSmallExplosionParticlesPacket(BlockPos position) {
        this.position = position;
    }

    public CSmallExplosionParticlesPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            if (!Minecraft.getInstance().player.getPersistentData().getBoolean("helldivers.useLodestone")) return;

            ClientLevel level = Minecraft.getInstance().level;

            triggerExplosionEffect(level, position.getCenter());
        });
        context.get().setPacketHandled(true);
    }

    public static void triggerExplosionEffect(Level level, Vec3 pos) {
        RandomSource random = level.getRandom();
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(20)
                .setEasing(Easing.SINE_IN_OUT)
                .setIntensity(5f));

        // Light Flash Particle (the cherry on top)
        WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
                .setScaleData(GenericParticleData.create(150f, 2.5f).build())
                .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                .setLifetime(120)
                .addMotion(0, 0, 0)
                .setForceSpawn(true)
                .spawn(level, pos.x, pos.y, pos.z);

        // Smoke
        for (int i = 0; i < 30; i++) {
            float angle = random.nextFloat() * ((float) Math.PI * 2);
            float speed = 0.1f + random.nextFloat() * 0.05f;
            float dx = Mth.cos(angle) * speed;
            float dz = Mth.sin(angle) * speed;
            float dy = 0.05f + random.nextFloat() * 0.05f;
//            for (int j = 0; j < 100; j++) {
//                WorldParticleBuilder.create(ModParticles.SMOKE)
//                        .setScaleData(GenericParticleData.create(5f, 20f).build())
//                        .setTransparencyData(GenericParticleData.create(0.5f, 0f).build())
//                        .setLifetime(700)
//                        .addMotion(dx / 5, dy / 1.5, dz / 4)
//                        .enableNoClip()
//                        .spawn(level, pos.x, pos.y, pos.z);
//            }

            // Blast particle outer
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(10f, 20f).build())
                    .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39, 200)).build())
                    .addMotion(dx / 1.5, dy, dz / 1.5)
                    .setLifetime(120)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);
            // Blast particle inner
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(5f, 10f).build())
                    .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                    .setLifetime(120)
                    .addMotion(dx / 2, dy, dz / 2)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);

            // Flash Particle
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(5f, 30f).build())
                    .setTransparencyData(GenericParticleData.create(0.05f, 0.02f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39)).build())
                    .setLifetime(120)
                    .addMotion(dx * 4, dy, dz * 4)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);

        }
//        ModClientEvents.triggerFlashEffect(0.00005f);
        TintPostProcessor.INSTANCE.setActive(false);
    }

}