package net.team.helldivers;

import com.mojang.logging.LogUtils;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.entity.client.HellpodProjectileRenderer;
import net.team.helldivers.entity.client.MissileProjectileRenderer;
import net.team.helldivers.entity.client.StratagemOrbProjectileRenderer;
import net.team.helldivers.item.ModCreativeModeTabs;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.particle.ModParticles;
import net.team.helldivers.screen.ModMenuTypes;
import net.team.helldivers.screen.custom.StratagemPickerScreen;
import net.team.helldivers.sound.ModSounds;
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
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import static software.bernie.example.GeckoLibMod.DISABLE_EXAMPLES_PROPERTY_KEY;

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
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EntityRenderers.register(ModEntities.MISSILE_PROJECTILE.get(), MissileProjectileRenderer::new);
        EntityRenderers.register(ModEntities.STRATAGEM_ORB.get(), StratagemOrbProjectileRenderer::new);
        EntityRenderers.register(ModEntities.HELLPOD.get(), HellpodProjectileRenderer::new);
    }

    public static boolean shouldRegisterExamples() {
        return !FMLEnvironment.production && !Boolean.getBoolean(DISABLE_EXAMPLES_PROPERTY_KEY);
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
            MenuScreens.register(ModMenuTypes.STRATAGEM_PICKER.get(), StratagemPickerScreen::new);
        }

        @SubscribeEvent
        public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.SMOKE.get(), LodestoneWorldParticleType.Factory::new);
        }
    }
}
