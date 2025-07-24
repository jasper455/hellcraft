package net.team.helldivers.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.ForgeRegistries;
import net.team.helldivers.HelldiversMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.team.helldivers.worldgen.ModBiomeModifiers;
import net.team.helldivers.worldgen.ModConfiguredFeatures;
import net.team.helldivers.worldgen.ModPlacedFeatures;
import net.team.helldivers.worldgen.biome.ModBiomes;
import net.team.helldivers.worldgen.dimension.ModDimensions;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem)
            .add(Registries.BIOME, ModBiomes::bootstrap);

    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(HelldiversMod.MOD_ID));
    }

    @Override
    public String getName() {
        return "Datapack Entries";
    }
}