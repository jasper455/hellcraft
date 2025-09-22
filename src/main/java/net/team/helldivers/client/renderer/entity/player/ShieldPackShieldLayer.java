package net.team.helldivers.client.renderer.entity.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.client.model.entity.player.ShieldPackShieldModel;
import net.team.helldivers.client.renderer.ModRenderTypes;
import net.team.helldivers.item.ModItems;
import net.team.lodestone.systems.rendering.VFXBuilders;

public class ShieldPackShieldLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final ShieldPackShieldModel shieldModel;

    public ShieldPackShieldLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
        this.shieldModel = new ShieldPackShieldModel(Minecraft.getInstance().getEntityModels()
                .bakeLayer(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/entity/shield_pack_shield.png"), "main")));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer pLivingEntity,
                       float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pLivingEntity.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            ItemStackHandler handler = backSlot.getInventory();
            ItemStack backSlotItem = handler.getStackInSlot(0);
            if (!pLivingEntity.isInvisible()) {
                if (backSlotItem.is(ModItems.SHIELD_PACK.get()) && backSlotItem.getDamageValue() <= backSlotItem.getMaxDamage() - 1 &&
                !pLivingEntity.getCooldowns().isOnCooldown(backSlotItem.getItem())) {
                    pPoseStack.pushPose();
                    VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(
                            HelldiversMod.MOD_ID, "textures/entity/shield_pack_shield.png")));
//                    shieldModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                    pPoseStack.translate(0, 0.5, 0);
                    VFXBuilders.WorldVFXBuilder builder = VFXBuilders.WorldVFXBuilder.createWorld();
                    builder.setColor(1.0f, 1.0f, 1.0f)
                            .setAlpha(1f)
                            .setUV(0f, 0f, 1f, 1f) // important!
                            .setLight(0xF000F0)
                            .replaceBufferSource(pBuffer)
                            .setRenderType(RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(
                                    HelldiversMod.MOD_ID, "textures/entity/shield_pack_shield.png")))
                            .renderSphere(vertexConsumer, pPoseStack, 1.5f, 50, 50);
                    pPoseStack.popPose();
                }
            }
        });
    }
}
