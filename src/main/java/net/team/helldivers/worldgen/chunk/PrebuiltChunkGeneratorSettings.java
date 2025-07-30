package net.team.helldivers.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.team.helldivers.HelldiversMod;

import java.util.Optional;

public class PrebuiltChunkGeneratorSettings {

    public static final Codec<PrebuiltChunkGeneratorSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Biome.CODEC.optionalFieldOf("biome").forGetter(settings ->
                            Optional.of(settings.getBiome())
                    ),
                    RegistryOps.retrieveElement(Registries.BIOME) // ⚠️ Depends on context – may need manual input
            ).apply(instance, (biomeOpt, biomeGetter) ->
                    new PrebuiltChunkGeneratorSettings(
                            biomeOpt,
                            biomeGetter.value().getHolderOrThrow(Biomes.THE_VOID) // Fallback biome
                    )
            )
    );

    private final Holder<Biome> biome;

    public PrebuiltChunkGeneratorSettings(Optional<Holder<Biome>> biomeHolder, Holder.Reference<Biome> defaultBiome) {
        if (biomeHolder.isEmpty()) {
            HelldiversMod.LOGGER.error("Unknown biome, defaulting to the_void");
            this.biome = defaultBiome;
        } else {
            this.biome = biomeHolder.get();
        }
    }

    public NoiseGeneratorSettings adjustGenerationSettings(Holder<Biome> biome) {
        if (!biome.equals(this.biome)) {
            // Pick a different generation setting for other biomes
            return NoiseGeneratorSettings.dummy(); // or whatever your default is
        } else {
            // Return void/no terrain gen settings
            return NoiseGeneratorSettings.dummy(); // good minimal preset, or create your own
        }
    }

    public Holder<Biome> getBiome() {
        return biome;
    }
}