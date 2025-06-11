package net.jasper.lodestonemod.entity;

import net.jasper.lodestonemod.LodestoneMod;
import net.jasper.lodestonemod.entity.custom.ExplosiveProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LodestoneMod.MOD_ID);

    public static final RegistryObject<EntityType<ExplosiveProjectileEntity>> EXPLOSIVE_PROJECTILE =
            ENTITY_TYPES.register("explosive", () -> EntityType.Builder.<ExplosiveProjectileEntity>of(ExplosiveProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("explosive"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
