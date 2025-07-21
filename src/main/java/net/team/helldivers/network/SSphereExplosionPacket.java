package net.team.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.gamerule.ModGameRules;

import java.util.function.Supplier;

public class SSphereExplosionPacket {
    private final BlockPos position;
    private final int radius;

    public SSphereExplosionPacket(BlockPos position, int radius) {
        this.position = position;
        this.radius = radius;
    }

    public SSphereExplosionPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
        buffer.writeInt(this.radius);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        boolean doFlyingBlocks = player.level().getGameRules().getBoolean(ModGameRules.DO_FLYING_BLOCKS);
        if (!doFlyingBlocks) {
            customSphereExplosion(player.level(), position, radius);
        } else {
            flyingBlocksExplosion(player.level(), position, radius);
        }
        player.level().explode(player, position.getX(), position.getY(), position.getZ(), radius * 0.75f, true, Level.ExplosionInteraction.BLOCK);
    }

    public boolean customSphereExplosion(Level level, BlockPos center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= radius) {
                        BlockPos targetPos = center.offset(x, y, z);
                        BlockState state = level.getBlockState(targetPos);

                        if (state.isAir() || state.getDestroySpeed(level, targetPos) == -1.0f) continue;

                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        return true;
    }

    public void flyingBlocksExplosion(Level level, BlockPos center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= radius) {
                        BlockPos targetPos = center.offset(x, y, z);
//                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

                        BlockState state = level.getBlockState(targetPos);
                        if (state.isAir() || state.is(Blocks.BEDROCK)) continue;
                        if (state.liquid()) {
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                            continue;
                        }

                        int randomNumber = Mth.randomBetweenInclusive(RandomSource.create(), 0, 18);

                        level.removeBlock(targetPos, false);

                        if (randomNumber == 0) {
                        FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, targetPos, state);

                        fallingBlock.setPos(targetPos.getX() + 0.5, targetPos.getY() + 10, targetPos.getZ() + 0.5);

                        Vec3 vec3 = new Vec3(
                                Mth.randomBetween(RandomSource.create(), -5, 5),
                                Mth.randomBetween(RandomSource.create(), 0, 2.5f),
                                Mth.randomBetween(RandomSource.create(), -5, 5)
                        );

                        fallingBlock.setDeltaMovement(vec3);

                        level.addFreshEntity(fallingBlock);
                        }
                    }
                }
            }
        }
    }
}