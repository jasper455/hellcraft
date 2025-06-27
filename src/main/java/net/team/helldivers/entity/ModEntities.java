package net.team.helldivers.entity;

import net.team.helldivers.HellcraftMod;
import net.team.helldivers.entity.custom.HellpodProjectileEntity;
import net.team.helldivers.entity.custom.MissileProjectileEntity;
import net.team.helldivers.entity.custom.StratagemOrbEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HellcraftMod.MOD_ID);

    public static final RegistryObject<EntityType<MissileProjectileEntity>> MISSILE_PROJECTILE =
            ENTITY_TYPES.register("missile", () -> EntityType.Builder.<MissileProjectileEntity>of(MissileProjectileEntity::new, MobCategory.MISC)
                    .sized(1f, 0.75f).build("missile"));

    public static final RegistryObject<EntityType<StratagemOrbEntity>> STRATAGEM_ORB =
            ENTITY_TYPES.register("stratagem_orb", () -> EntityType.Builder.<StratagemOrbEntity>of(StratagemOrbEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.4f).build("stratagem_orb"));


    public static final RegistryObject<EntityType<HellpodProjectileEntity>> HELLPOD =
            ENTITY_TYPES.register("hellpod", () -> EntityType.Builder.<HellpodProjectileEntity>of(HellpodProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.4f).build("hellpod"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
