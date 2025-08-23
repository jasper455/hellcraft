package net.team.helldivers.client.renderer.entity.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.items.ItemStackHandler;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.client.model.entity.player.HelldiverCapeModel;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.armor.IHelldiverArmorItem;
import net.team.helldivers.item.custom.backpacks.AbstractBackpackItem;

public class HelldiverCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final HelldiverCapeModel capeModel;

    public HelldiverCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
        this.capeModel = new HelldiverCapeModel(Minecraft.getInstance().getEntityModels()
                .bakeLayer(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/entity/helldiver_cape.png"), "main")));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer pLivingEntity,
                       float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pLivingEntity.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack backSlotItem = handler.getStackInSlot(0);

            if (pLivingEntity.isCapeLoaded() && pLivingEntity.getCloakTextureLocation() != null
                    && pLivingEntity.isModelPartShown(PlayerModelPart.CAPE)) return;

            if (!pLivingEntity.isInvisible()) {
                ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
                if (!(itemstack.getItem() instanceof IHelldiverArmorItem)) return;
                pPoseStack.pushPose();
                pPoseStack.translate(0.0F, 0.125F, 0F);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                double d0 = Mth.lerp((double) pPartialTicks, pLivingEntity.xCloakO, pLivingEntity.xCloak) - Mth.lerp((double) pPartialTicks, pLivingEntity.xo, pLivingEntity.getX());
                double d1 = Mth.lerp((double) pPartialTicks, pLivingEntity.yCloakO, pLivingEntity.yCloak) - Mth.lerp((double) pPartialTicks, pLivingEntity.yo, pLivingEntity.getY());
                double d2 = Mth.lerp((double) pPartialTicks, pLivingEntity.zCloakO, pLivingEntity.zCloak) - Mth.lerp((double) pPartialTicks, pLivingEntity.zo, pLivingEntity.getZ());
                float f = Mth.rotLerp(pPartialTicks, pLivingEntity.yBodyRotO, pLivingEntity.yBodyRot);
                double d3 = (double) Mth.sin(f * ((float) Math.PI / 180F));
                double d4 = (double) (-Mth.cos(f * ((float) Math.PI / 180F)));
                float f1 = (float) d1 * 2.0F; // instead of *10.0F
                f1 = Mth.clamp(f1, -2.0F, 4.0F);
                float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                f2 = Mth.clamp(f2, 0.0F, backSlotItem.isEmpty() ? 75f : 10f);
                float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
                f3 = Mth.clamp(f3, -20.0F, 20.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }
                float f4 = Mth.lerp(pPartialTicks, pLivingEntity.oBob, pLivingEntity.bob);
                f1 += Mth.sin(Mth.lerp(pPartialTicks, pLivingEntity.walkDistO, pLivingEntity.walkDist) * 6.0F) * 32.0F * f4;
                if (pLivingEntity.isCrouching()) {
                    f1 -= 25.0F;
                    pPoseStack.translate(0.0F, 0.15F, 0F);
                }
                pPoseStack.mulPose(Axis.XP.rotationDegrees((backSlotItem.isEmpty() ? -6.0F : 0.0f) - f2 / 2.0F + f1));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
                if (backSlotItem.getItem() instanceof AbstractBackpackItem) {
                    pPoseStack.mulPose(Axis.XP.rotationDegrees((-20)));
                }
                VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entitySolid(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/entity/helldiver_cape.png")));
                capeModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                pPoseStack.popPose();
            }
        });
    }
}
