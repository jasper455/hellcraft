package net.team.helldivers.item.custom.guns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.Plas1Renderer;
import net.team.helldivers.entity.custom.HeatedGasProjectileEntity;
import net.team.helldivers.network.CApplyRecoilPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;

public class  Plas1Item extends AbstractGunItem {

    public Plas1Item(Properties properties) {
        super(properties.durability(6).rarity(Rarity.COMMON),true,  true, "§e[Plasma-Based]",new Plas1Renderer(), ModSounds.PLAS1_RELOAD);
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        if (!player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            if (itemStack.getDamageValue() < itemStack.getMaxDamage() - 1) {
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
                player.getCooldowns().addCooldown(itemStack.getItem(), 5);

                // Damage the item
                if (!player.getAbilities().instabuild) {
                    itemStack.hurt(1, player.getRandom(), player);
                }
            } else {
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                player.getCooldowns().addCooldown(itemStack.getItem(), 10);
            }
        }
    }
}