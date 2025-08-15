package net.team.helldivers.network;

import java.util.function.Supplier;

import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.worldgen.dimension.ModDimensions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CFlamesParticlePacket {
    public static Map<UUID, ParticleEmitterInfo> activeFlameEmitter = new HashMap<>();
    public CFlamesParticlePacket(){}
    public CFlamesParticlePacket(FriendlyByteBuf buffer){
        this();
    }
    public void encode(FriendlyByteBuf buffer) {
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if(!player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)){
                //ParticleEmitterInfo emitter = newEmitterForPlayer(player);
                //activeFlameEmitter.put(player.getUUID(), emitter);
                float yaw = -player.getYRot();
                float pitch = -player.getXRot();
                Vec2 dir = new Vec2(pitch, yaw);

                AAALevel.addParticle(player.level(), true, EffekLoader.FIRE.clone().rotation(dir).position(player.getEyePosition()));
                System.out.println("particles start");
            }
        });
        context.get().setPacketHandled(true);
    }
}
