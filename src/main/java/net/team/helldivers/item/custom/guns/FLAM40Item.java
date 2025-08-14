package net.team.helldivers.item.custom.guns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.network.CFlamesEndParticlePacket;
import net.team.helldivers.network.CFlamesParticlePacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.util.ShootHelper;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks;
    private boolean shooting = false;
    public FLAM40Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), false, true, "§e[Flamethrower]", 1 ,new AmrRenderer(), ModSounds.AMR_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void onStartShoot(ItemStack itemStack, ServerPlayer player) {
        System.out.println("start");
        PacketHandler.sendToPlayer(new CFlamesParticlePacket(), player);
    }
    @Override
    public void onEndShoot(ItemStack itemStack, ServerPlayer player) {
        System.out.println("end");
        PacketHandler.sendToPlayer(new CFlamesEndParticlePacket(), player);
    }
    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        //pilotlight logic
    }
}