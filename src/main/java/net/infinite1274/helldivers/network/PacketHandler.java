package net.infinite1274.helldivers.network;


import net.infinite1274.helldivers.HelldiversMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "main"))
            .serverAcceptedVersions((status) -> true)
            .clientAcceptedVersions((status) -> true)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    public static void register() {
        int index = 0;

        // SERVER
        INSTANCE.messageBuilder(SSphereExplosionPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SSphereExplosionPacket::encode)
                .decoder(SSphereExplosionPacket::new)
                .consumerMainThread(SSphereExplosionPacket::handle)
                .add();

        INSTANCE.messageBuilder(SExplosionPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SExplosionPacket::encode)
                .decoder(SExplosionPacket::new)
                .consumerMainThread(SExplosionPacket::handle)
                .add();

        // CLIENT
        INSTANCE.messageBuilder(CSmallExplosionParticlesPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CSmallExplosionParticlesPacket::encode)
                .decoder(CSmallExplosionParticlesPacket::new)
                .consumerMainThread(CSmallExplosionParticlesPacket::handle)
                .add();

        INSTANCE.messageBuilder(CLargeExplosionParticlesPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CLargeExplosionParticlesPacket::encode)
                .decoder(CLargeExplosionParticlesPacket::new)
                .consumerMainThread(CLargeExplosionParticlesPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), msg);
    }

    public static void sendToAllClients(Object msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}