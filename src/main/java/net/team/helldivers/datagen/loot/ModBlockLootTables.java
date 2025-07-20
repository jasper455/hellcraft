package net.team.helldivers.datagen.loot;

import net.team.helldivers.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {


    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropOther(ModBlocks.HELLBOMB.get(), Items.AIR);
        dropSelf(ModBlocks.AMMO_CRATE.get());
        dropSelf(ModBlocks.BARBED_WIRE.get());
        dropOther(ModBlocks.EXTRACTION_TERMINAL.get(), ModItems.EXTRACTION_TERMINAL_BLOCK_ITEM.get());
        dropSelf(ModBlocks.COMMON_SAMPLE.get());
        dropSelf(ModBlocks.RARE_SAMPLE.get());
        dropSelf(ModBlocks.SUPER_SAMPLE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
