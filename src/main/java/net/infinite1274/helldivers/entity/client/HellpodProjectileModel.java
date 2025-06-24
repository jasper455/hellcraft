package net.infinite1274.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.infinite1274.helldivers.entity.custom.MissileProjectileEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class HellpodProjectileModel extends HierarchicalModel<MissileProjectileEntity> {
    private final ModelPart MainBody;
    private final ModelPart upper;
    private final ModelPart fins;
    private final ModelPart logo;
    private final ModelPart middle;
    private final ModelPart inner;
    private final ModelPart lower;

    public HellpodProjectileModel(ModelPart root) {
        this.MainBody = root.getChild("MainBody");
        this.upper = this.MainBody.getChild("upper");
        this.fins = this.upper.getChild("fins");
        this.logo = this.upper.getChild("logo");
        this.middle = this.MainBody.getChild("middle");
        this.inner = this.middle.getChild("inner");
        this.lower = this.MainBody.getChild("lower");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create().texOffs(21, 0).addBox(-2.0F, -52.75F, -1.75F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition upper = MainBody.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -50.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r1 = upper.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 137).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, -0.4259F, 0.0F, -2.0944F, 0.0F, 1.5708F));

        PartDefinition cube_r2 = upper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(84, 132).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, -0.4009F, -0.175F, -1.0472F, 0.0F, 1.5708F));

        PartDefinition cube_r3 = upper.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(52, 133).addBox(-1.0F, -14.5F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5179F, -0.4509F, -0.1F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r4 = upper.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(117, 132).addBox(-1.0F, -14.0F, -6.0F, 2.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1865F, 11.0F, 9.134F, 2.6614F, -0.9719F, -2.579F));

        PartDefinition cube_r5 = upper.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(89, 83).addBox(-1.0F, -14.0F, -6.0F, 2.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.1878F, 11.0F, 9.634F, 0.4802F, -0.9719F, -0.5626F));

        PartDefinition cube_r6 = upper.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(57, 109).addBox(-1.0F, -13.5F, -5.0F, 2.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9365F, 10.5F, -9.384F, -2.6614F, 0.9719F, -2.579F));

        PartDefinition cube_r7 = upper.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(89, 108).addBox(-1.0F, -13.5F, -5.0F, 2.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.1878F, 10.5F, -9.884F, -0.4802F, 0.9719F, -0.5626F));

        PartDefinition cube_r8 = upper.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(91, 26).addBox(-1.0F, -13.5F, -5.0F, 2.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 10.5F, -0.5F, 0.0F, 0.0F, -0.3054F));

        PartDefinition cube_r9 = upper.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(91, 1).addBox(-1.0F, -13.5F, -5.0F, 2.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.2487F, 10.5F, -0.25F, 0.0F, 0.0F, 0.3054F));

        PartDefinition fins = upper.addOrReplaceChild("fins", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.1808F, 1.206F, -0.0316F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r10 = fins.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(144, 0).addBox(-1.0F, 0.0F, -6.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7346F, -3.206F, -6.9684F, -1.0199F, 0.3007F, -1.3908F));

        PartDefinition cube_r11 = fins.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(144, 123).addBox(0.0F, 0.0F, -5.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.4692F, -3.206F, -0.5633F, 0.0F, 0.0F, 1.2217F));

        PartDefinition cube_r12 = fins.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(28, 137).addBox(-1.0F, 0.0F, -6.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4846F, -3.206F, 7.2816F, 1.0199F, -0.3007F, -1.3908F));

        PartDefinition logo = upper.addOrReplaceChild("logo", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.8269F, 0.2692F, -2.4423F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(-1, 6).addBox(-1.8269F, 0.7692F, -2.4423F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.3269F, 0.2692F, 1.5577F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 2).addBox(1.6731F, 0.2692F, -1.4423F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 2).addBox(-0.8269F, 0.2692F, -1.4423F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.3269F, 0.2692F, -1.9423F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.3269F, 0.2692F, -1.4423F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.3269F, 0.2692F, -0.4423F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.3269F, 0.2692F, 0.0577F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 2).addBox(-0.3269F, 0.2692F, 1.0577F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0769F, -2.5192F, 0.1923F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r13 = logo.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, -0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4231F, 0.0192F, 1.3077F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r14 = logo.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0769F, 0.0192F, -0.6923F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r15 = logo.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0769F, 0.0192F, 0.8077F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r16 = logo.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5769F, 0.0192F, 1.3077F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r17 = logo.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0769F, 0.0192F, -0.1923F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r18 = logo.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0769F, 0.0192F, 0.3077F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r19 = logo.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9231F, 0.0192F, 1.3077F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r20 = logo.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(8, 2).addBox(0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0769F, 0.0192F, -0.6923F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r21 = logo.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(8, 2).addBox(0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0769F, 0.0192F, 1.3077F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r22 = logo.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(8, 2).addBox(0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5769F, 0.0192F, 1.8077F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r23 = logo.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(8, 2).addBox(0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5769F, 0.0192F, 0.3077F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r24 = logo.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(8, 2).addBox(0.25F, 0.25F, 0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5769F, 0.0192F, -1.1923F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r25 = logo.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(8, 2).addBox(-0.25F, 0.25F, -0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4231F, 0.0192F, -1.6923F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r26 = logo.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(7, 2).addBox(-1.25F, 0.25F, -0.25F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5769F, 0.0192F, 0.3077F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r27 = logo.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(3, 2).addBox(-2.25F, 0.25F, -0.25F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0769F, 0.0192F, 0.3077F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r28 = logo.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(3, 2).addBox(-2.25F, 0.25F, -0.25F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9231F, 0.0192F, 0.3077F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r29 = logo.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(3, 2).addBox(-2.25F, 0.25F, -0.25F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9231F, 0.0192F, 0.3077F, 0.0F, -1.5708F, 0.0F));

        PartDefinition middle = MainBody.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(30, 28).addBox(-13.3695F, -1.4643F, -5.4582F, 1.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(10.3792F, -1.4643F, -5.4582F, 1.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5418F, -38.5357F, -0.8708F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r30 = middle.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(60, 28).addBox(0.0F, -8.0F, -7.0F, 1.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.8073F, 6.5357F, 11.4258F, 3.1416F, -1.0472F, -3.1416F));

        PartDefinition cube_r31 = middle.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 28).addBox(0.0F, -8.0F, -7.0F, 1.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8171F, 6.5357F, 11.4258F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r32 = middle.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(30, 56).addBox(0.0F, -4.0F, -7.0F, 1.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.8073F, 2.5357F, -8.3422F, 0.0F, 2.0944F, 0.0F));

        PartDefinition cube_r33 = middle.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -4.0F, -7.0F, 1.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8171F, 2.5357F, -8.3422F, 0.0F, 1.0472F, 0.0F));

        PartDefinition cube_r34 = middle.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.6208F, 2.5357F, 1.6418F, -3.1416F, 0.0F, -1.5708F));

        PartDefinition cube_r35 = middle.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9292F, 2.5357F, -10.3582F, -1.0472F, 0.0F, -1.5708F));

        PartDefinition cube_r36 = middle.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.7792F, 5.5357F, 1.4918F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r37 = middle.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9708F, 5.5357F, 13.3918F, 2.0944F, 0.0F, -1.5708F));

        PartDefinition cube_r38 = middle.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7792F, 2.5357F, 11.1918F, -2.0944F, 0.0F, -1.5708F));

        PartDefinition cube_r39 = middle.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9708F, 5.5357F, -10.3582F, -2.0944F, 0.0F, -1.5708F));

        PartDefinition inner = middle.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(60, 56).addBox(-10.0346F, -4.0F, -5.134F, 2.0F, 14.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(8.75F, -4.0F, -5.134F, 2.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6208F, 2.6607F, 1.7918F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r40 = inner.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(88, 56).addBox(0.0F, -8.0F, -6.0F, 2.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8385F, 4.0F, 8.134F, 3.1416F, -1.0472F, -3.1416F));

        PartDefinition cube_r41 = inner.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(28, 84).addBox(0.0F, -8.0F, -6.0F, 2.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5538F, 4.0F, 8.134F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r42 = inner.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(0, 84).addBox(0.0F, -4.0F, -6.0F, 2.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8385F, 0.0F, -6.4019F, 0.0F, 2.0944F, 0.0F));

        PartDefinition cube_r43 = inner.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(60, 82).addBox(0.0F, -4.0F, -6.0F, 2.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5538F, 0.0F, -6.4019F, 0.0F, 1.0472F, 0.0F));

        PartDefinition lower = MainBody.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.75F, -22.0F, 0.25F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r44 = lower.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7375F, -1.0F, -7.529F, 2.1072F, 0.0801F, -1.5156F));

        PartDefinition cube_r45 = lower.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4125F, -1.0F, 0.771F, 0.012F, 0.0121F, -1.6575F));

        PartDefinition cube_r46 = lower.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(-1, 6).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.3678F, 3.1451F, 0.4872F, 0.0066F, -0.0111F, -1.48F));

        PartDefinition cube_r47 = lower.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(-1, 6).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9322F, 3.1451F, 9.7372F, 2.0958F, -0.0726F, -1.6194F));

        PartDefinition cube_r48 = lower.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7625F, 3.5F, -9.329F, -2.096F, 0.0756F, -1.6145F));

        PartDefinition cube_r49 = lower.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(-1, 6).addBox(-1.75F, -1.25F, -2.25F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.9875F, -1.0F, 8.771F, -2.096F, -0.0756F, -1.5271F));

        PartDefinition cube_r50 = lower.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(96, 156).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, 19.5991F, 0.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r51 = lower.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(80, 156).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, 18.5991F, 0.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r52 = lower.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(144, 150).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, 17.3491F, 0.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r53 = lower.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(118, 27).addBox(-1.0F, -5.0F, -5.0F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, 16.0991F, 0.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r54 = lower.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(142, 99).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, 14.3491F, 0.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r55 = lower.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -7.0F, 2.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1429F, 12.5991F, 0.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r56 = lower.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(142, 51).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1071F, 10.5741F, 0.8F, -2.0944F, 0.0F, 1.5708F));

        PartDefinition cube_r57 = lower.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(142, 27).addBox(-1.0F, -14.5F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3929F, 10.5491F, 0.8F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r58 = lower.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(142, 75).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1071F, 10.5991F, 0.8F, -1.0472F, 0.0F, 1.5708F));

        PartDefinition cube_r59 = lower.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(118, 0).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3707F, -4.0F, 9.0F, -2.9918F, -1.0406F, 2.9684F));

        PartDefinition cube_r60 = lower.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(116, 104).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5538F, -4.0F, 9.5F, -0.1498F, -1.0406F, 0.1732F));

        PartDefinition cube_r61 = lower.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(116, 77).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5047F, -4.0F, -8.1899F, 2.9918F, 1.0406F, 2.9684F));

        PartDefinition cube_r62 = lower.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(116, 50).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4199F, -4.0F, -7.6899F, 0.1498F, 1.0406F, 0.1732F));

        PartDefinition cube_r63 = lower.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(26, 110).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.9491F, -4.0F, 1.1551F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r64 = lower.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(0, 110).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.9F, -4.0F, 0.1551F, 0.0F, 0.0F, -0.0873F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }


    @Override
    public void setupAnim(MissileProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        MainBody.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    @Override
    public ModelPart root() {
        return MainBody;
    }
}
