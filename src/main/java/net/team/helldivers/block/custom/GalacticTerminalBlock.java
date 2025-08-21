package net.team.helldivers.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.block.entity.custom.GalacticTerminalBlockEntity;
import net.team.helldivers.screen.custom.StratagemSelectMenu;
import net.team.helldivers.screen.custom.GalaxyMapMenu;
import org.jetbrains.annotations.Nullable;

public class GalacticTerminalBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public GalacticTerminalBlock(Properties properties) {
        super(properties.noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.GALACTIC_TERMINAL.get().create(pPos, pState) ;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof GalacticTerminalBlockEntity galacticTerminal) {
            if (player instanceof ServerPlayer serverPlayer) {
                Container inventory = galacticTerminal.getPlayerInventory(player);
                if (player.isShiftKeyDown()) {
                    NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                            (containerId, playerInventory, playerEntity) -> new StratagemSelectMenu(
                                    containerId, playerInventory, inventory, pos),
                            Component.literal("Extraction Terminal")
                    ), pos);
                } else {
                    NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                            (containerId, playerInventory, playerEntity) -> new GalaxyMapMenu(
                                    containerId, playerInventory, blockEntity),
                            Component.literal("Galaxy Map")
                    ), pos);
                }
            }
        }

        return InteractionResult.CONSUME;
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
