package net.team.helldivers.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.item.custom.guns.AbstractGunItem;

public class SStopShootPacket {
    public SStopShootPacket() {}

    public SStopShootPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof AbstractGunItem gun){
           gun.onEndShoot(heldItem, player);
        }
    }
}