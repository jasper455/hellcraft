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

/* 
        // Ar-23 Liberator Shooting Logic

        if (heldItem.getItem() instanceof Ar23Item ar23Item) {
            // Check if we can still shoot
            if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) {
                if (heldItem.getDamageValue() < heldItem.getMaxDamage() - 5) {

                    // Play sound
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.AR_23_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                    PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);
                    ShootHelper.shoot(player, player.level(), 128, 0.02, 3, 0.3f, true);
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

        if (heldItem.getItem() instanceof P2Item p2) {
            p2.onShoot(heldItem, player);
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
                    for(int i=0; i<21;i++){
                                          ShootHelper.shoot(player, player.level(), 20, 0.3, 1, 0.3, true);
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
                    ShootHelper.shoot(player, player.level(), 64, 0.05, 2, 0.2f, true);
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

                    ShootHelper.shoot(player, player.level(), 256, 0, 10, 0.4f, false);
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
    }*/
    }
}