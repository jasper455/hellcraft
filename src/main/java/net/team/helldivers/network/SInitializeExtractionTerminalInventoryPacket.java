package net.team.helldivers.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.helper.ClientItemCache;

import java.util.function.Supplier;

public class SInitializeExtractionTerminalInventoryPacket {

    public SInitializeExtractionTerminalInventoryPacket() {}

    public SInitializeExtractionTerminalInventoryPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        for (int i = 0; i < 4; i++) {
            ClientItemCache.addToSlotCache(i, getExtractionTerminalItem(player, i));
        }
    }
    public ItemStack getExtractionTerminalItem(Player player, int slot) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag forgeData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag extractionData = forgeData.getCompound("ExtractionInventory");

        if (extractionData.contains("Items")) {
            ListTag items = extractionData.getList("Items", 10);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag itemTag = items.getCompound(i);
                int itemSlot = itemTag.getByte("Slot") & 255;
                if (itemSlot == slot) {
                    return ItemStack.of(itemTag);
                }
            }
        }

        return ItemStack.EMPTY;
    }
}