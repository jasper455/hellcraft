package net.team.helldivers.block;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.custom.*;
import net.team.helldivers.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HelldiversMod.MOD_ID);

    public static final RegistryObject<Block> HELLBOMB = registerBlock("hellbomb",
            () -> new HellbombBlock());

    public static final RegistryObject<Block> EXTRACTION_TERMINAL = registerBlock("extraction_terminal",
            () -> new ExtractionTerminalBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().lightLevel((level) -> 5)));

    public static final RegistryObject<Block> AMMO_CRATE = registerBlock("ammo_crate",
            () -> new AmmoCrateBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)));

    public static final RegistryObject<Block> BARBED_WIRE = registerBlock("barbed_wire",
            () -> new BarbedWireBlock(BlockBehaviour.Properties.of().instabreak().noCollission().speedFactor(0.0125f)
                    .noOcclusion().lightLevel((level) -> 2).sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> COMMON_SAMPLE = registerBlock("common_sample",
            () -> new CommonSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)));

    public static final RegistryObject<Block> RARE_SAMPLE = registerBlock("rare_sample",
            () -> new RareSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)));

    public static final RegistryObject<Block> SUPER_SAMPLE = registerBlock("super_sample",
            () -> new SuperSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)));

    public static final RegistryObject<Block> BOT_CONTACT_MINE = registerBlock("bot_contact_mine",
            () -> new BotContactMineBlock(BlockBehaviour.Properties.of().instabreak().noCollission().lightLevel((level) -> 5)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> toReturn) {
            ModItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
