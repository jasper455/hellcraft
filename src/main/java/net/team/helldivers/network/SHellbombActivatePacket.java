package net.team.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.helper.DelayedExplosion;

import java.util.UUID;
import java.util.function.Supplier;

public class SHellbombActivatePacket {
    private final int entityId;
    private final boolean activated;

    public SHellbombActivatePacket(int entityId, boolean activated) {
        this.entityId = entityId;
        this.activated = activated;
    }

    public SHellbombActivatePacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.activated = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.activated);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getSender() != null) {
                Entity entity = context.get().getSender().level().getEntity(entityId);
                if (entity instanceof HellbombHellpodEntity hellbomb) {
                    hellbomb.setActivated(activated);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
