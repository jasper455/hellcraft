package net.team.helldivers.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.helper.ClientJammedSync;

import java.util.function.Supplier;

public class SSyncJammedPacket {
    private final boolean isJammed;

    public SSyncJammedPacket(boolean isJammed) {
        this.isJammed = isJammed;
    }

    public SSyncJammedPacket(FriendlyByteBuf buffer) {
        this(buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {buffer.writeBoolean(this.isJammed);}

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        ClientJammedSync.setIsJammed(isJammed);
    }
}