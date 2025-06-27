package net.team.helldivers.client.shader.post.tint;

import com.mojang.blaze3d.vertex.PoseStack;
import net.team.helldivers.HellcraftMod;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

public class TintPostProcessor extends PostProcessor {
    public static final TintPostProcessor INSTANCE = new TintPostProcessor();
    // Static block added to turn the shader off by default
    static {
        INSTANCE.setActive(false);
    }

    @Override
    public ResourceLocation getPostChainLocation() {
        return ResourceLocation.fromNamespaceAndPath(HellcraftMod.MOD_ID, "tint");
    }

    @Override
    public void beforeProcess(PoseStack poseStack) {

    }

    @Override
    public void afterProcess() {

    }
}