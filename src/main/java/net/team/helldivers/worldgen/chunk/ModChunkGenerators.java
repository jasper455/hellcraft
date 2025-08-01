package net.team.helldivers.worldgen.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;

public class ModChunkGenerators {
    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, HelldiversMod.MOD_ID);

    public static final RegistryObject<Codec<PrebuiltChunkGenerator>> PREBUILT =
            CHUNK_GENERATORS.register("prebuilt", () -> PrebuiltChunkGenerator.CODEC);

    public static void register(IEventBus eventBus) {
        CHUNK_GENERATORS.register(eventBus);
    }
}