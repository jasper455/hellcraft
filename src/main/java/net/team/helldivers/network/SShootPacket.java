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

public class SShootPacket {
    private static final Map<UUID, Long> lastShootTime = new HashMap<>();
    private static final long SHOOT_COOLDOWN = 50; // milliseconds
    public SShootPacket() {}

    public SShootPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        if (player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) return;

        // Add cooldown check to prevent multiple shots
        long currentTime = System.currentTimeMillis();
        long lastTime = lastShootTime.getOrDefault(player.getUUID(), 0L);
        if (currentTime - lastTime < SHOOT_COOLDOWN) {
            return;
        }
        lastShootTime.put(player.getUUID(), currentTime);
        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof AbstractGunItem gun){
            gun.onShoot(heldItem, player);
        }
    }
}