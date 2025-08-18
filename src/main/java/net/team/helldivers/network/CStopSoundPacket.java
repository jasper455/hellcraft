package net.team.helldivers.network;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.worldgen.dimension.ModDimensions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CStopSoundPacket {
    private final ResourceLocation soundEventId;
    public CStopSoundPacket(ResourceLocation soundEvent) {
        this.soundEventId = ForgeRegistries.SOUND_EVENTS.getKey(ForgeRegistries.SOUND_EVENTS.getValue(soundEvent));
    }

    public CStopSoundPacket(FriendlyByteBuf buffer){
        this(buffer.readResourceLocation());
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(soundEventId);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            SoundManager soundManager = mc.getSoundManager();

            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundEventId);

            if (soundEvent != null) {
                // Stop all instances of this sound
                Collection<ResourceLocation> playingSounds = soundManager.getAvailableSounds(); // internal, fragile

                for (ResourceLocation instance : playingSounds) {
                    if (instance.equals(soundEventId)) {
                        soundManager.stop(instance, SoundSource.AMBIENT);
                    }
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
