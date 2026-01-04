package net.team.helldivers.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.team.helldivers.HelldiversMod;

public interface ModDamageTypes {
    ResourceKey<DamageType> ORBITAL_LASER = ResourceKey.create(Registries.DAMAGE_TYPE, 
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "orbital_laser"));

    ResourceKey<DamageType> RAYCAST = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "raycast"));
}
