package net.team.helldivers.item.custom.guns;

import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.network.CFlamesEndParticlePacket;
import net.team.helldivers.network.CFlamesParticlePacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.ShootHelper;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks;
    private boolean shooting = false;
    public FLAM40Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), false, true, "§e[Flamethrower]",new AmrRenderer(), ModSounds.AMR_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
         if(itemStack.getDamageValue() < itemStack.getMaxDamage() - 1 && !player.getCooldowns().isOnCooldown(itemStack.getItem()) && !shooting){
            player.getCooldowns().addCooldown(itemStack.getItem(), 5);
            System.out.println("shoot");
            PacketHandler.sendToPlayer(new CFlamesParticlePacket(), player);
            //damage 
            HitResult result = ShootHelper.raycast(player.level(), player, 0).getFirst();
            if(result.getType() == HitResult.Type.ENTITY){//entity code

            }
            if(result.getType() == HitResult.Type.BLOCK){//block code
                
            }
            shooting = true;
        }
        if(shooting){
            PacketHandler.sendToPlayer(new CFlamesEndParticlePacket(), player);
        }

    }
   
}