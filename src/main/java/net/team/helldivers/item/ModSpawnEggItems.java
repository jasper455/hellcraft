package net.team.helldivers.item;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.entity.ModBotEntities;
import net.team.helldivers.item.custom.*;
import net.team.helldivers.item.custom.guns.*;
import net.team.helldivers.sound.ModSounds;

public class ModSpawnEggItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HelldiversMod.MOD_ID);

    public static final RegistryObject<Item> HULK_SPAWN_EGG = ITEMS.register("hulk_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.HULK, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static final RegistryObject<Item> BERSERKER_SPAWN_EGG = ITEMS.register("berserker_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.BERSERKER, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static final RegistryObject<Item> AUTOMATON_TROOPER_SPAWN_EGG = ITEMS.register("automaton_trooper_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.AUTOMATON_TROOPER, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static final RegistryObject<Item> DEVASTATOR_SPAWN_EGG = ITEMS.register("devastator_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.DEVASTATOR, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static final RegistryObject<Item> COMMISSAR_SPAWN_EGG = ITEMS.register("commissar_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.COMMISSAR, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static final RegistryObject<Item> BRAWLER_SPAWN_EGG = ITEMS.register("brawler_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.BRAWLER, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static final RegistryObject<Item> AUTOMATON_CANNON_SPAWN_EGG = ITEMS.register("automaton_cannon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModBotEntities.AUTOMATON_CANNON, 0x383838, 0xff0000,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
