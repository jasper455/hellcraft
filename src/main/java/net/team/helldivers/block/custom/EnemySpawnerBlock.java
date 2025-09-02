package net.team.helldivers.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.team.helldivers.entity.custom.bots.AbstractBotEntity;

import java.util.List;
import java.util.function.Supplier;

public class EnemySpawnerBlock extends Block {
    private final List<Supplier<EntityType<? extends LivingEntity>>> entitySuppliers;
    private final boolean someFlag;

    public EnemySpawnerBlock(List<Supplier<EntityType<? extends LivingEntity>>> entitySuppliers, boolean someFlag) {
        super(Properties.of().randomTicks());
        this.entitySuppliers = entitySuppliers;
        this.someFlag = someFlag;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.isAreaLoaded(pos, 1)) return;

        for (Supplier<EntityType<? extends LivingEntity>> supplier : entitySuppliers) {
            EntityType<? extends LivingEntity> type = supplier.get();
            LivingEntity entity = type.create(level);
            if (entity != null) {
                entity.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                level.addFreshEntity(entity);
            }
        }
    }
}