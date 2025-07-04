package net.team.helldivers.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.renderer.item.AR23Renderer;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.client.*;
import net.team.helldivers.entity.custom.EagleAirshipEntity;
import net.team.helldivers.entity.custom.HeatedGasProjectileEntity;
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
        event.registerLayerDefinition(ModModelLayers.MISSILE, Eagle500KgModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.STRATAGEM_ORB, StratagemOrbProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HELLPOD, HellpodProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BULLET, BulletProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BULLET, HeatedGasProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ROCKET, RocketProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FRAG_GRENADE, FragGrenadeProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.EAGLE_AIRSHIP.get(), EagleAirshipEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }
}
