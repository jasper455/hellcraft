package net.team.helldivers.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.helper.ClientBackSlotCache;
import net.team.helldivers.helper.ClientItemCache;

import java.util.function.Supplier;

public class SInitializeBackSlotPacket {

    public SInitializeBackSlotPacket() {}

    public SInitializeBackSlotPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ClientBackSlotCache.addToSlotCache(backSlot.getInventory().getStackInSlot(0));
        });
    }
}