package net.team.helldivers.event;


import com.mojang.blaze3d.systems.RenderSystem;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.team.helldivers.client.model.entity.layers.HelldiverCapeModel;
import net.team.helldivers.client.renderer.block.HellbombBlockRenderer;
import net.team.helldivers.client.renderer.entity.layers.HelldiverCapeLayer;
import net.team.helldivers.client.shader.post.tint.TintPostProcessor;
import net.team.helldivers.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

import java.awt.*;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    private static float alpha = 0.0f;
    private static float outFadeSpeed;
    private static boolean hasFadedIn = false;

    @SubscribeEvent
    public static void onComputerFovModifierEvent(ComputeFovModifierEvent event) {
        if (Minecraft.getInstance().options.keySaveHotbarActivator.isDown()) {
            float fovModifier = 1f;
            float deltaTicks = ((float) 20) / 20f;
            deltaTicks *= deltaTicks;
            fovModifier *=1f - deltaTicks * 0.5f;
            event.setNewFovModifier(fovModifier);
        }

        if (Minecraft.getInstance().options.keyLoadHotbarActivator.isDown()) {
            float fovModifier = 1f;
            float deltaTicks = ((float) 30) / 20f;
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
        }

        @SubscribeEvent
        public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
            for (String skinType : event.getSkins()) {
                PlayerRenderer renderer = event.getPlayerSkin(skinType);
                HelldiverCapeModel capeModel = new HelldiverCapeModel(Minecraft.getInstance().getEntityModels()
                        .bakeLayer(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
                                "helldivers", "textures/entity/helldiver_cape.png"), "main")));

                renderer.addLayer(new HelldiverCapeLayer(renderer, capeModel));
            }
        }

        @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(new ModelLayerLocation(
                            ResourceLocation.fromNamespaceAndPath("helldivers", "textures/entity/helldiver_cape.png"), "main"),
                    HelldiverCapeModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            if (HelldiversMod.shouldRegisterExamples()) {
                event.registerBlockEntityRenderer(ModBlockEntities.HELLBOMB.get(), context -> new HellbombBlockRenderer());
            }
        }
    }

}