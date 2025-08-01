package net.team.helldivers.datagen;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.ModArmorItems;
import net.team.helldivers.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HelldiversMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.TRAP_ROYALTY_MUSIC_DISC.get());

        basicItem(ModItems.EFFECT_TESTER.get());

        basicItem(ModArmorItems.B01_HELMET.get());
        basicItem(ModArmorItems.B01_CHESTPLATE.get());
        basicItem(ModArmorItems.B01_LEGGINGS.get());
        basicItem(ModArmorItems.B01_BOOTS.get());

        basicItem(ModArmorItems.FS05_HELMET.get());
        basicItem(ModArmorItems.FS05_CHESTPLATE.get());
        basicItem(ModArmorItems.FS05_LEGGINGS.get());
        basicItem(ModArmorItems.FS05_BOOTS.get());

        basicItem(ModArmorItems.DP40_HELMET.get());
        basicItem(ModArmorItems.DP40_CHESTPLATE.get());
        basicItem(ModArmorItems.DP40_LEGGINGS.get());
        basicItem(ModArmorItems.DP40_BOOTS.get());

        basicItem(ModArmorItems.SC30_HELMET.get());
        basicItem(ModArmorItems.SC30_CHESTPLATE.get());
        basicItem(ModArmorItems.SC30_LEGGINGS.get());
        basicItem(ModArmorItems.SC30_BOOTS.get());

        basicItem(ModItems.HELLBOMB_ITEM.get());
        basicItem(ModItems.RESUPPLY.get());
        basicItem(ModItems.ANTI_TANK_STRATAGEM.get());
        basicItem(ModItems.STALWART_STRATAGEM.get());
        basicItem(ModItems.AMR_STRATAGEM.get());
        basicItem(ModItems.RESUPPLY.get());
        basicItem(ModItems.SMALL_BARRAGE.get());
        basicItem(ModItems.BIG_BARRAGE.get());
        basicItem(ModItems.PRECISION_STRIKE.get());
        basicItem(ModItems.ORBITAL_LASER.get());
        basicItem(ModItems.NAPALM_BARRAGE.get());
        basicItem(ModItems.WALKING_BARRAGE.get());
        basicItem(ModItems.EAGLE_500KG_BOMB.get());
        basicItem(ModItems.CLUSTER_BOMB.get());
        basicItem(ModItems.EAGLE_AIRSTRIKE.get());
        basicItem(ModItems.NAPALM_AIRSTRIKE.get());
    }


    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "block/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "item/" + item.getId().getPath()));
    }

}
