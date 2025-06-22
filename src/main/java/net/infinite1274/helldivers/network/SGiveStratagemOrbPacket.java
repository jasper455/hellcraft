package net.infinite1274.helldivers.network;

import net.infinite1274.helldivers.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SGiveStratagemOrbPacket {
    private final String stratagemType;

    public SGiveStratagemOrbPacket(String stratagemType) {
        this.stratagemType = stratagemType;
    }

    public SGiveStratagemOrbPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(stratagemType);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        ItemStack stack = new ItemStack(ModItems.STRATAGEM_ORB.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("stratagemType", stratagemType);

        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }
}
