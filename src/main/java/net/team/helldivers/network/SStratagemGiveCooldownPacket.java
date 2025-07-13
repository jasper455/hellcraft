package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.client.hud.Stratagems;
import net.team.helldivers.item.inventory.StratagemPickerInventory;

import java.util.function.Supplier;

public class SStratagemGiveCooldownPacket {
    private final ItemStack itemStack;
    private final int cooldownTime;

    public SStratagemGiveCooldownPacket(ItemStack itemStack, int cooldownTime) {
        this.itemStack = itemStack;
        this.cooldownTime = cooldownTime;
    }

    public SStratagemGiveCooldownPacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
        buffer.writeInt(this.cooldownTime);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null || Stratagems.getPickerInventory(player) == null) return;
        player.getCooldowns().addCooldown(itemStack.getItem(), cooldownTime);
    }

}