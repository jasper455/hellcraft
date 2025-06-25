package net.infinite1274.helldivers.network;

import net.infinite1274.helldivers.entity.custom.MissileProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

public class SOrbitalBarragePacket {
    private final BlockPos position;
    private final int radius;
    private int missileCount = 0;
    private int tickDelay = 20; // 20 ticks = 1 second
    private boolean isWalking;
    private Direction direction;

    public SOrbitalBarragePacket(BlockPos position, int radius) {
        this.position = position;
        this.radius = radius;
    }
    public SOrbitalBarragePacket(BlockPos position, int radius, boolean isWalking, Direction direction) {
        this.position = position;
        this.radius = radius;
        this.isWalking = isWalking;
        this.direction = direction;
    }

    public SOrbitalBarragePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
        buffer.writeInt(this.radius);
    }

    public static class BarrageSpawner {
        private final ServerPlayer player;
        private final BlockPos position;
        private final int radius;
        private int missileCount = 0;
        private int tickDelay = 20;
        private boolean isWalking;
        private Direction walkingDirection;

        public BarrageSpawner(ServerPlayer player, BlockPos position, int radius, boolean isWalking, Direction direction) {
            this.player = player;
            this.position = position;
            this.radius = radius;
            this.isWalking = isWalking;
            this.walkingDirection = direction;
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                if (tickDelay <= 0) {
                    if (missileCount < 3) {
                        spawnMissile();
                        missileCount++;
                        tickDelay = 20; // Reset delay for next missile
                    } else {
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
                tickDelay--;
            }
        }

        private void spawnMissile() {
            float randomPosX = (Mth.randomBetween(player.level().getRandom(), radius * -1, radius));
            float randomPosZ = (Mth.randomBetween(player.level().getRandom(), radius * -1, radius));
            float walkingZ = (Mth.randomBetween(player.level().getRandom(), 0, 100));
            float walkingX = (Mth.randomBetween(player.level().getRandom(), 0, 100));

            MissileProjectileEntity explosive = new MissileProjectileEntity(player, player.level());
            if (isWalking) {
                if (walkingDirection == Direction.NORTH) {
                    explosive.setPos(position.getX() + randomPosX, 200, position.getZ() - walkingZ * 1.5f);
                }
                if (walkingDirection == Direction.SOUTH) {
                    explosive.setPos(position.getX() + randomPosX, 200, position.getZ() + walkingZ * 1.5f);
                }
                if (walkingDirection == Direction.EAST) {
                    explosive.setPos(position.getX() + walkingX * 1.5f, 200, position.getZ() + randomPosZ);
                }
                if (walkingDirection == Direction.WEST) {
                    explosive.setPos(position.getX() - walkingX * 1.5f, 200, position.getZ() + randomPosZ);
                }
            } else {
                explosive.setPos(position.getX() + randomPosX, 200, position.getZ() - randomPosZ);
            }
            explosive.setDeltaMovement(-0.3f, 0f, 0f);
            player.level().addFreshEntity(explosive);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        
        // Create a new BarrageSpawner instead of spawning missiles directly
        new BarrageSpawner(player, position, radius, isWalking, direction);
    }
}