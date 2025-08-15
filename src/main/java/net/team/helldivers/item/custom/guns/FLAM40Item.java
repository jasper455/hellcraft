package net.team.helldivers.item.custom.guns;

import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
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
    public FLAM40Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), true, true, "§e[Flamethrower]", 1 ,new AmrRenderer(), ModSounds.AMR_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        System.out.println("shot");
        HitResult result = ShootHelper.raycast(player.level(), player, 0).getFirst();
         PacketHandler.sendToPlayer(new CFlamesParticlePacket(), player);
        //use this (make sure to add print statements) lower size of particle and spawn one every tick. at the begining, raycast and if raycast hits block spawn a burst particle and kill all particles past the block. kill the particles by storing each particle (every tick) in a map with the key of sys time mills, compare the time of the most recent one with the time of the oldest one to find out where each particle is now and which ones I should kill.
    }
}