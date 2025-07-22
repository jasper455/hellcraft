package net.team.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SHellpodDestroyBlocksPacket {
    private final BlockPos position;
    private final int radius;

    public SHellpodDestroyBlocksPacket(BlockPos position, int radius) {
        this.position = position;
        this.radius = radius;
    }

    public SHellpodDestroyBlocksPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.position);
        buffer.writeInt(this.radius);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        destroyBlocks(player.level(), position, radius);
    }

    public void destroyBlocks(Level level, BlockPos center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= radius) {
                        BlockPos targetPos = center.offset(x, y, z);
                        BlockState state = level.getBlockState(targetPos);

                        if (state.isAir() || state.getDestroySpeed(level, targetPos) == -1.0f) continue;

                        if (state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)) {
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }
}