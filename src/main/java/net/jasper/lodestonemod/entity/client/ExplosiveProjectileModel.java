package net.jasper.lodestonemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jasper.lodestonemod.entity.custom.ExplosiveProjectileEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ExplosiveProjectileModel extends HierarchicalModel<ExplosiveProjectileEntity> {
    private final ModelPart MainBody;
    private final ModelPart head;

    public ExplosiveProjectileModel(ModelPart root) {
        this.MainBody = root.getChild("MainBody");
        this.head = this.MainBody.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition head = MainBody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -1.1252F, 0.0629F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 15).addBox(-1.0F, -0.6252F, 2.5629F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 16).addBox(-2.5F, -2.1252F, 1.0629F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(20, 17).addBox(0.5F, -2.1252F, 1.0629F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 3.0F));

        PartDefinition legs = MainBody.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, -1.0F));

        PartDefinition leg1 = legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(12, 11).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.0F, 2.5F));

        PartDefinition leg2 = legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(12, 15).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, 2.5F));

        PartDefinition leg3 = legs.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.0F, -3.5F));

        PartDefinition leg4 = legs.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(8, 19).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, -3.5F));

        PartDefinition body = MainBody.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.5F, -4.0F, 5.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -1.0F));

        PartDefinition tail = MainBody.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -6.0F));

        PartDefinition segment1 = tail.addOrReplaceChild("segment1", CubeListBuilder.create().texOffs(16, 19).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 5.0F));

        PartDefinition segment2 = tail.addOrReplaceChild("segment2", CubeListBuilder.create().texOffs(0, 20).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -1.0F));

        PartDefinition segment3 = tail.addOrReplaceChild("segment3", CubeListBuilder.create().texOffs(20, 11).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -4.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public void setupAnim(ExplosiveProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        MainBody.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    @Override
    public ModelPart root() {
        return MainBody;
    }
}
