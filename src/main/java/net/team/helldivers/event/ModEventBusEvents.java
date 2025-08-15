package net.team.helldivers.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.ModBotEntities;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.client.*;
import net.team.helldivers.entity.custom.EagleAirshipEntity;
import net.team.helldivers.entity.custom.GatlingSentryHellpodEntity;
import net.team.helldivers.entity.custom.OrbitalLaserEntity;
import net.team.helldivers.entity.custom.bots.AutomatonTrooperEntity;
import net.team.helldivers.entity.custom.bots.BerserkerEntity;
import net.team.helldivers.entity.custom.bots.RangedHulkEntity;
import net.team.helldivers.network.PacketHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MISSILE, MissileProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MISSILE, Eagle500KgModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.STRATAGEM_ORB, StratagemOrbProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BULLET, BulletProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BULLET, HeatedGasProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ROCKET, RocketProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FRAG_GRENADE, FragGrenadeProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FIRE_GRENADE, FireGrenadeProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CLUSTER_BOMB, ClusterBombProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.EAGLE_AIRSHIP.get(), EagleAirshipEntity.createAttributes().build());
        event.put(ModEntities.ORBITAL_LASER.get(), OrbitalLaserEntity.createAttributes().build());
        event.put(ModEntities.GATLING_SENTRY.get(), GatlingSentryHellpodEntity.createAttributes().build());

        event.put(ModBotEntities.HULK.get(), RangedHulkEntity.createAttributes().build());
        event.put(ModBotEntities.BERSERKER.get(), BerserkerEntity.createAttributes().build());
        event.put(ModBotEntities.AUTOMATON_TROOPER.get(), AutomatonTrooperEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }
}
