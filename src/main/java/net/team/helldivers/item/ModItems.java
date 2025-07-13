package net.team.helldivers.item;


import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HelldiversMod.MOD_ID);

    public static final RegistryObject<Item> EFFECT_TESTER = ITEMS.register("effect_tester",
            () -> new EffectTesterItem(new Item.Properties()));

    public static final RegistryObject<Item> STRATAGEM_ORB = ITEMS.register("stratagem_orb",
            () -> new StratagemOrbItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> TITANIUM = ITEMS.register("titanium",
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

    public static final RegistryObject<Item> B01_HELMET = ITEMS.register("b01_helmet",
            () -> new B01ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL , ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> B01_CHESTPLATE = ITEMS.register("b01_chestplate",
            () -> new B01ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> B01_LEGGINGS = ITEMS.register("b01_leggings",
            () -> new B01ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> B01_BOOTS = ITEMS.register("b01_boots",
            () -> new B01ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> FS05_HELMET = ITEMS.register("fs05_helmet",
            () -> new Fs05ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL , ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> FS05_CHESTPLATE = ITEMS.register("fs05_chestplate",
            () -> new Fs05ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> FS05_LEGGINGS = ITEMS.register("fs05_leggings",
            () -> new Fs05ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> FS05_BOOTS = ITEMS.register("fs05_boots",
            () -> new Fs05ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> STRATAGEM_PICKER = ITEMS.register("stratagem_picker",
            () -> new StratagemPickerItem(new Item.Properties().stacksTo(1).fireResistant()));

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
