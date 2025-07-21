package net.team.helldivers.worldgen.biome;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.team.helldivers.HelldiversMod;

public class ModBiomes {
    public static final ResourceKey<Biome> CHOEPESSA_WASTES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "choepessa_wastes"));

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(CHOEPESSA_WASTES, choepessa(context));
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome choepessa(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

//        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        globalOverworldGeneration(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_PACKED);
        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_BLUE);
        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.FOREST_ROCK);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.DISK_GRAVEL);

        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, CavePlacements.FOSSIL_LOWER);
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, CavePlacements.FOSSIL_UPPER);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);


        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.5f)
                .temperature(0f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x386693)
                        .waterFogColor(0x386693)
                        .skyColor(0xccffff)
                        .grassColorOverride(0xcd7d6a)
                        .foliageColorOverride(0xcd7d6a)
                        .fogColor(0xffffff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FLOWER_FOREST))
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.SNOWFLAKE, 0.002f))
                        .build())
                .build();
    }

}
