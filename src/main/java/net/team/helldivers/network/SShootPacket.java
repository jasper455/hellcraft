package net.team.helldivers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.entity.custom.HeatedGasProjectileEntity;
import net.team.helldivers.entity.custom.RocketProjectileEntity;
import net.team.helldivers.item.custom.guns.*;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.worldgen.dimension.ModDimensions;

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

        if (player.level().dimension().equals(ModDimensions.SUPER_DESTROYER_DIM)) return;


        // Ar-23 Liberator Shooting Logic

        if (heldItem.getItem() instanceof Ar23Item ar23Item) {
            // Check if we can still shoot
            if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) {
                if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 5) {

                    // Play sound
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.AR_23_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                    PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);

                    // Actually shoot the bullet
                    BulletProjectileEntity bullet = new BulletProjectileEntity(player, player.level(), false, false);
                    bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 5f, 0.0f);
                    bullet.setXRot(player.getXRot());
                    bullet.setYRot(player.getYRot());
                    bullet.setNoGravity(true);
                    player.level().addFreshEntity(bullet);
                    player.getCooldowns().addCooldown(heldItem.getItem(), 2);

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
                player.getCooldowns().addCooldown(heldItem.getItem(), 2);

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
                BulletProjectileEntity bullet = new BulletProjectileEntity(player, player.level(), false, false);
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
            if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) {
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
                    player.getCooldowns().addCooldown(heldItem.getItem(), 5);

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

        // SG-225 Breaker Shooting Logic

        if (heldItem.getItem() instanceof Sg225Item) {
            // Check if we can still shoot
            if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) {
                if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 1) {
                    // Play sound
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.SG225_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                    PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);

                    // Actually shoot the bullets
                    for (int i = 0; i < 4; i++) {
                        BulletProjectileEntity bulletProjectile = new BulletProjectileEntity(player, player.level(), true, false);
                        bulletProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 5f, 3.0f);
                        bulletProjectile.setXRot(player.getXRot());
                        bulletProjectile.setYRot(player.getYRot());
                        bulletProjectile.setNoGravity(true);
                        player.level().addFreshEntity(bulletProjectile);
                    }
                    player.getCooldowns().addCooldown(heldItem.getItem(), 5);

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

        // Stalwart Shooting Logic

        if (heldItem.getItem() instanceof StalwartItem) {
            // Check if we can still shoot
            if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) {
                if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 1) {
                    // Play sound
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.STALWART_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                    PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.5f), player);

                    // Actually shoot the bullets
                    BulletProjectileEntity bulletProjectile = new BulletProjectileEntity(player, player.level(), false, false);
                    bulletProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 5f, 0f);
                    bulletProjectile.setXRot(player.getXRot());
                    bulletProjectile.setYRot(player.getYRot());
                    bulletProjectile.setNoGravity(true);
                    player.level().addFreshEntity(bulletProjectile);
                    player.getCooldowns().addCooldown(heldItem.getItem(), 1);

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

        // AMR Shooting Logic

        if (heldItem.getItem() instanceof AmrItem) {
            // Check if we can still shoot
            if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) {
                if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 1) {
                    // Play sound
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.AMR_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                    PacketHandler.sendToPlayer(new CApplyRecoilPacket(10.0f), player);

                    // Actually shoot the bullets
                    BulletProjectileEntity bulletProjectile = new BulletProjectileEntity(player, player.level(), false, true);
                    bulletProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 5f, 0f);
                    bulletProjectile.setXRot(player.getXRot());
                    bulletProjectile.setYRot(player.getYRot());
                    bulletProjectile.setNoGravity(true);
                    player.level().addFreshEntity(bulletProjectile);
                    player.getCooldowns().addCooldown(heldItem.getItem(), 25);

                    // Damage the item
                    if (!player.getAbilities().instabuild) {
                        heldItem.hurt(1, player.getRandom(), player);
                    }
                } else {
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                    player.getCooldowns().addCooldown(heldItem.getItem(), 25);
                }
            }
        }
    }
}