package net.infinite1274.helldivers.entity;

import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.entity.custom.MissileProjectileEntity;
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

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
