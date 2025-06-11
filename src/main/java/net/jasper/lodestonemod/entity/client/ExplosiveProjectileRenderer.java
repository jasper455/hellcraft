package net.jasper.lodestonemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jasper.lodestonemod.LodestoneMod;
import net.jasper.lodestonemod.client.rendering.ModRenderTypes;
import net.jasper.lodestonemod.client.rendering.SkyboxRenderer;
import net.jasper.lodestonemod.entity.custom.ExplosiveProjectileEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import net.jasper.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

public class ExplosiveProjectileRenderer extends EntityRenderer<ExplosiveProjectileEntity> {
    private ExplosiveProjectileModel model;
    public ExplosiveProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new ExplosiveProjectileModel(pContext.bakeLayer(ModModelLayers.EXPLOSIVE));
    }

    @Override
    public ResourceLocation getTextureLocation(ExplosiveProjectileEntity ratEntity) {
        return ResourceLocation.fromNamespaceAndPath(LodestoneMod.MOD_ID, "textures/entity/rat/rat.png");
    }

    @Override
    public void render(ExplosiveProjectileEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                pBuffer, ModRenderTypes.CUSTOM_PORTAL, false, false);
//        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 255, 255, 255, 255);
        pPoseStack.popPose();

        BlockPos pos = new BlockPos(pEntity.getBlockX(), pEntity.getBlockY() + 10, pEntity.getBlockZ());

        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(
                LodestoneRenderTypeRegistry.ADDITIVE_SOLID);
        builder
                .setColor(1.0f, 1.0f, 1.0f)
                .setAlpha(1f)
                .setUV(0f, 0f, 1f, 1f) // important!
                .setLight(0xF000F0)
//                .renderCylinder(vertexconsumer, pPoseStack, -pEntity.tickCount / 20f, 50, 1)
                .renderSphere(vertexconsumer, pPoseStack, -512, 50, 50);

        Minecraft mc = Minecraft.getInstance();

// Center on camera
        pPoseStack.pushPose();
//        pPoseStack.translate(mc.cameraEntity.getX() + 10, mc.cameraEntity.getY(), mc.cameraEntity.getZ());
// Optionally scale or rotate
        pPoseStack.last().pose().identity();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf reverseRot = new Quaternionf(camera.rotation()).invert();
        pPoseStack.mulPose(reverseRot);

        // REMOVE camera rotation so the cube stays still
//        SkyboxRenderer.renderCubeSkybox(pPoseStack, 512); // 512 blocks cube size

        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public boolean shouldRender(ExplosiveProjectileEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}