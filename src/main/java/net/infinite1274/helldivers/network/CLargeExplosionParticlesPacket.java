package net.infinite1274.helldivers.network;

import net.infinite1274.helldivers.client.shader.post.tint.TintPostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;
import java.util.function.Supplier;

public class CLargeExplosionParticlesPacket {
    private final BlockPos position;

    public CLargeExplosionParticlesPacket(BlockPos position) {
        this.position = position;
    }

    public CLargeExplosionParticlesPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;

            ClientLevel level = Minecraft.getInstance().level;

//            triggerExplosionEffect(level, position.getCenter());
        });
        context.get().setPacketHandled(true);
    }

    public static void triggerExplosionEffect(Level level, Vec3 pos) {
        RandomSource random = level.getRandom();

        // Light Flash Particle (the cherry on top)
        WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
                .setScaleData(GenericParticleData.create(150f, 2.5f).build())
                .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                .setLifetime(20)
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

            // Blast particle outer
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(25f, 50f).build())
                    .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39, 200)).build())
                    .addMotion(dx / 1.5, dy, dz / 1.5)
                    .setLifetime(300)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);
            // Blast particle inner
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(12.5f, 25f).build())
                    .setTransparencyData(GenericParticleData.create(0.25f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                    .setLifetime(300)
                    .addMotion(dx / 2, dy, dz / 2)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);

            // Flash Particle
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(15f, 87.5f).build())
                    .setTransparencyData(GenericParticleData.create(0.05f, 0.02f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39)).build())
                    .setLifetime(300)
                    .addMotion(dx * 4, dy, dz * 4)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);

        }

//        ModClientEvents.triggerFlashEffect(0.00005f);
        TintPostProcessor.INSTANCE.setActive(false);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(level.isClientSide())));
    }

}