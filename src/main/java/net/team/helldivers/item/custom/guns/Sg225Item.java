package net.team.helldivers.item.custom.guns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.Sg225Renderer;
import net.team.helldivers.network.CApplyRecoilPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.ShootHelper;

public class Sg225Item extends AbstractGunItem {

    public Sg225Item(Properties properties) {
        super(properties.durability(8).rarity(Rarity.COMMON), true,true, "§e[Shotgun]", 20, new Sg225Renderer(), ModSounds.AR_23_RELOAD);
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
         if (!player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            if (itemStack.getDamageValue() < itemStack.getMaxDamage() - 1) {

                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.SG225_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(2.0f), player);
                for(int i=0; i<21;i++){
                    ShootHelper.shoot(player, player.level(), 0.3, 1, 0.3, true);
                }
                player.getCooldowns().addCooldown(itemStack.getItem(), 20);

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