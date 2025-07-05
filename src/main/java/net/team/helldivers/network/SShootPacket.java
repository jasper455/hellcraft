package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.entity.custom.HeatedGasProjectileEntity;
import net.team.helldivers.entity.custom.MissileProjectileEntity;
import net.team.helldivers.entity.custom.RocketProjectileEntity;
import net.team.helldivers.item.custom.Ar23Item;
import net.team.helldivers.item.custom.EAT17Item;
import net.team.helldivers.item.custom.P2Item;
import net.team.helldivers.item.custom.Plas1Item;
import net.team.helldivers.sound.ModSounds;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

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

        // Add cooldown check to prevent multiple shots
        long currentTime = System.currentTimeMillis();
        long lastTime = lastShootTime.getOrDefault(player.getUUID(), 0L);
        if (currentTime - lastTime < SHOOT_COOLDOWN) {
            return;
        }
        lastShootTime.put(player.getUUID(), currentTime);

        ItemStack heldItem = player.getMainHandItem();


        // Ar-23 Liberator Shooting Logic

        if (heldItem.getItem() instanceof Ar23Item ar23Item) {
            // Check if we can still shoot
            if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 5) {
                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.AR_22_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);

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
            } else {
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                player.getCooldowns().addCooldown(heldItem.getItem(), 10);
            }
    }

        // Expendable Anti-Tank Shooting Logic

        if (heldItem.getItem() instanceof EAT17Item eat17Item) {
            if (heldItem.getDamageValue() < heldItem.getMaxDamage()) {

                player.level().playSound(null, player.blockPosition(),
                        ModSounds.EAT_FIRE.get(), SoundSource.PLAYERS, 5.0f, 1.0f);

                PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);
                // Actually shoot the rocket
                RocketProjectileEntity rocket = new RocketProjectileEntity(player, player.level());
                rocket.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 3f, 0.0f);
                rocket.setXRot(player.getXRot());
                rocket.setYRot(player.getYRot());
                rocket.setNoGravity(true);
                player.level().addFreshEntity(rocket);

                heldItem.hurtAndBreak(47, player, (serverPlayer) -> {
                        player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                    });

            }
        }

        // P2 Peacemaker Shooting Logic

        if (heldItem.getItem() instanceof P2Item) {
            // Check if we can still shoot
            if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 1) {
                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.P2_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);

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
            } else {
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                player.getCooldowns().addCooldown(heldItem.getItem(), 10);
            }
        }

        // PLAS-1 Scorcher Shooting Logic

        if (heldItem.getItem() instanceof Plas1Item) {
            // Check if we can still shoot
            if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 1) {
                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.PLAS1_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);

                // Actually shoot the bullet
                HeatedGasProjectileEntity heatedGas = new HeatedGasProjectileEntity(player, player.level());
                heatedGas.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 5f, 0.0f);
                heatedGas.setXRot(player.getXRot());
                heatedGas.setYRot(player.getYRot());
                heatedGas.setNoGravity(true);
                player.level().addFreshEntity(heatedGas);

                // Damage the item
                if (!player.getAbilities().instabuild) {
                    heldItem.hurt(1, player.getRandom(), player);
                }
            } else {
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                player.getCooldowns().addCooldown(heldItem.getItem(), 10);
            }
        }
    }
}