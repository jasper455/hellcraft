package net.team.helldivers.helper;

import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SOrbitalBarragePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OrbitalBarrage {
    private final Level level;
    private final BlockPos pos;
    private final int radius;
    private int ticksRemaining;
    private int groundedTicks;
    private boolean is380Barrage;

    public OrbitalBarrage(Level level, BlockPos pos, int radius, int delayTicks, int groundedTicks, Entity entity, boolean is380Barrage) {
        this.level = level;
        this.pos = pos;
        this.radius = radius;
        this.ticksRemaining = delayTicks;
        this.groundedTicks = groundedTicks;
        this.is380Barrage = is380Barrage;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !this.level.isClientSide()) {
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                for (int i = 0; i < 600; i += 10) {
                    if (groundedTicks == 150 + (i * (is380Barrage ? 6 : 8))) {
                        // Trigger the barrage explosion
                        PacketHandler.sendToServer(new SOrbitalBarragePacket(pos, radius));
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            }
        }
    }
}