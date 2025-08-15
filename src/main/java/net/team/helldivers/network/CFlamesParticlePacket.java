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
                float partialTicks = Minecraft.getInstance().getFrameTime();
                float rotY = (float) Math.toRadians(player.getViewYRot(partialTicks));
                float rotX = (float) Math.toRadians(player.getViewXRot(partialTicks));
                Vec2 dir = new Vec2(rotX, -rotY);
                ParticleEmitterInfo fire = EffekLoader.FIRE.clone().rotation(dir).position(player.getEyePosition().add(0, -0.3, 0)).rotation(dir);
                AAALevel.addParticle(player.level(), true, fire);
                System.out.println("rotx " + rotX);
                System.out.println("roty " + rotY);
                //extend bullet entity and make a new entity that lasts for the duration of the flames animation. spawn like 10 per tick
            }
        });
        context.get().setPacketHandled(true);
    }
}
