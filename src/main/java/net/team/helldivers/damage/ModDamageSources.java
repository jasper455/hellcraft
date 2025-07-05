package net.team.helldivers.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class ModDamageSources {
    public static DamageSource orbitalLaser(Entity source) {
        return new DamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(ModDamageTypes.ORBITAL_LASER).get());
    }
}

