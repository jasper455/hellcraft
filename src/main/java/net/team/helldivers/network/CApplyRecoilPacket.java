package net.team.helldivers.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CApplyRecoilPacket {
    private final float recoilAmount;

    public CApplyRecoilPacket(float recoilAmount) {
        this.recoilAmount = recoilAmount;
    }

    public CApplyRecoilPacket(FriendlyByteBuf buf) {
        this.recoilAmount = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(recoilAmount);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Client side only
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.setXRot(
                        Minecraft.getInstance().player.getXRot()
                                - (recoilAmount * (Minecraft.getInstance().player.isCrouching() ? 0.5f : 1f))
                );
            }
        });
        context.get().setPacketHandled(true);
    }
}
