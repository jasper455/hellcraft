package net.team.helldivers.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.EAT17Renderer;
import net.team.helldivers.entity.custom.RocketProjectileEntity;
import net.team.helldivers.network.CApplyRecoilPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;

public class EAT17Item extends AbstractGunItem {

    public EAT17Item(Properties properties) {
        super(properties.durability(2).rarity(Rarity.COMMON), true,false, "§e[Expendable-Anti-Tank]", ModSounds.AR_23_RELOAD);
    }  
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
         if (itemStack.getDamageValue() < itemStack.getMaxDamage()) {

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
                player.getCooldowns().addCooldown(itemStack.getItem(), 2);

                itemStack.hurtAndBreak(47, player, (serverPlayer) -> {
                        player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                    });

            }
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new EAT17Renderer();
    }
}