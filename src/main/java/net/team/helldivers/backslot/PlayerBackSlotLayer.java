package net.team.helldivers.backslot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.items.ItemStackHandler;

public class PlayerBackSlotLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerBackSlotLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight,
                       AbstractClientPlayer player, float pLimbSwing, float pLimbSwingAmount,
                       float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        pPoseStack.pushPose();

        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack backSlotItem = handler.getStackInSlot(0);
            if (!(backSlotItem.getItem() instanceof ShieldItem)) {
                pPoseStack.translate(0.1, 0, 0.1);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90f));
                pPoseStack.mulPose(Axis.XN.rotationDegrees(25f));
                if (player.isCrouching()) {
                    pPoseStack.mulPose(Axis.ZP.rotationDegrees(30f));
                    pPoseStack.mulPose(Axis.YN.rotationDegrees(15f));
                    pPoseStack.translate(0.1, 0.2, 0);
                }
            } else {
                pPoseStack.mulPose(Axis.YN.rotationDegrees(90));
                pPoseStack.mulPose(Axis.XN.rotationDegrees(45f));
                pPoseStack.translate(0.055, 0.5, 0f);
                pPoseStack.scale(0.75f, 1, 1);
                if (player.isCrouching()) {
                    pPoseStack.mulPose(Axis.ZN.rotationDegrees(30f));
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(30f));
                    pPoseStack.translate(0.1, 0.2, 0.1);
                }
            }

            if (player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != Items.ELYTRA.getDefaultInstance()) {
                if (!(backSlotItem.getItem() instanceof ShieldItem)) {
                    pPoseStack.translate(-0.1, 0, 0);
                } else {
                    pPoseStack.translate(-0.05, 0, 0);
                }
            }

            if (!backSlotItem.isEmpty()) {
                Minecraft.getInstance().getItemRenderer().renderStatic(backSlotItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, player.clientLevel, player.getId());
            }
        });

        pPoseStack.popPose();
    }
}
