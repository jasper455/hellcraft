package net.team.helldivers.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.command.StopUseLodestoneCommand;
import net.team.helldivers.command.UseLodestoneCommand;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.blockentity.TheEndPortalRenderer.END_SKY_LOCATION;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
//    private static void renderEndSky(PoseStack pPoseStack) {
//        ResourceLocation SKY_LOCATION = ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/environment/sky_two.png");
////        RenderSystem.enableBlend();
//        RenderSystem.depthMask(false);
//        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
////        RenderSystem.setShaderTexture(0, SKY_LOCATION);
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder bufferbuilder = tesselator.getBuilder();
//
//        for(int i = 0; i < 6; ++i) {
//            pPoseStack.pushPose();
//            if (i == 1) {
//                pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
//            }
//
//            if (i == 2) {
//                pPoseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
//            }
//
//            if (i == 3) {
//                pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
//            }
//
//            if (i == 4) {
//                pPoseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
//            }
//
//            if (i == 5) {
//                pPoseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
//            }
//
//            Matrix4f matrix4f = pPoseStack.last().pose();
//            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -1.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
//            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, 1.0F).uv(0.0F, 1.0F).color(255, 255, 255, 255).endVertex();
//            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, 1.0F).uv(1.0F, 1.0F).color(255, 255, 255, 255).endVertex();
//            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -1.0F).uv(1.0F, 0.0F).color(255, 255, 255, 255).endVertex();
//            tesselator.end();
//            pPoseStack.popPose();
//        }
//
//        RenderSystem.depthMask(true);
////        RenderSystem.disableBlend();
//    }
//
//
//    @SubscribeEvent
//    public static void levelRenderEvent(RenderLevelStageEvent event) {
//        renderEndSky(event.getPoseStack());
//    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new StopUseLodestoneCommand(event.getDispatcher());
        new UseLodestoneCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getEntity().getPersistentData().putBoolean("helldivers.useLodestone",
                event.getOriginal().getPersistentData().getBoolean("helldivers.useLodestone"));
    }
}
