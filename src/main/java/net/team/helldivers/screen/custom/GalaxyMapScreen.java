package net.team.helldivers.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.STeleportToDimensionPacket;
import net.team.helldivers.screen.custom.button.TexturedButton;
import net.team.helldivers.worldgen.dimension.ModDimensions;

public class GalaxyMapScreen extends AbstractContainerScreen<GalaxyMapMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/galaxy_map/galaxy_map.png");
    private static final ResourceLocation STARS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/galaxy_map/stars.png");
    private static final ResourceLocation OVERWORLD_NORMAL =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/galaxy_map/overworld_planet.png");
    private static final ResourceLocation OVERWORLD_HOVER =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/galaxy_map/overworld_planet_hover.png");
    private static final ResourceLocation CHOEPESSA_NORMAL =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/galaxy_map/choepessa_planet.png");
    private static final ResourceLocation CHOEPESSA_HOVER =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/galaxy_map/choepessa_planet_hover.png");

    public GalaxyMapScreen(GalaxyMapMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelX = 1000;
        this.inventoryLabelY = 1000;
        this.titleLabelX = 1000;
        this.titleLabelY = 1000;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.leftPos;
        int y = this.topPos;
        this.addRenderableWidget(new TexturedButton(
                x + 175, y + 55, 40, 40,
                OVERWORLD_NORMAL, OVERWORLD_HOVER,
                pButton -> PacketHandler.sendToServer(
                        new STeleportToDimensionPacket(Level.OVERWORLD.location())),
                "The Overworld"
        ));

        this.addRenderableWidget(new TexturedButton(
                x + 65, y + 15, 40, 40,
                CHOEPESSA_NORMAL, CHOEPESSA_HOVER,
                pButton -> PacketHandler.sendToServer(
                        new STeleportToDimensionPacket(ModDimensions.CHOEPESSA_DIM.location())),
                "Choepessa IV"
        ));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, STARS_TEXTURE);
        int x = ((width - imageWidth) / 2) - 75;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(BACKGROUND_TEXTURE,
                x, y, 319, 179, 0, 0, 426, 239,
                426, 239);
        guiGraphics.blit(STARS_TEXTURE,
                x + 4, y + 5, 309, 169, 0, 0, 768, 432,
                768, 432);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}