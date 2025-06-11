package net.jasper.lodestonemod.event;


import com.mojang.blaze3d.vertex.PoseStack;
import net.jasper.lodestonemod.client.rendering.SkyboxRenderer;
import net.minecraft.client.Camera;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onChatEvent(ClientChatReceivedEvent event) {

    }

//    @SubscribeEvent
//    public static void setCanFly(PlayerEvent event) {
//        if (event.getEntity().isCreative()){
//            event.getEntity().getAbilities().mayfly = true;
//            event.getEntity().getAbilities().setFlyingSpeed(0.2f);
//            event.getEntity().onUpdateAbilities();
//        } else {
//            event.getEntity().getAbilities().mayfly = false;
//            event.getEntity().onUpdateAbilities();
//        }
//    }

//    @SubscribeEvent
//    public static void onRenderSkybox(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
//
//        Camera camera = event.getCamera();
//        PoseStack stack = new PoseStack();
//
////        // Step 1: Move to camera position (center the cube on the camera)
//        stack.translate(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
//
////        // Step 2: DO NOT apply rotation. We keep the cube facing one direction only.
//
////        // Step 3: Render the skybox cube
//        SkyboxRenderer.renderCubeSkybox(event.getPoseStack(), 200.0f);
//    }

}
