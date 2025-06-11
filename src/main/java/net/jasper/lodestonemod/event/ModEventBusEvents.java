package net.jasper.lodestonemod.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jasper.lodestonemod.LodestoneMod;
import net.jasper.lodestonemod.client.rendering.ModRenderTypes;
import net.jasper.lodestonemod.entity.client.ExplosiveProjectileModel;
import net.jasper.lodestonemod.entity.client.ModModelLayers;
import net.jasper.lodestonemod.network.PacketHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.joml.Matrix4f;


@Mod.EventBusSubscriber(modid = LodestoneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.EXPLOSIVE, ExplosiveProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }

    @Mod.EventBusSubscriber(modid = LodestoneMod.MOD_ID, value = Dist.CLIENT)
    public class ModClientModEventBusEvents {

//        @SubscribeEvent
//        public static void onRenderWorld(RenderLevelStageEvent event) {
//            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
//
//            Level level = Minecraft.getInstance().level;
//            if (level == null || !isNight(level)) return;
//
//            PoseStack poseStack = event.getPoseStack();
//            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
//            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
//            Vec3 camPos = camera.getPosition();
//
//            poseStack.pushPose();
//            poseStack.translate(-camPos.x, -camPos.y, -camPos.z); // render at camera origin
//
//            VertexConsumer consumer = buffer.getBuffer(ModRenderTypes.CUSTOM_PORTAL);
//            Matrix4f matrix = poseStack.last().pose();
//
//            renderSkyCube(consumer, matrix); // see below
//
//            poseStack.popPose();
//        }
//
//        private static boolean isNight(Level level) {
//            float time = level.getTimeOfDay(1.0F); // 0.0 = midnight, 0.5 = noon
//            return time < 0.25F || time > 0.75F;
//        }
//
//        private static void renderSkyCube(VertexConsumer consumer, Matrix4f matrix) {
//            float size = 1000.0F;
//
//            // +Y
//            consumer.vertex(matrix, -size, size, -size).endVertex();
//            consumer.vertex(matrix, -size, size, size).endVertex();
//            consumer.vertex(matrix, size, size, size).endVertex();
//            consumer.vertex(matrix, size, size, -size).endVertex();
//
//            // -Y
//            consumer.vertex(matrix, -size, -size, -size).endVertex();
//            consumer.vertex(matrix, size, -size, -size).endVertex();
//            consumer.vertex(matrix, size, -size, size).endVertex();
//            consumer.vertex(matrix, -size, -size, size).endVertex();
//
//            // +Z
//            consumer.vertex(matrix, -size, -size, size).endVertex();
//            consumer.vertex(matrix, size, -size, size).endVertex();
//            consumer.vertex(matrix, size, size, size).endVertex();
//            consumer.vertex(matrix, -size, size, size).endVertex();
//
//            // -Z
//            consumer.vertex(matrix, -size, size, -size).endVertex();
//            consumer.vertex(matrix, size, size, -size).endVertex();
//            consumer.vertex(matrix, size, -size, -size).endVertex();
//            consumer.vertex(matrix, -size, -size, -size).endVertex();
//
//            // +X
//            consumer.vertex(matrix, size, -size, -size).endVertex();
//            consumer.vertex(matrix, size, -size, size).endVertex();
//            consumer.vertex(matrix, size, size, size).endVertex();
//            consumer.vertex(matrix, size, size, -size).endVertex();
//
//            // -X
//            consumer.vertex(matrix, -size, -size, -size).endVertex();
//            consumer.vertex(matrix, -size, size, -size).endVertex();
//            consumer.vertex(matrix, -size, size, size).endVertex();
//            consumer.vertex(matrix, -size, -size, size).endVertex();
//        }
    }

}
