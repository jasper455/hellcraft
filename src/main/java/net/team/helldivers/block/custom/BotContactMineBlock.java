package net.team.helldivers.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.team.helldivers.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class BotContactMineBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BotContactMineBlock(Properties properties) {
        super(properties.mapColor(MapColor.METAL).sound(SoundType.LANTERN).strength(15f, 1f).lightLevel(s -> 1)
                .requiresCorrectToolForDrops().noOcclusion().pushReaction(PushReaction.IGNORE).hasPostProcess((bs, br, bp) -> true)
                .emissiveRendering((bs, br, bp) -> true).isRedstoneConductor((bs, br, bp) -> false));

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.BOT_CONTACT_MINE.get().create(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(0, 0, 0, 16, 7.5, 16);
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

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        super.entityInside(pState, pLevel, pPos, pEntity);
        pLevel.explode(null, pPos.getX(), pPos.getY(), pPos.getZ(), 3, true, Level.ExplosionInteraction.BLOCK);
        pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
        pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pPos).inflate(3.0)).forEach(entity -> {
            entity.hurt(pLevel.damageSources().explosion(null), 12.5F);
        });
    }
}