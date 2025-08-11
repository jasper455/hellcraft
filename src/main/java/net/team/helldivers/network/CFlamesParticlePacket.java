package net.team.helldivers.network;

import java.util.function.Supplier;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.HelldiversMod;

public class CFlamesParticlePacket {
      private static final ParticleEmitterInfo FIRE = new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "flame_line"));
    public CFlamesParticlePacket(){}
    public CFlamesParticlePacket(FriendlyByteBuf buffer){
        this();
    }
     public void encode(FriendlyByteBuf buffer) {
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        LocalPlayer player = Minecraft.getInstance().player;
       // ParticleEmitterInfo part = new ParticleEmitterInfo(LOC);
        ParticleEmitterInfo emitter = FIRE.clone().position(player.getEyePosition());
            AAALevel.addParticle(player.level(), true, emitter);
        System.out.println("particles");
        context.get().setPacketHandled(true);
    }

}
