package net.team.helldivers.block;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.custom.HellbombBlock;
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
