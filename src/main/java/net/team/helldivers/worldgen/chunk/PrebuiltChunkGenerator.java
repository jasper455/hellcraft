package net.team.helldivers.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PrebuiltChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<PrebuiltChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PrebuiltChunkGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings)
    ).apply(instance, PrebuiltChunkGenerator::new));

    public static Schematic schematic; // your custom schematic
    private final PrebuiltChunkGeneratorSettings settings;

    public PrebuiltChunkGenerator(PrebuiltChunkGeneratorSettings settings) {
        super(new FixedBiomeSource(settings.getBiome()), Holder.direct(settings.adjustGenerationSettings(settings.getBiome())));
        this.settings = settings;
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState state, ChunkAccess chunk) {
        // Do nothing: no terrain generation
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        if (schematic != null) {
            schematic.placeChunk(chunk); // custom method
        }
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
}