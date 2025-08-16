package net.team.helldivers.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.*;
import net.team.helldivers.entity.custom.bots.AutomatonTrooperEntity;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import net.team.helldivers.entity.custom.bots.DevastatorEntity;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;

public class ModBotEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HelldiversMod.MOD_ID);

    public static final RegistryObject<EntityType<RangedHulkEntity>> HULK =
            ENTITY_TYPES.register("hulk", () -> EntityType.Builder.of(RangedHulkEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 3.5f).build("hulk"));

    public static final RegistryObject<EntityType<BerserkerEntity>> BERSERKER =
            ENTITY_TYPES.register("berserker", () -> EntityType.Builder.of(BerserkerEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2.6f).build("berserker"));

    public static final RegistryObject<EntityType<AutomatonTrooperEntity>> AUTOMATON_TROOPER =
            ENTITY_TYPES.register("automaton_trooper", () -> EntityType.Builder.of(AutomatonTrooperEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 2.6f).build("automaton_trooper"));

    public static final RegistryObject<EntityType<DevastatorEntity>> DEVASTATOR =
            ENTITY_TYPES.register("devastator", () -> EntityType.Builder.of(DevastatorEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2.6f).build("devastator"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
