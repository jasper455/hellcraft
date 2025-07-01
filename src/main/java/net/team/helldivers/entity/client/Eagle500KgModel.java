package net.team.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.team.helldivers.entity.custom.Eagle500KgEntity;
import net.team.helldivers.entity.custom.MissileProjectileEntity;

public class Eagle500KgModel extends HierarchicalModel<Eagle500KgEntity> {
    private final ModelPart MainBody;
    private final ModelPart fins_lower;
    private final ModelPart fins_upper;

    public Eagle500KgModel(ModelPart root) {
        this.MainBody = root.getChild("MainBody");
        this.fins_lower = this.MainBody.getChild("fins_lower");
        this.fins_upper = this.MainBody.getChild("fins_upper");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -37.0F, -5.0F, 10.0F, 32.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-4.0F, -39.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 10).addBox(-3.0F, -41.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(40, 18).addBox(-2.0F, -42.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 25).addBox(-1.5F, -5.5F, 1.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 25).addBox(-1.5F, -5.5F, -2.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = MainBody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(60, 25).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -3.5F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r2 = MainBody.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(60, 25).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -3.5F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r3 = MainBody.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-3, 3).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2874F, -32.5F, -0.479F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition cube_r4 = MainBody.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-3, 3).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5125F, -32.5F, 3.771F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r5 = MainBody.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-3, 3).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5125F, -32.5F, -6.279F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r6 = MainBody.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-3, 3).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.7626F, -32.5F, -0.479F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition fins_lower = MainBody.addOrReplaceChild("fins_lower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = fins_lower.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, -3.5F, -0.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 42).addBox(0.0F, -6.5F, -0.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.75F, -4.5F, 5.75F, 0.0F, 2.3562F, 0.0F));

        PartDefinition cube_r8 = fins_lower.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 42).addBox(0.0F, -6.5F, -0.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 24).addBox(-2.0F, -3.5F, -0.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.75F, -4.5F, 5.75F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r9 = fins_lower.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(48, 51).addBox(-2.0F, -3.5F, -0.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 34).addBox(0.0F, -6.5F, -0.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7678F, -4.5F, -5.6607F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r10 = fins_lower.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(48, 42).addBox(-1.0F, -5.0F, -2.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5784F, -3.0F, -5.4142F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r11 = fins_lower.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(40, 24).addBox(-1.0F, -8.0F, -2.0F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1642F, -3.0F, -4.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition fins_upper = MainBody.addOrReplaceChild("fins_upper", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.0107F, -24.25F, 0.0179F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r12 = fins_upper.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(18, 52).addBox(-2.0F, -3.5F, -0.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 44).addBox(0.0F, -6.5F, -0.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7607F, 0.75F, 4.7321F, 0.0F, 2.3562F, 0.0F));

        PartDefinition cube_r13 = fins_upper.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(24, 42).addBox(0.0F, -6.5F, -0.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 33).addBox(-2.0F, -3.5F, -0.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.7393F, 0.75F, 4.7321F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r14 = fins_upper.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(12, 52).addBox(-2.0F, -3.5F, -0.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 42).addBox(0.0F, -6.5F, -0.5F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7785F, 0.75F, -4.6786F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r15 = fins_upper.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(6, 52).addBox(-1.0F, -5.0F, -2.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5677F, 2.25F, -4.4321F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r16 = fins_upper.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(8, 42).addBox(-1.0F, -8.0F, -2.0F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1535F, 2.25F, -3.0179F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    @Override
    public void setupAnim(Eagle500KgEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        MainBody.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    @Override
    public ModelPart root() {
        return MainBody;
    }
}
