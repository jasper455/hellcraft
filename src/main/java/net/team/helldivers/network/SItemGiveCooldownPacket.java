package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.helper.ClientItemCache;
import net.team.helldivers.item.ModItems;

import java.util.function.Supplier;

public class SItemGiveCooldownPacket {
    private final ItemStack itemStack;
    private final int cooldownTime;

    public SItemGiveCooldownPacket(ItemStack itemStack, int cooldownTime) {
        this.itemStack = itemStack;
        this.cooldownTime = cooldownTime;
    }

    public SItemGiveCooldownPacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
        buffer.writeInt(this.cooldownTime);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        if (itemStack.is(ModItems.HELLBOMB_ITEM.get())) {
            ClientItemCache.removeFromSlotCache(ClientItemCache.getSlotWithItem(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()),
                    ModItems.HELLBOMB_ITEM.get().getDefaultInstance());
            return;
        }
        player.getCooldowns().addCooldown(itemStack.getItem(), cooldownTime);
    }

}