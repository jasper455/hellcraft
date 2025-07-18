package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.client.hud.Stratagems;

import java.util.function.Supplier;

public class ItemRequestPacket {
    private final int slot;

    public ItemRequestPacket(int slot) {
        this.slot = slot;
    }

    public static void encode(ItemRequestPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.slot);
    }

    public static ItemRequestPacket decode(FriendlyByteBuf buf) {
        return new ItemRequestPacket(buf.readInt());
    }

    public static void handle(ItemRequestPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack result = Stratagems.getItem(player, msg.slot);

                // Send result back to client
                PacketHandler.sendToPlayer(new ItemResponsePacket(msg.slot, result), player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}