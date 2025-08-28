package net.team.helldivers.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.team.helldivers.HelldiversMod;

import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class HitMarkRender {

    // Set this to true when you want to render the hit marker
    public static boolean showHitMarker = false;

    // Controls how long the hit marker is visible
    private static long showUntil = 0;

    public static void triggerHitMarker(long durationMillis) {
        showHitMarker = true;
        showUntil = System.currentTimeMillis() + durationMillis;
    }
    private static final ResourceLocation HITMARKER_TEXTURE =
    new ResourceLocation(HelldiversMod.MOD_ID, "textures/gui/hit.png");

@SubscribeEvent
public static void onRenderCrosshair(RenderGuiOverlayEvent event) {
    if (!showHitMarker || System.currentTimeMillis() > showUntil) {
        showHitMarker = false;
        return;
    }

    GuiGraphics guiGraphics = event.getGuiGraphics();
    PoseStack poseStack = guiGraphics.pose();
    Minecraft mc = Minecraft.getInstance();

    int screenWidth = mc.getWindow().getGuiScaledWidth();
    int screenHeight = mc.getWindow().getGuiScaledHeight();
    int centerX = screenWidth / 2;
    int centerY = screenHeight / 2;

    poseStack.pushPose();

    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    int size = 16; // Size of the texture in pixels

    guiGraphics.blit(
        HITMARKER_TEXTURE,
        centerX - size / 2,
        centerY - size / 2,
        0, 0,
        size, size,
        size, size
    );

    poseStack.popPose();
    }
}
