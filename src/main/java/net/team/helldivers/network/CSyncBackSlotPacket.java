package net.team.helldivers.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.helper.ClientBackSlotCache;

import java.util.function.Supplier;

public class CSyncBackSlotPacket {
    private final CompoundTag nbt;

    public CSyncBackSlotPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public CSyncBackSlotPacket(FriendlyByteBuf buffer) {
        this(buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;

            player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
                backSlot.loadNBTData(this.nbt); // apply server data to client
            });
        });
        context.get().setPacketHandled(true);
    }
}