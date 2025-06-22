package net.infinite1274.helldivers.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class HellbombHud {
    public static boolean firstInputDown = false;
    public static boolean secondInputDown = false;
    public static boolean thirdInputDown = false;
    public static boolean fourthInputDown = false;
    public static boolean fifthInputDown = false;
    public static boolean sixthInputDown = false;
    public static boolean seventhInputDown = false;
    public static boolean eighthInputDown = false;
    public static boolean allInputsDown = false;
    public static int inputStep = 0;

    public static void renderHellbombHud(
            GuiGraphics guiGraphics
    ) {

        guiGraphics.blit(StratagemHudOverlay.HELLBOMB,
                12, 12, 20, 20, 0, 0, 16, 16,
                16, 16);

        guiGraphics.pose().pushPose();

        guiGraphics.pose().scale(0.7f, 0.7f, 0.7f);

        guiGraphics.pose().translate(16, 10, 1);

        guiGraphics.drawString(Minecraft.getInstance().font, "HELLBOMB", 35, 12, 0xFFFFFF);

        guiGraphics.pose().popPose();


        StratagemHudOverlay.renderDownArrow(guiGraphics, 35, 22, 10, 10,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 45, 22, 10, 10,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 55, 22, 10, 10,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 65, 22, 10, 10,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 75, 22, 10, 10,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 85, 22, 10, 10,
                0, 0, 16, 16, 16, 16, sixthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 95, 22, 10, 10,
                0, 0, 16, 16, 16, 16, seventhInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 105, 22, 10, 10,
                0, 0, 16, 16, 16, 16, eighthInputDown);
    }

    public static void resetInputValues() {
        firstInputDown = false;
        secondInputDown = false;
        thirdInputDown = false;
        fourthInputDown = false;
        fifthInputDown = false;
        sixthInputDown = false;
        seventhInputDown = false;
        eighthInputDown = false;
        allInputsDown = false;
        inputStep = 0;
    }

}
