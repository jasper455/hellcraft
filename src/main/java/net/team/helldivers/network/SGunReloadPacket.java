package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.block.custom.AmmoCrateBlock;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.sound.ModSounds;

import java.util.function.Supplier;

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
        if (heldItem.is(ModItems.PLAS1.get())) {
            player.level().playSound(null, player.blockPosition(), ModSounds.PLAS1_RELOAD.get(), SoundSource.PLAYERS, 10.0f, 1.0f);
        } else {
            player.level().playSound(null, player.blockPosition(), ModSounds.AR_23_RELOAD.get(), SoundSource.PLAYERS, 10.0f, 1.0f);
        }
        heldItem.setDamageValue(0);
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AmmoCrateBlock) {
                stack.shrink(1);
            }
        }
    }

}