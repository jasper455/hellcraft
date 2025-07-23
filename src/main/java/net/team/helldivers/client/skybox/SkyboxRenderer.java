package net.team.helldivers.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;
import java.util.Comparator;
import java.util.Optional;

public class SkyboxRenderer {

    private static final Minecraft mc = Minecraft.getInstance();

    private final RenderType[] PHASE2_SKYBOXES;
    private final RenderType CREDITS_SKYBOX;
    private final RenderType STARS_FAR_1;
    public static final RenderType VORTEX;

    public SkyboxRenderer() {
        PHASE2_SKYBOXES = new RenderType[]{
                LodestoneRenderLayers.TRANSPARENT_TEXTURE_ACTUAL_TRIANGLE.apply(new ResourceLocation("helldivers", "textures/environment/skybox/stars.png")),
                LodestoneRenderLayers.ADDITIVE_TEXTURE_ACTUAL_TRIANGLE.apply(new ResourceLocation("helldivers", "textures/environment/skybox/9.png")),
                LodestoneRenderLayers.ADDITIVE_TEXTURE_ACTUAL_TRIANGLE.apply(new ResourceLocation("helldivers", "textures/environment/skybox/2.png")),
                LodestoneRenderLayers.ADDITIVE_TEXTURE_ACTUAL_TRIANGLE.apply(new ResourceLocation("helldivers", "textures/environment/skybox/4.png"))
        };

        CREDITS_SKYBOX = LodestoneRenderLayers.TRANSPARENT_TEXTURE_ACTUAL_TRIANGLE.apply(
                new ResourceLocation("helldivers", "textures/environment/skybox/credits.png"));
        STARS_FAR_1 = LodestoneRenderLayers.TRANSPARENT_TEXTURE_ACTUAL_TRIANGLE.apply(
                new ResourceLocation("helldivers", "textures/environment/skybox/stars_far.png"));
    }

    public void renderPhase1Fog(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();

        Player player = mc.player;
        if (player == null) return;

        Vec3 playerPos = player.position();

        renderFogLayer(poseStack, builder, playerPos.add(-1200, -35, 0), new Vec3(0, 5, 0), 20, true);
        renderFogLayer(poseStack, builder, playerPos.add(-1200, 285, 0), new Vec3(0, -5, 0), 20, true);
        renderFogLayer(poseStack, builder, playerPos.add(-1100, -50, -100), new Vec3(5, 0, 0), 20, false);
        renderFogLayer(poseStack, builder, playerPos.add(-200, -50, -100), new Vec3(10, 0, 0), 20, false);

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void renderFogLayer(PoseStack poseStack, WorldVFXBuilder builder, Vec3 start, Vec3 step, int count, boolean vertical) {
        poseStack.pushPose();
        poseStack.translate(start.x, start.y, start.z);

        for (int i = 0; i < count; i++) {
            poseStack.translate(step.x, step.y, step.z);
            ResourceLocation texture = new ResourceLocation("helldivers", "textures/environment/fog/fog" + (i % 10) + ".png");
            RenderType layer = LodestoneRenderLayers.TRANSPARENT_TEXTURE.applyAndCache(texture);
            builder.setColor(new Color(3598))
                    .setAlpha(0.18f)
                    .renderQuadFacing(
                            RenderHandler.DELAYED_RENDER.getBuffer(layer),
                            poseStack,
                            Vec3.ZERO,
                            vertical ? new Vec3(1500, 0, 0) : new Vec3(0, 500, 0),
                            vertical ? 1500 : 500,
                            vertical ? Vec3.YP : new Vec3(1, 0, 0)
                    );
        }

        poseStack.popPose();
    }

    public void renderPhase2Skybox(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();

        builder.setColor(Color.WHITE)
                .setAlpha(1.0f)
                .renderSphere(buffer.getBuffer(PHASE2_SKYBOXES[0]), poseStack, -1000.0f, 25, 25);

        for (int i = 1; i < PHASE2_SKYBOXES.length; i++) {
            poseStack.pushPose();
            float time = (mc.level.getGameTime() % 24000L + mc.getFrameTime()) / (1000f * i);
            poseStack.mulPose(Axis.YP.rotationDegrees(time));
            builder.setColor(Color.WHITE)
                    .setAlpha(0.7f)
                    .renderSphere(buffer.getBuffer(PHASE2_SKYBOXES[i]), poseStack, -1000.0f - i, 25, 25);
            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(0.0, 500.0, 0.0);
        builder.setColor(Color.WHITE)
                .setAlpha(1.0f)
                .renderSphere(buffer.getBuffer(LodestoneRenderLayers.END_PORTAL_ACTUAL_TRIANGLE), poseStack, -165.0f, 100, 10);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        for (int i = 0; i < 10; i++) {
            float rot = (-mc.level.getGameTime() % 24000L - mc.getFrameTime()) / 500.0f;
            poseStack.mulPose(Axis.YP.rotationDegrees(rot));
            builder.setColor(Color.WHITE)
                    .setAlpha(0.1f)
                    .renderSphere(buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation("helldivers", "textures/environment/eye_overlay.png"))),
                            poseStack, -1000.0f, 25, 25);
        }
        poseStack.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public void renderCreditsSkybox(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();

        builder.setColor(Color.WHITE).setAlpha(1.0f).renderSphere(buffer.getBuffer(PHASE2_SKYBOXES[0]), poseStack, -1000.0f, 25, 25);
        builder.setColor(Color.WHITE).setAlpha(0.7f).renderSphere(buffer.getBuffer(STARS_FAR_1), poseStack, -1001.0f, 25, 25);

        poseStack.pushPose();
        builder.setColor(Color.WHITE).setAlpha(0.7f).renderSphere(buffer.getBuffer(CREDITS_SKYBOX), poseStack, -1002.0f, 25, 25);
        poseStack.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    static {
        VORTEX = LodestoneRenderLayers.ADDITIVE_TEXTURE_ACTUAL_TRIANGLE.apply(
                new ResourceLocation("helldivers", "textures/vfx/vortex.png"));
    }
}
