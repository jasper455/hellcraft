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

public class HelldiverCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final HelldiverCapeModel capeModel;
    private final ResourceLocation textureLocation;

    public HelldiverCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer, ResourceLocation textureLocation) {
        super(pRenderer);
        this.textureLocation = textureLocation;
        this.capeModel = new HelldiverCapeModel(Minecraft.getInstance().getEntityModels()
                .bakeLayer(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/armor/b01_armor.png"
                ), "main")));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight,
                       AbstractClientPlayer pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
                       float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

//        if (!(pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem)) return;
        pLivingEntity.sendSystemMessage(Component.literal("test"));
        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.entitySolid(textureLocation));

        if (!pLivingEntity.isInvisible()) {
            ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
            if (!itemstack.is(Items.ELYTRA)) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.0F, 0.0F, 0.125F);
                double d0 = Mth.lerp((double)pPartialTick, pLivingEntity.xCloakO, pLivingEntity.xCloak) - Mth.lerp((double)pPartialTick, pLivingEntity.xo, pLivingEntity.getX());
                double d1 = Mth.lerp((double)pPartialTick, pLivingEntity.yCloakO, pLivingEntity.yCloak) - Mth.lerp((double)pPartialTick, pLivingEntity.yo, pLivingEntity.getY());
                double d2 = Mth.lerp((double)pPartialTick, pLivingEntity.zCloakO, pLivingEntity.zCloak) - Mth.lerp((double)pPartialTick, pLivingEntity.zo, pLivingEntity.getZ());
                float f = Mth.rotLerp(pPartialTick, pLivingEntity.yBodyRotO, pLivingEntity.yBodyRot);
                double d3 = (double)Mth.sin(f * ((float)Math.PI / 180F));
                double d4 = (double)(-Mth.cos(f * ((float)Math.PI / 180F)));
                float f1 = (float)d1 * 10.0F;
                f1 = Mth.clamp(f1, -6.0F, 32.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                f2 = Mth.clamp(f2, 0.0F, 150.0F);
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
                f3 = Mth.clamp(f3, -20.0F, 20.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                float f4 = Mth.lerp(pPartialTick, pLivingEntity.oBob, pLivingEntity.bob);
                f1 += Mth.sin(Mth.lerp(pPartialTick, pLivingEntity.walkDistO, pLivingEntity.walkDist) * 6.0F) * 32.0F * f4;
                if (pLivingEntity.isCrouching()) {
                    f1 += 25.0F;
                }

                pPoseStack.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
                capeModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                pPoseStack.popPose();
            }
        }
    }
}
