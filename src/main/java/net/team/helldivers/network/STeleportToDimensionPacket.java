package net.team.helldivers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.ModEntities;

import java.util.function.Supplier;

public class STeleportToDimensionPacket {
    private final ResourceLocation dimension;

    public STeleportToDimensionPacket(ResourceLocation id) {
        this.dimension = id;
    }

    public STeleportToDimensionPacket(FriendlyByteBuf buffer) {
        this(buffer.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(dimension);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        int randomX = Mth.randomBetweenInclusive(player.level().getRandom(), -5, 5);
        int randomZ = Mth.randomBetweenInclusive(player.level().getRandom(), -5, 5);

        ResourceKey<Level> targetDimension = ResourceKey.create(Registries.DIMENSION, dimension);

        ServerLevel destination = player.server.getLevel(targetDimension);
        if (destination != null && player.level().dimension() != targetDimension) {
            player.teleportTo(destination, randomX, 200, randomZ, 0, 0);
            Entity hellpod = ModEntities.HELLPOD.get().create(destination);
            if (hellpod != null) {
                hellpod.setPos(randomX, 200, randomZ);
                destination.addFreshEntity(hellpod);
                player.startRiding(hellpod, true);
            }
            player.closeContainer();
        } else if (player.level().dimension() == targetDimension && destination != null) {
            player.displayClientMessage(Component.literal("Already on this world!"), true);
            player.closeContainer();
        }
    }
}