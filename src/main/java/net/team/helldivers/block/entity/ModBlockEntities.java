package net.team.helldivers.block.entity;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.block.entity.custom.BotContactMineBlockEntity;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.GeckoLib;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, HelldiversMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<HellbombBlockEntity>> HELLBOMB = BLOCK_ENTITIES
            .register("hellbomb", () -> BlockEntityType.Builder
                    .of(HellbombBlockEntity::new, ModBlocks.HELLBOMB.get()).build(null));

    public static final RegistryObject<BlockEntityType<ExtractionTerminalBlockEntity>> EXTRACTION_TERMINAL = BLOCK_ENTITIES
            .register("extraction_terminal", () -> BlockEntityType.Builder
                    .of(ExtractionTerminalBlockEntity::new, ModBlocks.EXTRACTION_TERMINAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<BotContactMineBlockEntity>> BOT_CONTACT_MINE = BLOCK_ENTITIES
            .register("bot_contact_mine", () -> BlockEntityType.Builder
                    .of(BotContactMineBlockEntity::new, ModBlocks.BOT_CONTACT_MINE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
