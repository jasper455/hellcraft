package net.team.helldivers;

import com.mojang.logging.LogUtils;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.block.custom.samples.ModSampleBlocks;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.client.*;
import net.team.helldivers.gamerule.ModGameRules;
import net.team.helldivers.item.ModArmorItems;
import net.team.helldivers.item.ModCreativeModeTabs;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.particle.ModParticles;
import net.team.helldivers.screen.ModMenuTypes;
import net.team.helldivers.screen.custom.*;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.Headshots.HeadHitboxRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.team.helldivers.worldgen.chunk.ModChunkGenerators;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;
//TODO: hit indicators
// The value here should match an entry in the META-INF/mods.toml file
@Mod(HelldiversMod.MOD_ID)
public class HelldiversMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "helldivers";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public HelldiversMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        GeckoLib.initialize();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModArmorItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSampleBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        HeadHitboxRegistry.Register(); //pulls the bounding boxes that determine where a mob can be headshotted from json
        ModChunkGenerators.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(HeadHitboxRegistry.class);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        ModGameRules.DO_FLYING_BLOCKS.getId();
        ModGameRules.FLYING_BLOCKS_INTENSITY.getId();

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EntityRenderers.register(ModEntities.MISSILE_PROJECTILE.get(), MissileProjectileRenderer::new);
        EntityRenderers.register(ModEntities.EAGLE_500KG_BOMB.get(), Eagle500KgRenderer::new);
        EntityRenderers.register(ModEntities.STRATAGEM_ORB.get(), StratagemOrbProjectileRenderer::new);
        EntityRenderers.register(ModEntities.BULLET.get(), BulletProjectileRenderer::new);
        EntityRenderers.register(ModEntities.ROCKET.get(), RocketProjectileRenderer::new);
        EntityRenderers.register(ModEntities.FRAG_GRENADE.get(), FragGrenadeProjectileRenderer::new);
        EntityRenderers.register(ModEntities.FIRE_GRENADE.get(), FireGrenadeProjectileRenderer::new);
        EntityRenderers.register(ModEntities.HEATED_GAS.get(), HeatedGasProjectileRenderer::new);
        EntityRenderers.register(ModEntities.CLUSTER_BOMB.get(), ClusterBombProjectileRenderer::new);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.SUPPORT_HELLPOD_MENU.get(), SupportHellpodScreen::new);
            MenuScreens.register(ModMenuTypes.HELLBOMB_INPUT_MENU.get(), HellbombInputScreen::new);
            MenuScreens.register(ModMenuTypes.HELLBOMB_ENTITY_INPUT_MENU.get(), HellbombEntityInputScreen::new);
            MenuScreens.register(ModMenuTypes.EXTRACTION_TERMINAL.get(), ExtractionTerminalScreen::new);
            MenuScreens.register(ModMenuTypes.GALAXY_MAP_MENU.get(), GalaxyMapScreen::new);
        }

        @SubscribeEvent
        public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.SMOKE.get(), LodestoneWorldParticleType.Factory::new);
        }
        @SubscribeEvent
        public static void registerDamageTypes(RegisterEvent event) {
            event.register(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "orbital_laser"),
                    () -> new DamageType("orbital_laser", 0.1F));
        }

    }
}
