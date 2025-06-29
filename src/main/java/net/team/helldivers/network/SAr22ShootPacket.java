package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.item.custom.Ar23Item;
import net.team.helldivers.sound.ModSounds;

import java.util.function.Supplier;

public class SAr22ShootPacket {

    public SAr22ShootPacket() {}

    public SAr22ShootPacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;

        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof Ar23Item ar23Item) {
            // Check if we can still shoot
            if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 5) {
                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.AR_22_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(1.0f), player);

            // Actually shoot the bullet
            BulletProjectileEntity bullet = new BulletProjectileEntity(player, player.level());
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 5f, 0.0f);
            bullet.setXRot(player.getXRot());
            bullet.setYRot(player.getYRot());
            bullet.setNoGravity(true);
            player.level().addFreshEntity(bullet);

            // Damage the item
            if (!player.getAbilities().instabuild) {
                heldItem.hurt(1, player.getRandom(), player);
            }
        }
    }
}
}