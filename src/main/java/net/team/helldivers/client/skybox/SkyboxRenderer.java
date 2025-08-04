package net.team.helldivers.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import org.joml.Matrix4f;

public class SkyboxRenderer {
    public static void renderSkybox(PoseStack pPoseStack, float size, int alpha, ResourceLocation[] skyboxTexture,
                                    Axis rotationAxis1, Axis rotationAxis2, float rotationSpeed) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Rotate the entire skybox cube
        float time = (float)(Minecraft.getInstance().level.getGameTime()) / rotationSpeed;
        pPoseStack.pushPose();
        pPoseStack.mulPose(rotationAxis1.rotationDegrees(time)); // <-- Y-axis rotation
        pPoseStack.mulPose(rotationAxis2.rotationDegrees(time)); // <-- Z-axis rotation

        for (int i = 0; i < 6; ++i) {
            pPoseStack.pushPose();

            switch (i) {
                case 0 -> pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                case 1 -> {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                }
                case 2 -> {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                }
                case 3 -> {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                }
                case 4 -> {
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                }
                case 5 -> pPoseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
            }

            RenderSystem.setShaderTexture(0, skyboxTexture[i]);

            Matrix4f matrix4f = pPoseStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(matrix4f, -size, -size, -size).uv(0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
            bufferbuilder.vertex(matrix4f, -size, -size, size).uv(0.0F, 1.0F).color(255, 255, 255, alpha).endVertex();
            bufferbuilder.vertex(matrix4f, size, -size, size).uv(1.0F, 1.0F).color(255, 255, 255, alpha).endVertex();
            bufferbuilder.vertex(matrix4f, size, -size, -size).uv(1.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
            tesselator.end();

            pPoseStack.popPose();
        }

        pPoseStack.popPose(); // Pop the cube rotation

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}