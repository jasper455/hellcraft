package net.team.helldivers.item.custom.guns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.network.CFlamesParticlePacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks;

    public FLAM40Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), true, true, "§e[Flamethrower]",new AmrRenderer(), ModSounds.AMR_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        if(itemStack.getDamageValue() < itemStack.getMaxDamage() - 1 && !player.getCooldowns().isOnCooldown(itemStack.getItem())){
            player.getCooldowns().addCooldown(itemStack.getItem(), 2);
             PacketHandler.sendToPlayer(new CFlamesParticlePacket(), player);
        }
    }
   
}