package net.infinite1274.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SExplosionPacket {
    private final BlockPos position;
    private final int radius;

    public SExplosionPacket(BlockPos position, int radius) {
        this.position = position;
        this.radius = radius;
    }

    public SExplosionPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
        buffer.writeInt(this.radius);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        player.level().explode(null, position.getX(), position.getY(), position.getZ(), radius, false, Level.ExplosionInteraction.BLOCK);
    }
}