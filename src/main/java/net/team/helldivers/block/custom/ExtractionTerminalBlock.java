package net.team.helldivers.block.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import net.team.helldivers.event.ModClientEvents;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.STeleportToDimensionPacket;
import net.team.helldivers.screen.custom.ExtractionTerminalMenu;
import net.team.helldivers.screen.custom.GalaxyMapMenu;
import net.team.helldivers.screen.custom.SupportHellpodMenu;
import net.team.helldivers.worldgen.dimension.ModDimensions;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ExtractionTerminalBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ExtractionTerminalBlock(Properties properties) {
        super(properties.noOcclusion());
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
        if (blockEntity instanceof ExtractionTerminalBlockEntity extractionTerminal) {
            if (player instanceof ServerPlayer serverPlayer) {
                Container inventory = extractionTerminal.getPlayerInventory(player);
                if (!player.isShiftKeyDown()) {
                    NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                            (containerId, playerInventory, playerEntity) -> new ExtractionTerminalMenu(
                                    containerId, playerInventory, inventory, pos),
                            Component.literal("Extraction Terminal")
                    ), pos);
                } else {
                    ModClientEvents.triggerFlashEffect(0.0005f, new Color(0, 0, 0));
                    PacketHandler.sendToServer(new STeleportToDimensionPacket(ModDimensions.SUPER_DESTROYER_DIM.location()));
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
            case EAST -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
            case WEST -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
            default -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(5, 2, 5, 11, 21, 11), box(6, 21, 6, 10, 40, 10));
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
}
