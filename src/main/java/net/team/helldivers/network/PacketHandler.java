package net.team.helldivers.network;


import net.team.helldivers.HelldiversMod;
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

        INSTANCE.messageBuilder(SGiveStratagemOrbPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SGiveStratagemOrbPacket::encode)
                .decoder(SGiveStratagemOrbPacket::new)
                .consumerMainThread(SGiveStratagemOrbPacket::handle)
                .add();

        INSTANCE.messageBuilder(SOrbitalBarragePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SOrbitalBarragePacket::encode)
                .decoder(SOrbitalBarragePacket::new)
                .consumerMainThread(SOrbitalBarragePacket::handle)
                .add();

        INSTANCE.messageBuilder(SShootPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SShootPacket::encode)
                .decoder(SShootPacket::new)
                .consumerMainThread(SShootPacket::handle)
                .add();

        INSTANCE.messageBuilder(SStartShootPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SStartShootPacket::encode)
                .decoder(SStartShootPacket::new)
                .consumerMainThread(SStartShootPacket::handle)
                .add();
        
        INSTANCE.messageBuilder(SStopShootPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SStopShootPacket::encode)
                .decoder(SStopShootPacket::new)
                .consumerMainThread(SStopShootPacket::handle)
                .add();

        INSTANCE.messageBuilder(SGunReloadPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SGunReloadPacket::encode)
                .decoder(SGunReloadPacket::new)
                .consumerMainThread(SGunReloadPacket::handle)
                .add();

        INSTANCE.messageBuilder(SHellbombExplodePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SHellbombExplodePacket::encode)
                .decoder(SHellbombExplodePacket::new)
                .consumerMainThread(SHellbombExplodePacket::handle)
                .add();

        INSTANCE.messageBuilder(SItemGiveCooldownPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SItemGiveCooldownPacket::encode)
                .decoder(SItemGiveCooldownPacket::new)
                .consumerMainThread(SItemGiveCooldownPacket::handle)
                .add();

        INSTANCE.messageBuilder(SHellbombActivatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SHellbombActivatePacket::encode)
                .decoder(SHellbombActivatePacket::new)
                .consumerMainThread(SHellbombActivatePacket::handle)
                .add();

        INSTANCE.messageBuilder(SInitializeExtractionTerminalInventoryPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SInitializeExtractionTerminalInventoryPacket::encode)
                .decoder(SInitializeExtractionTerminalInventoryPacket::new)
                .consumerMainThread(SInitializeExtractionTerminalInventoryPacket::handle)
                .add();

        INSTANCE.messageBuilder(SHellpodDestroyBlocksPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SHellpodDestroyBlocksPacket::encode)
                .decoder(SHellpodDestroyBlocksPacket::new)
                .consumerMainThread(SHellpodDestroyBlocksPacket::handle)
                .add();

        INSTANCE.messageBuilder(SSyncJammedPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SSyncJammedPacket::encode)
                .decoder(SSyncJammedPacket::new)
                .consumerMainThread(SSyncJammedPacket::handle)
                .add();

        INSTANCE.messageBuilder(STeleportToDimensionPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(STeleportToDimensionPacket::encode)
                .decoder(STeleportToDimensionPacket::new)
                .consumerMainThread(STeleportToDimensionPacket::handle)
                .add();

        INSTANCE.messageBuilder(SSetBackSlotPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SSetBackSlotPacket::encode)
                .decoder(SSetBackSlotPacket::new)
                .consumerMainThread(SSetBackSlotPacket::handle)
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

        INSTANCE.messageBuilder(CApplyRecoilPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CApplyRecoilPacket::encode)
                .decoder(CApplyRecoilPacket::new)
                .consumerMainThread(CApplyRecoilPacket::handle)
                .add();

        INSTANCE.messageBuilder(CClusterBombExplosionParticlesPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CClusterBombExplosionParticlesPacket::encode)
                .decoder(CClusterBombExplosionParticlesPacket::new)
                .consumerMainThread(CClusterBombExplosionParticlesPacket::handle)
                .add();

        INSTANCE.messageBuilder(CSyncBackSlotPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CSyncBackSlotPacket::encode)
                .decoder(CSyncBackSlotPacket::new)
                .consumerMainThread(CSyncBackSlotPacket::handle)
                .add();

        INSTANCE.messageBuilder(CStopSoundPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CStopSoundPacket::encode)
                .decoder(CStopSoundPacket::new)
                .consumerMainThread(CStopSoundPacket::handle)
                .add();
        INSTANCE.messageBuilder(CHitMarkPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CHitMarkPacket::encode)
                .decoder(CHitMarkPacket::new)
                .consumerMainThread(CHitMarkPacket::handle)
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