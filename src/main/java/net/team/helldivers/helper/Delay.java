package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.team.helldivers.network.CLargeExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SSphereExplosionPacket;
import net.team.helldivers.sound.ModSounds;

public class Delay {
    private int delayTicks;
    private int ticksRemaining = delayTicks;
    private boolean isDelayFinished;

    public Delay(int delayTicks) {
        this.delayTicks = delayTicks;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                isDelayFinished = true;
                ticksRemaining = delayTicks;
            } else {
                isDelayFinished = false;
            }
        }
    }

    public boolean isDelayFinished() {
        return isDelayFinished;
    }
}