package net.infinite1274.helldivers.event;

import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.entity.client.HellpodProjectileModel;
import net.infinite1274.helldivers.entity.client.MissileProjectileModel;
import net.infinite1274.helldivers.entity.client.ModModelLayers;
import net.infinite1274.helldivers.entity.client.StratagemOrbProjectileModel;
import net.infinite1274.helldivers.network.PacketHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MISSILE, MissileProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.STRATAGEM_ORB, StratagemOrbProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HELLPOD, HellpodProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }
}
