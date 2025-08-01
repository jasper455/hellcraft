package net.team.helldivers.block.custom.samples;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.custom.*;
import net.team.helldivers.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ModSampleBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HelldiversMod.MOD_ID);

    public static final RegistryObject<Block> COMMON_SAMPLE_CONTAINER = registerBlock("common_sample_container",
            () -> new CommonSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)) {
                @Override
                public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
                    return switch (state.getValue(FACING)) {
                        case NORTH -> box(4, 0, 6, 12, 2, 11);
                        case EAST -> box(5, 0, 4, 10, 2, 12);
                        case WEST -> box(6, 0, 4, 11, 2, 12);
                        default -> box(4, 0, 5, 12, 2, 10);
                    };
                }
            });

    public static final RegistryObject<Block> BOT_COMMON_SAMPLE = registerBlock("bot_common_sample",
            () -> new CommonSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)) {
                @Override
                public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
                    return switch (state.getValue(FACING)) {
                        case NORTH -> box(6, 0, 4, 10, 8, 12);
                        case EAST -> box(4, 0, 6, 12, 8, 10);
                        case WEST -> box(4, 0, 6, 12, 8, 10);
                        default -> box(6, 0, 4, 10, 8, 12);
                    };
                }
            });

    public static final RegistryObject<Block> RARE_SAMPLE_CRYSTAL = registerBlock("rare_sample_crystal",
            () -> new RareSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)) {
                @Override
                public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
                    return switch (state.getValue(FACING)) {
                        case NORTH -> box(5, 0, 6, 10, 7, 11);
                        case EAST -> box(5, 0, 5.5, 10, 7, 10.5);
                        case WEST -> box(6, 0, 5.5, 11, 7, 10.5);
                        default -> box(5, 0, 5.5, 10, 7, 10.5);
                    };
                }
            });

    public static final RegistryObject<Block> SUPER_SAMPLE_CRYSTAL = registerBlock("super_sample_crystal",
            () -> new SuperSampleBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().lightLevel((level) -> 5)) {
                @Override
                public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
                    return switch (state.getValue(FACING)) {
                        case NORTH -> box(5, 0, 6, 10, 7, 11);
                        case EAST -> box(5, 0, 5.5, 10, 7, 10.5);
                        case WEST -> box(6, 0, 5.5, 11, 7, 10.5);
                        default -> box(5, 0, 5.5, 10, 7, 10.5);
                    };
                }
            });

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
