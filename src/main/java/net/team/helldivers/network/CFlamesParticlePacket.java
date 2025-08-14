package net.team.helldivers.network;

import java.util.function.Supplier;

import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.worldgen.dimension.ModDimensions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CFlamesParticlePacket {
      private static final ParticleEmitterInfo FIRE = new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "flame_long"));
    public static Map<UUID, ParticleEmitterInfo> activeFlameEmitter = new HashMap<>();
    public CFlamesParticlePacket(){}
    public CFlamesParticlePacket(FriendlyByteBuf buffer){
        this();
    }
    public void encode(FriendlyByteBuf buffer) {
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(!player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)){
            ParticleEmitterInfo emitter =  FIRE.clone().bindOnEntity(player).useEntityHeadSpace().rotation(0, -90, 0);
            activeFlameEmitter.put(player.getUUID(), emitter);
            AAALevel.addParticle(player.level(), true, emitter);
            System.out.println("particles");
            context.get().setPacketHandled(true);
        }
    }

}
