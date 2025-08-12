package net.team.helldivers.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.team.helldivers.entity.custom.MissileProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.team.helldivers.worldgen.dimension.ModDimensions;

import java.util.function.Supplier;

public class SOrbitalBarragePacket {
    private final BlockPos position;
    private final int radius;
    private boolean isWalking;
    private int direction;
    private boolean isNapalm;
    private int groundedTicks;

    public SOrbitalBarragePacket(BlockPos position, int radius, boolean isNapalm, boolean isWalking, int direction,
                                 int groundedTicks) {
        this.position = position;
        this.radius = radius;
        this.isNapalm = isNapalm;
        this.isWalking = isWalking;
        this.direction = direction;
        this.groundedTicks = groundedTicks;
    }

    public SOrbitalBarragePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
        buffer.writeInt(this.radius);
        buffer.writeBoolean(this.isNapalm);
        buffer.writeBoolean(this.isWalking);
        buffer.writeInt(this.direction);
        buffer.writeInt(this.groundedTicks);
    }

    public static class BarrageSpawner {
        private final ServerPlayer player;
        private final BlockPos position;
        private final int radius;
        private int missileCount = 0;
        private int tickDelay = 20;
        private boolean isWalking;
        private int walkingDirection;
        private boolean isNapalm;
        private int groundedTicks;

        public BarrageSpawner(ServerPlayer player, BlockPos position, int radius, boolean isWalking, int direction, boolean isNapalm, int groundedTicks) {
            this.player = player;
            this.position = position;
            this.radius = radius;
            this.isWalking = isWalking;
            this.walkingDirection = direction;
            this.isNapalm = isNapalm;
            this.groundedTicks = groundedTicks;
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
            float walkingZ = (Mth.randomBetween(player.level().getRandom(), groundedTicks / 7.5f, groundedTicks / 7.5f));
            float walkingX = (Mth.randomBetween(player.level().getRandom(), groundedTicks / 7.5f, groundedTicks / 7.5f));

            MissileProjectileEntity explosive = new MissileProjectileEntity(player, player.level(),12, isNapalm);
            if (isWalking) {
                if (walkingDirection == 0) {
                    explosive.setPos(position.getX() + randomPosX, 200, position.getZ() - walkingZ);
                }
                if (walkingDirection == 1) {
                    explosive.setPos(position.getX() + randomPosX, 200, position.getZ() + walkingZ);
                }
                if (walkingDirection == 2) {
                    explosive.setPos(position.getX() + walkingX, 200, position.getZ() + randomPosZ);
                }
                if (walkingDirection == 3) {
                    explosive.setPos(position.getX() - walkingX, 200, position.getZ() + randomPosZ);
                }
            } else {
                explosive.setPos(position.getX() + randomPosX, 200, position.getZ() - randomPosZ);
            }
            explosive.setDeltaMovement(0f, 0f, 0f);
            player.level().addFreshEntity(explosive);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        if (player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) return;
        // Create a new BarrageSpawner instead of spawning missiles directly
        new BarrageSpawner(player, position, radius, isWalking, direction, isNapalm, groundedTicks);
    }
}