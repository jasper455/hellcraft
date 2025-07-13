package net.team.helldivers.helper;

import net.team.helldivers.network.CLargeExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SSphereExplosionPacket;
import net.team.helldivers.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DelayedExplosion {
    private final Level level;
    private final BlockPos pos;
    private final int radius;
    private int ticksRemaining;
    private boolean hasPlayedSound = false; // Add this field

    public DelayedExplosion(Level level, BlockPos pos, int radius, int delayTicks) {
        this.level = level;
        this.pos = pos;
        this.radius = radius;
        this.ticksRemaining = delayTicks;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        System.out.println("test");
        if (event.phase == TickEvent.Phase.END && !this.level.isClientSide()) {
            if (!hasPlayedSound) {
                level.playSound(null, pos, ModSounds.HELLBOMB_ARMED.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                level.playSound(null, pos, ModSounds.CLEAR_THE_AREA.get(), SoundSource.PLAYERS, 10000000.0f, 1.0f);
                hasPlayedSound = true;
            }
            
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                PacketHandler.sendToServer(new SSphereExplosionPacket(pos, radius));
                PacketHandler.sendToAllClients(new CLargeExplosionParticlesPacket(new BlockPos(pos.getX(), pos.getY() - 10, pos.getZ())));
                level.playSound(null, pos, ModSounds.HELLBOMB_EXPLOSION.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}