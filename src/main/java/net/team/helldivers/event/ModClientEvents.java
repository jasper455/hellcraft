package net.team.helldivers.event;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.*;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.client.renderer.block.BotContactMineBlockRenderer;
import net.team.helldivers.client.renderer.block.ExtractionTerminalBlockRenderer;
import net.team.helldivers.client.renderer.block.HellbombBlockRenderer;
import net.team.helldivers.client.renderer.entity.EagleAirshipRenderer;
import net.team.helldivers.client.renderer.entity.HellbombHellpodRenderer;
import net.team.helldivers.client.renderer.entity.OrbitalLaserRenderer;
import net.team.helldivers.client.renderer.entity.SupportHellpodRenderer;
import net.team.helldivers.client.shader.post.tint.TintPostProcessor;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.item.custom.guns.IGunItem;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.GeckoLib;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    private static boolean SHOW_INVINCIBLE_HUD = false;
    private static float alpha = 0.0f;
    private static float outFadeSpeed;
    private static boolean hasFadedIn = false;

    @SubscribeEvent
    public static void onComputerFovModifierEvent(ComputeFovModifierEvent event) {
        if (Minecraft.getInstance().options.keySaveHotbarActivator.isDown()
                && !(Minecraft.getInstance().player.getMainHandItem().getItem() instanceof IGunItem)) {
            float fovModifier = 1f;
            float deltaTicks = ((float) 20) / 20f;
            deltaTicks *= deltaTicks;
            fovModifier *=1f - deltaTicks * 0.5f;
            event.setNewFovModifier(fovModifier);
        }

        if (Minecraft.getInstance().options.keyLoadHotbarActivator.isDown()
                && !(Minecraft.getInstance().player.getMainHandItem().getItem() instanceof IGunItem)) {
            float fovModifier = 1f;
            float deltaTicks = ((float) 30) / 20f;
            deltaTicks *= deltaTicks;
            fovModifier *=1f - deltaTicks * 0.5f;
            event.setNewFovModifier(fovModifier);
        }

        if (KeyBinding.AIM.isDown()
                && (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof IGunItem)) {
            float fovModifier = 1f;
            float deltaTicks = ((float) 25) / 20f;
            deltaTicks *= deltaTicks;
            fovModifier *=1f - deltaTicks * 0.5f;
            event.setNewFovModifier(fovModifier);
        }
    }

    public static void triggerFlashEffect(float fadeOutSpeed) {
        alpha = 0.001f;
        outFadeSpeed = fadeOutSpeed;
        hasFadedIn = false;
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (alpha <= 0f) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        // Optional: Bind a texture (like smoke), or use a plain color
        guiGraphics.fill(0, 0, screenWidth, screenHeight, new Color(255, 251, 216, (int)(alpha * 255)).getRGB());

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        if (alpha > 0.5f){
            hasFadedIn = true;
        }

        // Fade in the alpha
        if (!hasFadedIn && alpha < 0.5f) {
            alpha += 0.001f; // Tweak as needed
        }
        // Fade out the alpha
        if (hasFadedIn) {
            alpha -= outFadeSpeed; // Tweak as needed
        }
    }

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent event) {
        String message = event.getMessage().getString();
        Timer timer = new Timer();
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (message.contains("invincible")) {
            SHOW_INVINCIBLE_HUD = true;
            player.level().playSound(player, player.getX(), player.getY(), player.getZ(),
                    ModSounds.EASTER_EGG.get(), SoundSource.NEUTRAL, 1f, 1f);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    player.sendSystemMessage(Component.literal("This in because it's funny, Deal with it..."));
                }
            }, 2350);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SHOW_INVINCIBLE_HUD = false;
                }
            }, 4700);
        }
    }

    @SubscribeEvent
    public static void registerGuiOverlays(CustomizeGuiOverlayEvent event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        if (SHOW_INVINCIBLE_HUD) {
            guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                            HelldiversMod.MOD_ID, "textures/easter_egg/easter_egg.png"),
                    -20, -20, (int) (480 * 1.4), (int) (270 * 1.4), 0, 0, 1920, 1080,
                    1920, 1080);
        }
    }

    @Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ModClientBusEvents {


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PostProcessHandler.addInstance(TintPostProcessor.INSTANCE);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.SHOW_STRATAGEM_KEY);
            event.register(KeyBinding.UP_INPUT_KEY);
            event.register(KeyBinding.DOWN_INPUT_KEY);
            event.register(KeyBinding.LEFT_INPUT_KEY);
            event.register(KeyBinding.RIGHT_INPUT_KEY);
            event.register(KeyBinding.SHOOT);
            event.register(KeyBinding.RELOAD);
            event.register(KeyBinding.AIM);
            event.register(KeyBinding.EQUIP_BACKPACK);
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.EAGLE_AIRSHIP.get(), EagleAirshipRenderer::new);
            event.registerEntityRenderer(ModEntities.SUPPORT_HELLPOD.get(), SupportHellpodRenderer::new);
            event.registerEntityRenderer(ModEntities.ORBITAL_LASER.get(), OrbitalLaserRenderer::new);
            event.registerEntityRenderer(ModEntities.HELLBOMB_HELLPOD.get(), HellbombHellpodRenderer::new);

            event.registerBlockEntityRenderer(ModBlockEntities.HELLBOMB.get(), context -> new HellbombBlockRenderer());
            event.registerBlockEntityRenderer(ModBlockEntities.EXTRACTION_TERMINAL.get(), context -> new ExtractionTerminalBlockRenderer());
            event.registerBlockEntityRenderer(ModBlockEntities.BOT_CONTACT_MINE.get(), context -> new BotContactMineBlockRenderer());
        }

        @SubscribeEvent
        public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
            GeckoLib.initialize();
        }
    }

}