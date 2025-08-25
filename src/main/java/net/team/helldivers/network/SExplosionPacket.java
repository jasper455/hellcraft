package net.team.helldivers.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.gamerule.ModGameRules;
import net.team.helldivers.particle.EffekLoader;

import java.util.function.Supplier;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;

public class SExplosionPacket {
    private final BlockPos position;
    private final int radius;
    private final boolean isNapalm;

    public SExplosionPacket(BlockPos position, int radius, boolean isNapalm) {
        this.position = position;
        this.radius = radius;
        this.isNapalm = isNapalm;
    }

    public SExplosionPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
        buffer.writeInt(this.radius);
        buffer.writeBoolean(this.isNapalm);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        boolean doFlyingBlocks = player.level().getGameRules().getBoolean(ModGameRules.DO_FLYING_BLOCKS);
        if (!isNapalm) {
            ParticleEmitterInfo boom = EffekLoader.BLAST.clone().position(position.getCenter()).scale(radius);
            AAALevel.addParticle(player.level(), 256, boom);
            if (!doFlyingBlocks) {
                player.level().explode(null, position.getX(), position.getY(), position.getZ(), radius, false, Level.ExplosionInteraction.BLOCK);
            } else {
                player.level().explode(null, position.getX(), position.getY(), position.getZ(), radius, false, Level.ExplosionInteraction.BLOCK);
                flyingBlocksExplosion(player.level(), position, radius / 2);
            }
        } else {
            ParticleEmitterInfo boom = EffekLoader.NAPALM_BURST.clone().position(position.getCenter()).scale(radius);
            AAALevel.addParticle(player.level(), 256, boom);
            player.level().explode(null, position.getX(), position.getY(), position.getZ(), radius, true, Level.ExplosionInteraction.NONE);
        }
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

                        int randomNumber = Mth.randomBetweenInclusive(RandomSource.create(), 0, 1);

                        level.removeBlock(targetPos, false);

                        if (randomNumber == 0) {
                            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, targetPos, state);

                            fallingBlock.setPos(targetPos.getX() + 0.5, targetPos.getY() + 10, targetPos.getZ() + 0.5);
                            int flyingBlocksIntensity = level.getGameRules().getInt(ModGameRules.FLYING_BLOCKS_INTENSITY);

                            Vec3 vec3 = new Vec3(
                                    Mth.randomBetween(RandomSource.create(), -flyingBlocksIntensity, flyingBlocksIntensity),
                                    Mth.randomBetween(RandomSource.create(), 0, flyingBlocksIntensity / 2f),
                                    Mth.randomBetween(RandomSource.create(), -flyingBlocksIntensity, flyingBlocksIntensity)
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