package net.team.helldivers.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.block.custom.AmmoCrateBlock;

public class SGunReloadPacket {

    public SGunReloadPacket() {}

    public SGunReloadPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        ItemStack heldItem = player.getMainHandItem();
        heldItem.setDamageValue(0);
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AmmoCrateBlock) {
                stack.shrink(1);
            }
        }
    }

}