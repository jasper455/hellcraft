package net.team.helldivers.event;

import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.renderer.item.AR23Renderer;
import net.team.helldivers.entity.client.*;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.Ar23Item;
import net.team.helldivers.network.PacketHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;


@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MISSILE, MissileProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.STRATAGEM_ORB, StratagemOrbProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HELLPOD, HellpodProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BULLET, BulletProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }
}
