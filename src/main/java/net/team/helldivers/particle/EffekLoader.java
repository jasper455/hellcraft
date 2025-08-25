package net.team.helldivers.particle;

import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.team.helldivers.HelldiversMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EffekLoader {
	public static final ParticleEmitterInfo FIRE = new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "flame_long"));
	public static final ParticleEmitterInfo TRAIL = new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "trail"));
	public static final ParticleEmitterInfo HIT= new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "hit"));
	public static final ParticleEmitterInfo BLAST =  new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "blast"));
	public static final ParticleEmitterInfo ROCKET_TRAIL =  new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "rocket_trail"));
	public static final ParticleEmitterInfo PLASMA =  new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "plasma_shot"));
	public static final ParticleEmitterInfo PLASMA_HIT =  new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "hit_plasma"));
	public static final ParticleEmitterInfo NAPALM_BURST =  new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "napalm"));
	public static final ParticleEmitterInfo GRENADE =  new ParticleEmitterInfo(new ResourceLocation(HelldiversMod.MOD_ID, "grenade"));
}