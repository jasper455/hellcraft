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
import net.team.helldivers.worldgen.dimension.ModDimensions;

public class SStartShootPacket {
    public SStartShootPacket() {}

    public SStartShootPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        if (player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) return;
        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof AbstractGunItem gun){
            if(!gun.isAuto){
                gun.onShoot(heldItem, player);
                gun.onStartShoot(heldItem, player);
            }
            else gun.onStartShoot(heldItem, player);
        }
    }
}