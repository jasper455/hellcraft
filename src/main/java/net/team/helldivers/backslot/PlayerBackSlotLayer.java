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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.items.ItemStackHandler;
import net.team.helldivers.item.custom.armor.IHelldiverArmorItem;
import net.team.helldivers.item.custom.backpacks.AbstractBackpackItem;

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

            if (!(backSlotItem.getItem() instanceof AbstractBackpackItem)) {
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                pPoseStack.translate(0.0F, 0F, 0.05F);
                double d0 = Mth.lerp((double)pPartialTick, player.xCloakO, player.xCloak) - Mth.lerp((double)pPartialTick, player.xo, player.getX());
                double d1 = Mth.lerp((double)pPartialTick, player.yCloakO, player.yCloak) - Mth.lerp((double)pPartialTick, player.yo, player.getY());
                double d2 = Mth.lerp((double)pPartialTick, player.zCloakO, player.zCloak) - Mth.lerp((double)pPartialTick, player.zo, player.getZ());
                float f = Mth.rotLerp(pPartialTick, player.yBodyRotO, player.yBodyRot);
                double d3 = (double)Mth.sin(f * ((float)Math.PI / 180F));
                double d4 = (double)(-Mth.cos(f * ((float)Math.PI / 180F)));
                float f1 = (float)d1 * 2.0F; // instead of *10.0F
                f1 = Mth.clamp(f1, -2.0F, 4.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                ItemStack itemstack = player.getItemBySlot(EquipmentSlot.CHEST);
                f2 = Mth.clamp(f2, itemstack.getItem() instanceof IHelldiverArmorItem ? 40F : 0, 50.0F);
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
                f3 = Mth.clamp(f3, -20.0F, 20.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }
                float f4 = Mth.lerp(pPartialTick, player.oBob * 2, player.bob * 2);
                f1 += Mth.sin(Mth.lerp(pPartialTick, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * f4;
                if (player.isCrouching()) {
//                f1 -= 25.0F;
                    pPoseStack.translate(0.0F, 0.15F, 0F);
                }
                pPoseStack.mulPose(Axis.XP.rotationDegrees(-6.0F - f2 / 2.0F + f1));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
            }


            if (!(backSlotItem.getItem() instanceof AbstractBackpackItem)) {
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
            } else {
                pPoseStack.scale(3, 3, 3);
                pPoseStack.mulPose(Axis.XP.rotationDegrees(180));
                pPoseStack.translate(0.1, -0.1, -0.025);
                if (player.isCrouching()) {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees(30f));
                    pPoseStack.translate(0, -0.05, 0);
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
