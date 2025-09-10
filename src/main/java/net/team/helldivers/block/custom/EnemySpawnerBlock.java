package net.team.helldivers.block.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Supplier;

public class EnemySpawnerBlock extends Block {
    private final List<Supplier<EntityType<? extends LivingEntity>>> entitySuppliers;
    private final boolean isAreaSpawn;
    private final int maxEnemiesToSpawn;

    public EnemySpawnerBlock(List<Supplier<EntityType<? extends LivingEntity>>> entitySuppliers, boolean isAreaSpawn, int maxEnemiesToSpawn) {
        super(Properties.of().randomTicks().noCollission());
        this.entitySuppliers = entitySuppliers;
        this.isAreaSpawn = isAreaSpawn;
        this.maxEnemiesToSpawn = maxEnemiesToSpawn;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.isAreaLoaded(pos, 1)) return;

        // Choose number of enemies to spawn, between 1 and maxEnemiesToSpawn
        int enemiesToSpawn = Mth.randomBetweenInclusive(random, 1, maxEnemiesToSpawn);

        for (int i = 0; i < (isAreaSpawn ? maxEnemiesToSpawn : enemiesToSpawn); i++) {
            // Randomly pick one entity type from the pool
            Supplier<EntityType<? extends LivingEntity>> supplier = entitySuppliers.get(random.nextInt(entitySuppliers.size()));
            EntityType<? extends LivingEntity> type = supplier.get();

            LivingEntity entity = type.create(level);
            if (entity != null) {
                int randomX = Mth.randomBetweenInclusive(random, -5, 5);
                int randomZ = Mth.randomBetweenInclusive(random, -5, 5);
                double spawnX = pos.getX() + (isAreaSpawn ? randomX : 0.5);
                double spawnZ = pos.getZ() + (isAreaSpawn ? randomZ : 0.5);

                entity.moveTo(spawnX, pos.getY(), spawnZ);
                level.addFreshEntity(entity);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext) {
            if (entityContext.getEntity() instanceof Player player) {
                if (player.isCreative()) {
                    return Shapes.block(); // Full cube outline for Creative players
                }
            }
        }
        return Shapes.empty(); // No outline for everyone else
    }

}