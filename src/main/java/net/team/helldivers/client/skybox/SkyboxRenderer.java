package net.team.helldivers.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.HelldiversMod;
import org.joml.Matrix4f;

public class SkyboxRenderer {
    public static void renderSkybox(PoseStack pPoseStack) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        ResourceLocation[] skyboxFaces = new ResourceLocation[] {
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/milky_way/skybox_front.png"),   // North
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/milky_way/skybox_right.png"),   // East
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/milky_way/skybox_back.png"),    // South
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/milky_way/skybox_left.png"),    // West
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/milky_way/skybox_top.png"),     // Up
                ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/milky_way/skybox_bottom.png")   // Down
        };

        for (int i = 0; i < 6; ++i) {
            pPoseStack.pushPose();

            switch (i) {
                case 0 -> pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // North
                case 1 -> {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // East
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(90.0F)); // East
                }
                case 2 -> {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // South
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F)); // South
                }
                case 3 -> {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // West
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F)); // West
                }
                case 4 -> {
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F)); // Up
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F)); // Up
                    }
                case 5 -> pPoseStack.mulPose(Axis.XP.rotationDegrees(0.0F));  // Down
            }

            RenderSystem.setShaderTexture(0, skyboxFaces[i]);

            Matrix4f matrix4f = pPoseStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.0F, 1.0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(1.0F, 1.0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(1.0F, 0.0F).color(255, 255, 255, 255).endVertex();
            tesselator.end();

            pPoseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}