package net.infinite1274.helldivers.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PrecisionStrikeHud {
    public static boolean firstInputDown = false;
    public static boolean secondInputDown = false;
    public static boolean thirdInputDown = false;
    public static boolean allInputsDown = false;
    public static int inputStep = 0;


    public static void renderPrecisionStrikeHud(
            GuiGraphics guiGraphics
    ) {

        guiGraphics.blit(StratagemHudOverlay.PRECISION_STRIKE,
                12, 38, 20, 20, 0, 0, 31, 31,
                31, 31);

        guiGraphics.pose().pushPose();

        guiGraphics.pose().scale(0.7f, 0.7f, 0.7f);

        guiGraphics.pose().translate(16, 20, 1);

        guiGraphics.drawString(Minecraft.getInstance().font, "ORBITAL PRECISION STRIKE", 35, 38, 0xFFFFFF);

        guiGraphics.pose().popPose();


        StratagemHudOverlay.renderRightArrow(guiGraphics, 35, 48, 10, 10,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 45, 48, 10, 10,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 55, 48, 10, 10,
                0, 0, 16, 16, 16, 16, thirdInputDown);
    }

    public static void resetInputValues() {
        firstInputDown = false;
        secondInputDown = false;
        thirdInputDown = false;
        allInputsDown = false;
        PrecisionStrikeHud.inputStep = 0;
    }

}
