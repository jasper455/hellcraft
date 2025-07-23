package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
    private final int groundedTicks;
    private final boolean is380Barrage;
    private final boolean isNapalmBarrage;
    private boolean isWalkingBarrage;
    private int walkingDirection;

    public OrbitalBarrage(Level level, BlockPos pos, int radius, int delayTicks, int groundedTicks,
                          boolean is380Barrage, boolean isNapalmBarrage) {
        this.level = level;
        this.pos = pos;
        this.radius = radius;
        this.ticksRemaining = delayTicks;
        this.groundedTicks = groundedTicks;
        this.is380Barrage = is380Barrage;
        this.isNapalmBarrage = isNapalmBarrage;
    }

    public OrbitalBarrage(Level level, BlockPos pos, int radius, int delayTicks, int groundedTicks,
                          boolean is380Barrage, boolean isNapalmBarrage, boolean isWalkingBarrage,
                          int walkingDirection) {
        this.level = level;
        this.pos = pos;
        this.radius = radius;
        this.ticksRemaining = delayTicks;
        this.groundedTicks = groundedTicks;
        this.is380Barrage = is380Barrage;
        this.isNapalmBarrage = isNapalmBarrage;
        this.isWalkingBarrage = isWalkingBarrage;
        this.walkingDirection = walkingDirection;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !this.level.isClientSide()) {
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                for (int i = 0; i < 600; i += 10) {
                    if (groundedTicks == 150 + (i * (is380Barrage ? 6 : 8))) {
                        // Trigger the barrage explosion
                        if (!isWalkingBarrage) {
                            PacketHandler.sendToServer(new SOrbitalBarragePacket(pos, radius, isNapalmBarrage, false, walkingDirection, groundedTicks));
                        } else {
                            PacketHandler.sendToServer(new SOrbitalBarragePacket(pos, radius, isNapalmBarrage, true, walkingDirection, groundedTicks));
                        }
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            }
        }
    }
}