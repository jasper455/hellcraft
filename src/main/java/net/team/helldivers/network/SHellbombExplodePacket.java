package net.team.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.helper.DelayedExplosion;

import java.util.function.Supplier;

public class SHellbombExplodePacket {
    private final BlockPos position;

    public SHellbombExplodePacket(BlockPos position) {
        this.position = position;
    }

    public SHellbombExplodePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        MinecraftForge.EVENT_BUS.register(new DelayedExplosion(player.level(), position, 25, 300));
    }
}