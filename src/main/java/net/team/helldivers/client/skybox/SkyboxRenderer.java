package net.team.helldivers.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.HelldiversMod;
import net.team.lodestone.systems.rendering.VFXBuilders;
import org.joml.Matrix4f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.*;

public class SkyboxRenderer {

    private static final Minecraft mc = Minecraft.getInstance();

    private final RenderType[] SKYBOX_LAYERS;
    private final RenderType FOG_LAYER;

    public SkyboxRenderer() {
        SKYBOX_LAYERS = new RenderType[]{
                LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE_TRIANGLE.apply(RenderTypeToken.createToken(
                        ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/skybox/stars.png"))),
                LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeToken.createToken(
                        ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/skybox/stars_far.png")))
        };
        FOG_LAYER = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE_TRIANGLE.apply(RenderTypeToken.createToken(
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/fog/fog0.png"))); // Fallback default
    }

    public void render(PoseStack poseStack) {
        renderSkybox(poseStack);
        renderFog(poseStack);
    }

    private void renderSkybox(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.WorldVFXBuilder.createWorld().setPosColorTexLightmapDefaultFormat();

        for (int i = 0; i < SKYBOX_LAYERS.length; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(((mc.level.getGameTime() % 24000L) + mc.getFrameTime()) / (500.0F * (i + 1))));
            builder.setColor(Color.WHITE)
                    .setAlpha(0.75f)
                    .renderSphere(buffer.getBuffer(SKYBOX_LAYERS[i]), poseStack, -1000.0f - i, 32, 32);
            poseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    private void renderFog(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.WorldVFXBuilder.createWorld().setPosColorTexLightmapDefaultFormat();

        Player player = mc.player;
        if (player == null) return;

        Vec3 playerPos = player.position();

        poseStack.pushPose();
        poseStack.translate(playerPos.x - 1200, playerPos.y - 35, playerPos.z);

        for (int i = 0; i < 20; i++) {
            poseStack.translate(0, 5, 0);

            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/fog/fog" + (i % 10) + ".png");
            RenderType dynamicFog = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE_TRIANGLE.applyAndCache(RenderTypeToken.createToken(texture));

            builder.setColor(new Color(3598)) // Soft bluish fog color
                    .setAlpha(0.18f)
                    .renderQuadFacing(RenderHandler.DELAYED_RENDER.getTarget().getBuffer(dynamicFog),
                            poseStack,
                            Vec3.ZERO,
                            new Vec3(1500, 0, 0),
                            1500,
                            new Vec3(0, 1, 0));
        }

        poseStack.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}