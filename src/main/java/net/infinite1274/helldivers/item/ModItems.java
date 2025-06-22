package net.infinite1274.helldivers.item;


import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.item.custom.EffectTesterItem;
import net.infinite1274.helldivers.item.custom.HelldiverArmorItem;
import net.infinite1274.helldivers.item.custom.StratagemOrbItem;
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
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HELLDIVER_HELMET = ITEMS.register("helldiver_helmet",
            () -> new HelldiverArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL , ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HELLDIVER_CHESTPLATE = ITEMS.register("helldiver_chestplate",
            () -> new HelldiverArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HELLDIVER_LEGGINGS = ITEMS.register("helldiver_leggings",
            () -> new HelldiverArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HELLDIVER_BOOTS = ITEMS.register("helldiver_boots",
            () -> new HelldiverArmorItem(ModArmorMaterials.HELLDIVER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
