package net.jasper.lodestonemod.client.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

public class ModRenderTypes {
    public static final ResourceLocation SKY_ONE = ResourceLocation.fromNamespaceAndPath("lodestonemod",
            "textures/environment/sky_one.png");
    public static final ResourceLocation SKY_TWO = ResourceLocation.fromNamespaceAndPath("lodestonemod",
            "textures/environment/sky_two.png");
    public static final ResourceLocation SKY_THREE = ResourceLocation.fromNamespaceAndPath("lodestonemod",
            "textures/environment/sky_three.png");
    public static final ResourceLocation STARS = ResourceLocation.fromNamespaceAndPath("lodestonemod",
            "textures/environment/stars.png");

    public static final RenderType CUSTOM_PORTAL = RenderType.create(
            "custom_portal", // Name
            DefaultVertexFormat.POSITION_TEX, // Format
            VertexFormat.Mode.QUADS, // Drawing mode
            512, // Buffer size
            false, // Affects crumbling
            false, // Sort on upload
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(
                            () -> Minecraft.getInstance().gameRenderer.getShader("lodestonemod:rendertype_custom_sky")
                    ))
                    .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                            .add(SKY_THREE, false, false)
                            .add(STARS, false, false)
                            .build())
//                    .setTextureState(new RenderStateShard.TextureStateShard(STARS, false, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard("no_transparency", () -> {
                        RenderSystem.disableBlend();
                    }, () -> {
                    }))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .createCompositeState(false)
    );
}