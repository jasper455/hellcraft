package net.team.helldivers.screen.custom.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.team.helldivers.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

public class TexturedButton extends Button {

    private final ResourceLocation normalTexture;
    private final ResourceLocation hoverTexture;
    private final int textureWidth;
    private final int textureHeight;
    private final String planetName;

    public TexturedButton(int x, int y, int width, int height,
                          ResourceLocation normalTexture,
                          ResourceLocation hoverTexture,
                          OnPress onPress, String planetName) {
        super(x, y, width, height, net.minecraft.network.chat.Component.empty(), onPress, DEFAULT_NARRATION);
        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.textureWidth = width;
        this.textureHeight = height;
        this.planetName = planetName;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation textureToUse = isHovered() ? hoverTexture : normalTexture;
        graphics.blit(textureToUse, getX(), getY(), 0, 0, width, height, textureWidth, textureHeight);
        if (isHovered) {
            graphics.renderTooltip(
                    Minecraft.getInstance().font,
                    Component.literal(planetName),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    public void playDownSound(SoundManager pHandler) {
        Minecraft.getInstance().player.playSound(
                ModSounds.PLANET_SELECT.get(), // <- Replace this with your sound
                1.0F, // volume
                1.0F  // pitch
        );
    }
}