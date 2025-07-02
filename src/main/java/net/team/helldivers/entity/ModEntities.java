package net.team.helldivers.entity;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HelldiversMod.MOD_ID);

    public static final RegistryObject<EntityType<MissileProjectileEntity>> MISSILE_PROJECTILE =
            ENTITY_TYPES.register("missile", () -> EntityType.Builder.<MissileProjectileEntity>of(MissileProjectileEntity::new, MobCategory.MISC)
                    .sized(1f, 0.75f).build("missile"));

    public static final RegistryObject<EntityType<Eagle500KgEntity>> EAGLE_500KG_BOMB =
            ENTITY_TYPES.register("eagle_500kg_bomb", () -> EntityType.Builder.<Eagle500KgEntity>of(Eagle500KgEntity::new, MobCategory.MISC)
                    .sized(1f, 0.75f).build("eagle_500kg_bomb"));

    public static final RegistryObject<EntityType<StratagemOrbEntity>> STRATAGEM_ORB =
            ENTITY_TYPES.register("stratagem_orb", () -> EntityType.Builder.<StratagemOrbEntity>of(StratagemOrbEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.4f).build("stratagem_orb"));

    public static final RegistryObject<EntityType<HellpodProjectileEntity>> HELLPOD =
            ENTITY_TYPES.register("hellpod", () -> EntityType.Builder.<HellpodProjectileEntity>of(HellpodProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.4f).build("hellpod"));

    public static final RegistryObject<EntityType<SupportHellpodEntity>> SUPPORT_HELLPOD =
            ENTITY_TYPES.register("support_hellpod", () -> EntityType.Builder.<SupportHellpodEntity>of(SupportHellpodEntity::new, MobCategory.MISC)
                    .sized(0.63f, 2.3f).build("support_hellpod"));

    public static final RegistryObject<EntityType<BulletProjectileEntity>> BULLET =
            ENTITY_TYPES.register("bullet", () -> EntityType.Builder.<BulletProjectileEntity>of(BulletProjectileEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f).build("bullet"));

    public static final RegistryObject<EntityType<RocketProjectileEntity>> ROCKET =
            ENTITY_TYPES.register("rocket", () -> EntityType.Builder.<RocketProjectileEntity>of(RocketProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).build("rocket"));

    public static final RegistryObject<EntityType<EagleAirshipEntity>> EAGLE_AIRSHIP =
            ENTITY_TYPES.register("eagle_airship", () -> EntityType.Builder.of(EagleAirshipEntity::new, MobCategory.MISC)
                    .sized(0.00001f, 0.00001f).build("eagle_airship"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
