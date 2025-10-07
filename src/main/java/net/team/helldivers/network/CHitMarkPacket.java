package net.team.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.client.hud.HitMarkRender;

import java.util.function.Supplier;

public class CHitMarkPacket {

    public CHitMarkPacket(FriendlyByteBuf buffer) {
    }
    public CHitMarkPacket() {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            HitMarkRender.triggerHitMarker(200); 
        });
        context.get().setPacketHandled(true);
    }

}