package net.team.helldivers.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.helper.ClientJammedSync;

import static net.team.helldivers.client.hud.StratagemHudOverlay.getCurrentFrame;

public class BackSlotHud {
    public static void renderBackSlotHud(GuiGraphics guiGraphics, ItemStack stack) {

        guiGraphics.pose().pushPose();

        guiGraphics.pose().popPose();

        renderBackSlot(guiGraphics, 100, 100, 22, 22, 22, 22,
                22, 22, 22, 22, stack);
    }

    public static void renderBackSlot(GuiGraphics guiGraphics, int pX, int pY, int pWidth,
                                      int pHeight, int pUOffset, int pVOffset, int pUWidth, int pVHeight,
                                      int pTextureWidth, int pTextureHeight, ItemStack stack) {
        guiGraphics.renderItem(stack, pX, pY - 22);
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, "textures/gui/backslot.png"),
                pX, pY, pWidth, pHeight, pUOffset, pVOffset, pUWidth, pVHeight,
                pTextureWidth, pTextureHeight);
    }

}
