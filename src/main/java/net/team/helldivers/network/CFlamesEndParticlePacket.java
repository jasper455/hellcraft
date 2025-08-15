package net.team.helldivers.network;

import java.util.function.Supplier;

import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter.Type;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.HelldiversMod;
import java.util.Map;
import java.util.UUID;

public class CFlamesEndParticlePacket {
    public CFlamesEndParticlePacket(){}
    public CFlamesEndParticlePacket(FriendlyByteBuf buffer){
        this();
    }
    public void encode(FriendlyByteBuf buffer) {
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            ParticleEmitterInfo info = CFlamesParticlePacket.activeFlameEmitter.remove(player.getUUID());// removes from map
            if (info != null) {
                AAALevel.sendTriggerFor(player, Type.WORLD, info.effek, info.emitter, new int[]{0});
                System.out.println("particle end");
            }
        });
        context.get().setPacketHandled(true);
    }
}
