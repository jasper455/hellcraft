package net.team.helldivers.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.helper.ClientBackSlotCache;
import net.team.helldivers.item.custom.backpacks.AbstractBackpackItem;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;

import java.util.function.Supplier;

public class SSetBackSlotPacket {

    public SSetBackSlotPacket() {}

    public SSetBackSlotPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;

        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack mainHand = player.getMainHandItem();
            ItemStack backSlotItem = handler.getStackInSlot(0);

            if (backSlotItem.isEmpty() && !mainHand.isEmpty()) {
                if (mainHand.getItem() instanceof TieredItem || mainHand.getItem() instanceof ShieldItem
                        || mainHand.getItem() instanceof AbstractBackpackItem) {
                    handler.setStackInSlot(0, mainHand.copy());
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                }
            } else if (!backSlotItem.isEmpty() && mainHand.isEmpty()) {
                if (backSlotItem.getItem() instanceof PortableHellbombItem hellbombItem) {
                    if (!hellbombItem.isActivated()) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, backSlotItem.copy());
                        handler.setStackInSlot(0, ItemStack.EMPTY);
//                        player.sendSystemMessage(Component.literal("test"));
                    }
                } else {
                    player.setItemInHand(InteractionHand.MAIN_HAND, backSlotItem.copy());
                    handler.setStackInSlot(0, ItemStack.EMPTY);
                }
            } else if (!backSlotItem.isEmpty() && !mainHand.isEmpty()) {
                if (mainHand.getItem() instanceof TieredItem || mainHand.getItem() instanceof ShieldItem
                        || mainHand.getItem() instanceof AbstractBackpackItem) {
                    if (backSlotItem.getItem() instanceof PortableHellbombItem hellbombItem) {
                        if (!hellbombItem.isActivated()) {
                            handler.setStackInSlot(0, mainHand.copy());
                            player.setItemInHand(InteractionHand.MAIN_HAND, backSlotItem.copy());
//                            player.sendSystemMessage(Component.literal("test1"));
                        }
                    } else {
                        handler.setStackInSlot(0, mainHand.copy());
                        player.setItemInHand(InteractionHand.MAIN_HAND, backSlotItem.copy());
                    }
                }
            }

            // Sync back to client
            CompoundTag tag = new CompoundTag();
            backSlot.saveNBTData(tag);
            PacketHandler.sendToPlayer(new CSyncBackSlotPacket(tag), player);
        });
    }
}