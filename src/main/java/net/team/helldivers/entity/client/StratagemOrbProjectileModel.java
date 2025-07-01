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

        PartDefinition mainBody = partdefinition.addOrReplaceChild("mainBody", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition cube_r1 = mainBody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7418F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }


    @Override
    public void setupAnim(StratagemOrbEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}


    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        stratagem_orb.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
