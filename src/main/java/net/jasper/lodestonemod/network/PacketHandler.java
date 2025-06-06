package net.jasper.lodestonemod.network;


import net.jasper.lodestonemod.LodestoneMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(LodestoneMod.MOD_ID, "main"))
            .serverAcceptedVersions((status) -> true)
            .clientAcceptedVersions((status) -> true)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(SSphereExplosionPacket.class, NetworkDirection.PLAY_TO_SERVER.ordinal())
                .encoder(SSphereExplosionPacket::encode)
                .decoder(SSphereExplosionPacket::new)
                .consumerMainThread(SSphereExplosionPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send((PacketDistributor.PacketTarget) msg, PacketDistributor.PLAYER.with(() -> player));
    }

    public static void sendToAllClients(Object msg) {
        INSTANCE.send((PacketDistributor.PacketTarget) msg, PacketDistributor.ALL.noArg());
    }
}