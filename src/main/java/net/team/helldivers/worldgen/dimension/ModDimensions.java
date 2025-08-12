package net.team.helldivers.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.worldgen.biome.ModBiomes;
import net.team.helldivers.worldgen.chunk.PrebuiltChunkGenerator;
import net.team.helldivers.worldgen.chunk.PrebuiltChunkGeneratorSettings;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> CHOEPESSA_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "choepessa_iv"));
    public static final ResourceKey<Level> CHOEPESSA_DIM = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "choepessa_iv"));
    public static final ResourceKey<DimensionType> CHOEPESSA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "choepessa_dim_type"));
    public static final ResourceKey<LevelStem> SUPER_DESTROYER_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "super_destroyer"));
    public static final ResourceKey<Level> SUPER_DESTROYER_DIM = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "super_destroyer"));
    public static final ResourceKey<DimensionType> SUPER_DESTROYER_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "super_destroyer_dim_type"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(CHOEPESSA_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000),
                false,
                false,
                false,
                false,
                1.0,
                true,
                false,
                0,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.END_EFFECTS,
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
        ));
        context.register(SUPER_DESTROYER_DIM_TYPE, new DimensionType(
                OptionalLong.of(18000),
                false,
                false,
                false,
                false,
                1.0,
                true,
                false,
                0,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.END_EFFECTS,
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
        ));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.CHOEPESSA_WASTES)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
                                        biomeRegistry.getOrThrow(Biomes.THE_VOID)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F),
                                        biomeRegistry.getOrThrow(Biomes.THE_VOID)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F),
                                        biomeRegistry.getOrThrow(Biomes.THE_VOID)),
                                Pair.of(
                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F),
                                        biomeRegistry.getOrThrow(Biomes.THE_VOID))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem choepessaStem = new LevelStem(dimTypes.getOrThrow(ModDimensions.CHOEPESSA_DIM_TYPE), wrappedChunkGenerator);
        context.register(CHOEPESSA_KEY, choepessaStem);
    }

}
