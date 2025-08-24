package net.team.helldivers.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.Plas1Renderer;
import net.team.helldivers.entity.custom.SmallGrenadeEntity;
import net.team.helldivers.network.CApplyRecoilPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;

public class  GpItem extends AbstractGunItem {

    public GpItem(Properties properties) {
        super(properties.durability(6).rarity(Rarity.COMMON),false,  true, "§e[Explosive]", 10, ModSounds.PLAS1_RELOAD);//TEMP SOUNDS
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        if (!player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            if (itemStack.getDamageValue() < itemStack.getMaxDamage() - 1) {
                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.PLAS1_SHOOT.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(7.0f), player);
                // Actually shoot the bullet
                SmallGrenadeEntity grenade = new SmallGrenadeEntity(player, player.level(), 1);
                grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 4f, 0.0f);
                grenade.setXRot(player.getXRot());
                grenade.setYRot(player.getYRot());
                player.level().addFreshEntity(grenade);
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

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new Plas1Renderer();
    }
}