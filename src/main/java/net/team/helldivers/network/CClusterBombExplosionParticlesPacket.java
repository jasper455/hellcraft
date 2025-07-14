package net.team.helldivers.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.client.shader.post.tint.TintPostProcessor;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;
import java.util.function.Supplier;

public class CClusterBombExplosionParticlesPacket {
    private final BlockPos position;

    public CClusterBombExplosionParticlesPacket(BlockPos position) {
        this.position = position;
    }

    public CClusterBombExplosionParticlesPacket(FriendlyByteBuf buffer) {
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

        // Smoke
        for (int i = 0; i < 30; i++) {
            float angle = random.nextFloat() * ((float) Math.PI * 2);
            float speed = 0.1f + random.nextFloat() * 0.25f;
            float dx = Mth.cos(angle) * speed;
            float dz = Mth.sin(angle) * speed;
            float dy = 0.05f + random.nextFloat() * 0.25f;

            // Blast particle outer
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(1f, 0.5f).build())
                    .setTransparencyData(GenericParticleData.create(0.15f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                    .addMotion(dx, dy, dz)
                    .setLifetime(60)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);
            // Blast particle inner
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(1f, 0.5f).build())
                    .setTransparencyData(GenericParticleData.create(0.15f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                    .setLifetime(60)
                    .addMotion(dx, dy, dz)
                    .setForceSpawn(true)
                    .spawn(level, pos.x, pos.y, pos.z);


        }
//        ModClientEvents.triggerFlashEffect(0.00005f);
        TintPostProcessor.INSTANCE.setActive(false);
    }

}