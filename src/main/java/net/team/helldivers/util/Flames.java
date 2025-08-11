package net.team.helldivers.util;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.ModItems;

public class Flames {
   // private static final ParticleEmitterInfo FIRE = new ParticleEmitterInfo(new ResourceLocation("helldivers", "flameline"));
    public static void spawnFlames(Player player){
        if(player.level().isClientSide && Minecraft.getInstance().player == player && player.getMainHandItem().is(ModItems.FLAM40.get())){
            //AAALevel.addParticle(player.level(), FIRE.clone().position(player.getEyePosition()));  
        }
    }
    
}
