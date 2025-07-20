package net.team.helldivers.item;


import net.minecraft.world.item.RecordItem;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.sound.ModSounds;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HelldiversMod.MOD_ID);

    public static final RegistryObject<Item> TRAP_ROYALTY_MUSIC_DISC = ITEMS.register("trap_royalty_music_disc",
            () -> new RecordItem(6, ModSounds.TRAP_ROYALTY, new Item.Properties().stacksTo(1), 3300));

    public static final RegistryObject<Item> EFFECT_TESTER = ITEMS.register("effect_tester",
            () -> new EffectTesterItem(new Item.Properties()));

    public static final RegistryObject<Item> STRATAGEM_ORB = ITEMS.register("stratagem_orb",
            () -> new StratagemOrbItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> TITANIUM = ITEMS.register("titanium",
            () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> SUPPLIES = ITEMS.register("supplies",
            () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> AR23 = ITEMS.register("ar23",
            () -> new Ar23Item(new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> EAT_17 = ITEMS.register("eat_17",
            () -> new EAT17Item(new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> FRAG_GRENADE = ITEMS.register("frag_grenade",
            () -> new FragGrenadeItem(new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> P2_PEACEMAKER = ITEMS.register("p2",
            () -> new P2Item(new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> PLAS1 = ITEMS.register("plas1",
            () -> new Plas1Item(new Item.Properties().stacksTo(1).fireResistant()));


    public static final RegistryObject<Item> STRATAGEM_PICKER = ITEMS.register("stratagem_picker",
            () -> new StratagemPickerItem(new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> EXTRACTION_TERMINAL_BLOCK_ITEM = ITEMS.register("extraction_terminal_block_item",
            () -> new ExtractionTerminalBlockItem(ModBlocks.EXTRACTION_TERMINAL.get(), new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> BOT_CONTACT_MINE_BLOCK_ITEM = ITEMS.register("bot_contact_mine_block_item",
            () -> new BotContactMineBlockItem(ModBlocks.BOT_CONTACT_MINE.get(), new Item.Properties().fireResistant()));

    // STRATAGEMS

    // OTHER
    public static final RegistryObject<Item> HELLBOMB_ITEM = ITEMS.register("hellbomb_item",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    // SUPPORT
    public static final RegistryObject<Item> RESUPPLY = ITEMS.register("resupply",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> ANTI_TANK_STRATAGEM = ITEMS.register("eat_stratagem",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    // ORBITAL
    public static final RegistryObject<Item> SMALL_BARRAGE = ITEMS.register("small_barrage",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> BIG_BARRAGE = ITEMS.register("big_barrage",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> PRECISION_STRIKE = ITEMS.register("precision_strike",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> ORBITAL_LASER = ITEMS.register("orbital_laser",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    // EAGLE
    public static final RegistryObject<Item> EAGLE_500KG_BOMB = ITEMS.register("500kg_bomb",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> CLUSTER_BOMB = ITEMS.register("cluster_bomb",
            () -> new StratagemItem(new Item.Properties().stacksTo(1).fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
