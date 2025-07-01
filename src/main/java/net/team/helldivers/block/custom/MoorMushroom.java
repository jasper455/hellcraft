package net.team.helldivers.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.team.helldivers.block.ModBlocks;

public class MoorMushroom {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ModBlocks.MOD_ID);


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
