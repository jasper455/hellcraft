package net.jasper.lodestonemod.event;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jasper.lodestonemod.LodestoneMod;
import net.jasper.lodestonemod.client.rendering.ModRenderTypes;
import net.jasper.lodestonemod.client.rendering.SkyboxRenderer;
import net.jasper.lodestonemod.client.shader.post.tint.TintPostProcessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.joml.Matrix4f;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

import java.awt.*;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = LodestoneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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

    @Mod.EventBusSubscriber(modid = LodestoneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ModClientBusEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PostProcessHandler.addInstance(TintPostProcessor.INSTANCE);
        }


        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
            event.registerShader(new ShaderInstance(
                    event.getResourceProvider(),
                    ResourceLocation.fromNamespaceAndPath("lodestonemod", "rendertype_custom_sky"),
                    DefaultVertexFormat.POSITION
            ), shader -> {
                // You can store the shader here for later use if you want to use it manually
            });
        }

    }
}