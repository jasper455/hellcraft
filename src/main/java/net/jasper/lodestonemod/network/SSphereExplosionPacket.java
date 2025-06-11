package net.jasper.lodestonemod.network;

import net.jasper.lodestonemod.event.ModClientEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;
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
        customSphereExplosion(player.level(), position, radius);
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
}