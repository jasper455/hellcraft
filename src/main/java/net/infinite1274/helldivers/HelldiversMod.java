package net.infinite1274.helldivers;

import com.mojang.logging.LogUtils;
import net.infinite1274.helldivers.entity.ModEntities;
import net.infinite1274.helldivers.entity.client.MissileProjectileRenderer;
import net.infinite1274.helldivers.item.ModCreativeModeTabs;
import net.infinite1274.helldivers.item.ModItems;
import net.infinite1274.helldivers.particle.ModParticles;
import net.infinite1274.helldivers.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
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
import org.slf4j.Logger;
import team.lodestar.lodestone.systems.particle.screen.LodestoneScreenParticleType;

import static team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HelldiversMod.MOD_ID)
public class HelldiversMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "helldivers";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public HelldiversMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EntityRenderers.register(ModEntities.MISSILE_PROJECTILE.get(), MissileProjectileRenderer::new);
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
        public static void onClientSetup(FMLClientSetupEvent event) {}

        @SubscribeEvent
        public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
            registerProvider(WISP, new LodestoneScreenParticleType.Factory(getSpriteSet(
                    ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "smoke_particle"))));
        }
    }
}
