package net.team.helldivers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.team.helldivers.entity.custom.StratagemOrbEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class StratagemOrbProjectileModel extends EntityModel<StratagemOrbEntity> {
    private final ModelPart stratagem_orb;

    public StratagemOrbProjectileModel(ModelPart root) {
        this.stratagem_orb = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition mainBody = partdefinition.addOrReplaceChild("mainBody", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, -4.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 6).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = mainBody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-3.0F, -1.0F, -2.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.5F, 0.5F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r2 = mainBody.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -2.0F, -2.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.5F, 0.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r3 = mainBody.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 14).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r4 = mainBody.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(16, 10).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(StratagemOrbEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}


    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        stratagem_orb.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
