package net.team.helldivers.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.custom.B01ArmorItem;
import net.team.helldivers.item.custom.Dp40ArmorItem;
import net.team.helldivers.item.custom.Fs05ArmorItem;

public class ModArmorItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HelldiversMod.MOD_ID);

    // B0-1 Tactical Armor
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

    // FS-05 Marksman Armor
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

    // DP-40 Hero of The Federation Armor
    public static final RegistryObject<Item> DP40_HELMET = ITEMS.register("dp40_helmet",
            () -> new Dp40ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL , ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> DP40_CHESTPLATE = ITEMS.register("dp40_chestplate",
            () -> new Dp40ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> DP40_LEGGINGS = ITEMS.register("dp40_leggings",
            () -> new Dp40ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> DP40_BOOTS = ITEMS.register("dp40_boots",
            () -> new Dp40ArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1).fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
