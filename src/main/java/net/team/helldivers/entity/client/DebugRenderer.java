package net.team.helldivers.entity.client;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.team.helldivers.util.OBB;
import net.team.helldivers.util.ShootHelper;
import net.team.helldivers.util.Headshots.HeadHitbox;
import net.team.helldivers.util.Headshots.HeadHitboxRegistry;
//this will render a red outline over the area where an entity can be headshotted 
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DebugRenderer {
    private static boolean shouldRender = true;
    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if(shouldRender){
                if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;
            if (!player.level().isClientSide()) return;

            PoseStack poseStack = event.getPoseStack();
            Camera camera = mc.gameRenderer.getMainCamera();
            Vec3 camPos = camera.getPosition();

            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            VertexConsumer lines = bufferSource.getBuffer(RenderType.lines());

            ClientLevel level = mc.level;
            if (level == null) return;

            for (Entity entity : level.entitiesForRendering()) {
                ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                if (id == null) continue;

                HeadHitbox hitbox = HeadHitboxRegistry.get(id.toString());
                if (hitbox == null) continue;
                AABB box = hitbox.getBox(entity.getBoundingBox());
                if(!hitbox.isVert()){
                    OBB rotated = ShootHelper.rotateHeadOBB(entity, box);
                    Vec3[] corners = rotated.getCorners();
                    for (Vec3 corner : corners) {
                        double size = 0.01;
                        AABB dotBox = new AABB(
                            corner.x - size, corner.y - size, corner.z - size,
                            corner.x + size, corner.y + size, corner.z + size
                        );

                        LevelRenderer.renderLineBox(
                            poseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.lines()),
                            dotBox.minX - camPos.x, dotBox.minY - camPos.y, dotBox.minZ - camPos.z,
                            dotBox.maxX - camPos.x, dotBox.maxY - camPos.y, dotBox.maxZ - camPos.z,
                            1f, 0f, 0f, 1f
                        );
                    }
                }
                else{
                    AABB rotated = ShootHelper.rotateAABB(box, entity);
                    AABB shifted = rotated.move(-camPos.x, -camPos.y, -camPos.z);
                    LevelRenderer.renderLineBox(poseStack, lines, shifted, 1f, 0f, 0f, 1f);
                }

            }

            bufferSource.endBatch(RenderType.lines());
        }
    }
    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent event) {
        String message = event.getMessage().getString();
        if (message.contains("show headshot debug overlay")) {//put this in chat to show the overlay
            shouldRender = true;
        }
        if (message.contains("hide headshot debug overlay")) {//put this in chat to hide it
            shouldRender = false;
        }
    }
}
