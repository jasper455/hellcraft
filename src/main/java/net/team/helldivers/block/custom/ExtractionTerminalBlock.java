package net.team.helldivers.block.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.screen.custom.ExtractionTerminalMenu;
import org.jetbrains.annotations.Nullable;

public class ExtractionTerminalBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ExtractionTerminalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.EXTRACTION_TERMINAL.get().create(pPos, pState) ;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ExtractionTerminalBlockEntity extractionTerminal && !player.isCrouching()) {
            if (player instanceof ServerPlayer serverPlayer) {
                Container inventory = extractionTerminal.getPlayerInventory(player);

                NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) -> new ExtractionTerminalMenu(
                                containerId, playerInventory, inventory, pos),
                        Component.literal("Extraction Terminal")
                ), pos);
            }
        }

        if (player.isCrouching()) {
            player.sendSystemMessage(Component.literal(String.valueOf(getExtractionTerminalItem(player, 0))));
        }


        return InteractionResult.CONSUME;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
            case NORTH -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
            case EAST -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
            case WEST -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    public ItemStack getExtractionTerminalItem(Player player, int slot) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag forgeData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag extractionData = forgeData.getCompound("ExtractionInventory");

        if (extractionData.contains("Items")) {
            ListTag items = extractionData.getList("Items", 10);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag itemTag = items.getCompound(i);
                int itemSlot = itemTag.getByte("Slot") & 255;
                if (itemSlot == slot) {
                    return ItemStack.of(itemTag);
                }
            }
        }

        return ItemStack.EMPTY;
    }

}
