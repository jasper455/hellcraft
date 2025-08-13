package net.team.helldivers.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.*;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;

public class ModBotEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HelldiversMod.MOD_ID);

    public static final RegistryObject<EntityType<RangedHulkEntity>> HULK =
            ENTITY_TYPES.register("hulk", () -> EntityType.Builder.of(RangedHulkEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 3.5f).build("hulk"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
