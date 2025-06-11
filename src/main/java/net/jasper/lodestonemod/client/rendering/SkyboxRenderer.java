package net.jasper.lodestonemod.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jasper.lodestone.systems.rendering.VFXBuilders;
import net.jasper.lodestonemod.LodestoneMod;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.*;

import static net.jasper.lodestonemod.client.rendering.ModRenderTypes.CUSTOM_PORTAL;
import static net.jasper.lodestonemod.client.rendering.ModRenderTypes.SKY_THREE;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LodestoneMod.MOD_ID)
public class SkyboxRenderer {

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
//
//        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
//        LevelRenderer levelRenderer = mc.levelRenderer;
//        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
//
//        poseStack.pushPose();
//
//        // Step 1: Move to the camera position
//        Vec3 camPos = camera.getPosition();
//        poseStack.translate(camPos.x, camPos.y, camPos.z);
//
//        // Step 2: Remove rotation
//        poseStack.last().pose().identity();
//        Quaternionf reverseRot = new Quaternionf(new Quaternionf(
//                camera.rotation().x / 5, camera.rotation().y / 5, camera.rotation().z / 5, camera.rotation().w)).invert();
//        poseStack.mulPose(reverseRot);
//
//        float size = 512; // large enough to surround the player completely
//
//        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld()
//                .setRenderType(CUSTOM_PORTAL)
//                .setAlpha(1f)
//                .setColor(Color.WHITE)
//                .setUV(0.0f, 0.0f, 1.0f, 1.0f);
//
//        // Step 3: Render cube using WorldVFXBuilder
//        renderFace(builder, poseStack, size, Direction.UP);
//        renderFace(builder, poseStack, size, Direction.DOWN);
//        renderFace(builder, poseStack, size, Direction.NORTH);
//        renderFace(builder, poseStack, size, Direction.SOUTH);
//        renderFace(builder, poseStack, size, Direction.EAST);
//        renderFace(builder, poseStack, size, Direction.WEST);
//
//        poseStack.popPose();

        poseStack.pushPose();
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                Minecraft.getInstance().renderBuffers().bufferSource(), ModRenderTypes.CUSTOM_PORTAL, false, false);
//        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 255, 255, 255, 255);
        poseStack.popPose();

        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(
                LodestoneRenderTypeRegistry.ADDITIVE_TWO_SIDED_TEXTURE_TRIANGLE.apply(RenderTypeToken.createToken(SKY_THREE)));
        builder
                .setColor(1.0f, 1.0f, 1.0f)
                .setAlpha(1f)
                .setUV(0f, 0f, 1f, 1f) // important!
                .setLight(0xF000F0)
//                .renderCylinder(vertexconsumer, pPoseStack, -pEntity.tickCount / 20f, 50, 1)
                .renderSphere(vertexconsumer, poseStack, -512, 50, 50);

        Minecraft mc = Minecraft.getInstance();
    }
    private static void renderFace(VFXBuilders.WorldVFXBuilder builder, PoseStack stack, float size, Direction direction) {
        float half = size / 2f;

        // Base quad facing NORTH
        Vector3f[] face = new Vector3f[] {
                new Vector3f(-half, -half, -half),
                new Vector3f(half, -half, -half),
                new Vector3f(half, half, -half),
                new Vector3f(-half, half, -half)
        };

        // Rotate face to target direction
        for (Vector3f v : face) {
            rotateToDirection(v, direction);
        }

        builder.renderQuad(stack, face, 1f, 1f);
    }

    // Utility: rotate a point to match a direction
    private static void rotateToDirection(Vector3f vec, Direction dir) {
        switch (dir) {
            case UP -> vec.set(vec.x(), vec.z(), -vec.y());
            case DOWN -> vec.set(vec.x(), -vec.z(), vec.y());
            case SOUTH -> vec.set(-vec.x(), vec.y(), -vec.z());
            case EAST -> vec.set(-vec.z(), vec.y(), vec.x());
            case WEST -> vec.set(vec.z(), vec.y(), -vec.x());
            // NORTH is default
        }
    }

//    public static void renderCubeSkybox(PoseStack stack, float size) {
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.depthMask(false);
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//
//        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(ModRenderTypes.CUSTOM_PORTAL)
//                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
//                .setUV(0.0f, 0.0f, 1.0f, 1.0f);
//
//        float half = size / 2;
//
//        // For skybox atlases, divide UVs accordingly (1/4 wide, 1/3 tall)
//        // Assume layout like:
//        //       [TOP]
//        // [LEFT][FRONT][RIGHT][BACK]
//        //      [BOTTOM]
//
//        // --- Top Face
////        builder.setUV(1f / 4f, 0f / 3f, 2f / 4f, 1f / 3f);
//        builder.renderQuad(stack, new Vector3f[] {
//                new Vector3f(-half, half, -half),
//                new Vector3f(half,  half, -half),
//                new Vector3f(half,  half, half),
//                new Vector3f(-half, half, half)
//        }, 1.0f);
//
//        // --- Bottom Face
////        builder.setUV(1f / 4f, 2f / 3f, 2f / 4f, 3f / 3f);
//        builder.renderQuad(stack, new Vector3f[] {
//                new Vector3f(-half, -half, half),
//                new Vector3f(half,  -half, half),
//                new Vector3f(half,  -half, -half),
//                new Vector3f(-half, -half, -half)
//        }, 1.0f);
//
//        // --- Front (-Z)
////        builder.setUV(1f / 4f, 1f / 3f, 2f / 4f, 2f / 3f);
//        builder.renderQuad(stack, new Vector3f[] {
//                new Vector3f(-half, -half, -half),
//                new Vector3f(half,  -half, -half),
//                new Vector3f(half,   half, -half),
//                new Vector3f(-half,  half, -half)
//        }, 1.0f);
//
//        // --- Back (+Z)
////        builder.setUV(3f / 4f, 1f / 3f, 4f / 4f, 2f / 3f);
//        builder.renderQuad(stack, new Vector3f[] {
//                new Vector3f(half,  -half, half),
//                new Vector3f(-half, -half, half),
//                new Vector3f(-half,  half, half),
//                new Vector3f(half,   half, half)
//        }, 1.0f);
//
//        // --- Left (-X)
////        builder.setUV(0f / 4f, 1f / 3f, 1f / 4f, 2f / 3f);
//        builder.renderQuad(stack, new Vector3f[] {
//                new Vector3f(-half, -half, half),
//                new Vector3f(-half, -half, -half),
//                new Vector3f(-half,  half, -half),
//                new Vector3f(-half,  half, half)
//        }, 1.0f);
//
//        // --- Right (+X)
////        builder.setUV(2f / 4f, 1f / 3f, 3f / 4f, 2f / 3f);
//        builder.renderQuad(stack, new Vector3f[] {
//                new Vector3f(half, -half, -half),
//                new Vector3f(half, -half,  half),
//                new Vector3f(half,  half,  half),
//                new Vector3f(half,  half, -half)
//        }, 1.0f);
//    }
}
