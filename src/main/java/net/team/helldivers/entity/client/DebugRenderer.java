package net.team.helldivers.entity.client;

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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.team.helldivers.util.ShootHelper;
import net.team.helldivers.util.Headshots.HeadHitbox;
import net.team.helldivers.util.Headshots.HeadHitboxRegistry;
//this will render a red outline over the area where an entity can be headshotted 
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DebugRenderer {
    private static boolean shouldRender = false;
    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if(shouldRender){
                if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;

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

                AABB box = hitbox.getBox(entity.getBoundingBox()).move(-camPos.x, -camPos.y, -camPos.z);
                AABB rotated = ShootHelper.rotateHeadBox(entity, box);

                LevelRenderer.renderLineBox(
                    poseStack,
                    lines,
                    rotated,
                    1.0F, 0.0F, 0.0F, // red
                    1.0F              // full alpha
                );
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
