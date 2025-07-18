package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.helper.ClientItemCache;

import java.util.function.Supplier;

public class ItemResponsePacket {
    private final int slot;
    private final ItemStack stack;

    public ItemResponsePacket(int slot, ItemStack stack) {
        this.slot = slot;
        this.stack = stack;
    }

    public ItemResponsePacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.stack = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
        buf.writeItem(this.stack);
    }

    public static void handle(ItemResponsePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client thread
            ClientItemCache.setSlot(msg.slot, msg.stack);
        });
        ctx.get().setPacketHandled(true);
    }
}