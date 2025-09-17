package net.team.helldivers.datagen.loot;

import net.team.helldivers.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.block.custom.samples.ModSampleBlocks;
import net.team.helldivers.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {


    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropOther(ModBlocks.HELLBOMB.get(), Items.AIR);
        dropOther(ModBlocks.STRATAGEM_JAMMER.get(), Items.AIR);
        dropSelf(ModBlocks.AMMO_CRATE.get());
        dropSelf(ModBlocks.BARBED_WIRE.get());
        dropSelf(ModBlocks.SUPER_DESTROYER_GLASS.get());
        dropOther(ModBlocks.AUTOMATON_FABRICATOR_SPAWNER.get(), Items.AIR);
        dropOther(ModBlocks.AUTOMATON_AREA_SPAWNER.get(), Items.AIR);
        dropOther(ModBlocks.EXTRACTION_TERMINAL.get(), ModItems.EXTRACTION_TERMINAL_BLOCK_ITEM.get());
        dropOther(ModBlocks.GALACTIC_TERMINAL.get(), ModItems.GALACTIC_TERMINAL_BLOCK_ITEM.get());
        dropOther(ModBlocks.BOT_CONTACT_MINE.get(), ModItems.BOT_CONTACT_MINE_BLOCK_ITEM.get());
        dropOther(ModSampleBlocks.COMMON_SAMPLE_CONTAINER.get(), ModItems.COMMON_SAMPLE.get());
        dropOther(ModSampleBlocks.BOT_COMMON_SAMPLE.get(), ModItems.COMMON_SAMPLE.get());
        dropOther(ModSampleBlocks.RARE_SAMPLE_CRYSTAL.get(), ModItems.RARE_SAMPLE.get());
        dropOther(ModSampleBlocks.SUPER_SAMPLE_CRYSTAL.get(), ModItems.SUPER_SAMPLE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> allBlocks = new ArrayList<>();

        ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(allBlocks::add);
        ModSampleBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(allBlocks::add);

        return allBlocks;
    }
}
