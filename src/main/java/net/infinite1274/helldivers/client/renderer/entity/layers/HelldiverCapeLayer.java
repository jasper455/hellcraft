package net.infinite1274.helldivers.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.infinite1274.helldivers.client.model.HelldiverCapeModel;
import net.infinite1274.helldivers.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class HelldiverCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final HelldiverCapeModel capeModel;

    public HelldiverCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer, HelldiverCapeModel capeModel) {
        super(pRenderer);
        this.capeModel = new HelldiverCapeModel(Minecraft.getInstance().getEntityModels()
                .bakeLayer(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
                        "helldivers", "textures/entity/helldiver_cape.png"), "main")));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight,
                       AbstractClientPlayer player, float pLimbSwing, float pLimbSwingAmount,
                       float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() != ModItems.HELLDIVER_CHESTPLATE.get() ||
                player.getItemBySlot(EquipmentSlot.HEAD).getItem() != ModItems.HELLDIVER_HELMET.get() ||
                player.getItemBySlot(EquipmentSlot.LEGS).getItem() != ModItems.HELLDIVER_LEGGINGS.get() ||
                player.getItemBySlot(EquipmentSlot.FEET).getItem() != ModItems.HELLDIVER_BOOTS.get()) return;

        VertexConsumer vertexConsumer = pBufferSource.getBuffer(this.capeModel.renderType(this.getTextureLocation(player)));

        pPoseStack.pushPose();

        if (player.isCrouching()) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(22.5f));
            pPoseStack.translate(0, 0.3, -0.1);
        }

        if (player.getDeltaMovement().x > 0.1 || player.getDeltaMovement().x< -0.1 ||
                player.getDeltaMovement().z > 0.1 || player.getDeltaMovement().z < -0.1 ||
                player.getDeltaMovement().y > 0.1 || player.getDeltaMovement().y < -0.1) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(player.moveDist / 10));
        }

        capeModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        pPoseStack.popPose();

        pPoseStack.pushPose();

        

        pPoseStack.popPose();
    }
}
