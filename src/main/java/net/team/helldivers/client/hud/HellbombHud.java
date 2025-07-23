package net.team.helldivers.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.team.helldivers.helper.ClientJammedSync;

import static net.team.helldivers.client.hud.StratagemHudOverlay.getCurrentFrame;

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
    private static int imgHeight;
    private static int translateHeight;
    private static int textHeight;
    private static int arrowHeight;

    public static void renderHellbombHud(GuiGraphics guiGraphics, int slotIndex) {
        if (slotIndex == 0) {
            imgHeight = 12;
            translateHeight = 10;
            textHeight = 12;
            arrowHeight = 22;
        }
        if (slotIndex == 1) {
            imgHeight = 38;
            translateHeight = 20;
            textHeight = 38;
            arrowHeight = 48;
        }
        if (slotIndex == 2) {
            imgHeight = 64;
            translateHeight = 30;
            textHeight = 64;
            arrowHeight = 74;
        }
        if (slotIndex == 3) {
            imgHeight = 90;
            translateHeight = 40;
            textHeight = 90;
            arrowHeight = 100;
        }

        int currentFrame = getCurrentFrame();
        int vOffset = currentFrame * 160;
        boolean isJammed = ClientJammedSync.getIsJammed();

        if (isJammed) {
            guiGraphics.blit(StratagemHudOverlay.JAMMED,
                    12, imgHeight, 20, 20,
                    0, vOffset, 16, 160,
                    16, 160 * 10); // full texture size
        } else {
            guiGraphics.blit(StratagemHudOverlay.HELLBOMB,
                    12, imgHeight, 20, 20, 0, 0, 16, 16,
                    16, 16);
        }

        guiGraphics.pose().pushPose();

        guiGraphics.pose().scale(0.7f, 0.7f, 0.7f);

        guiGraphics.pose().translate(16, translateHeight, 1);

        guiGraphics.drawString(Minecraft.getInstance().font, isJammed ? "§kHELLBOMB" : "HELLBOMB", 35, textHeight, 0xFFFFFF);

        guiGraphics.pose().popPose();

        if (!isJammed) {
            StratagemHudOverlay.renderDownArrow(guiGraphics, 35, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, firstInputDown);
            StratagemHudOverlay.renderUpArrow(guiGraphics, 45, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, secondInputDown);
            StratagemHudOverlay.renderLeftArrow(guiGraphics, 55, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, thirdInputDown);
            StratagemHudOverlay.renderDownArrow(guiGraphics, 65, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, fourthInputDown);
            StratagemHudOverlay.renderUpArrow(guiGraphics, 75, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, fifthInputDown);
            StratagemHudOverlay.renderRightArrow(guiGraphics, 85, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, sixthInputDown);
            StratagemHudOverlay.renderDownArrow(guiGraphics, 95, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, seventhInputDown);
            StratagemHudOverlay.renderUpArrow(guiGraphics, 105, arrowHeight, 10, 10,
                    0, 0, 16, 16, 16, 16, eighthInputDown);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, "JAMMED", 35, arrowHeight, 0xFFFFFF);
        }
    }

    public static void renderCooldownHud(GuiGraphics guiGraphics, int cooldownLeft) {
        int currentFrame = getCurrentFrame();
        int vOffset = currentFrame * 160;
        boolean isJammed = ClientJammedSync.getIsJammed();

        if (isJammed) {
            guiGraphics.blit(StratagemHudOverlay.JAMMED,
                    12, imgHeight, 20, 20,
                    0, vOffset, 16, 160,
                    16, 160 * 10); // full texture size
        } else {
            guiGraphics.blit(StratagemHudOverlay.HELLBOMB,
                    12, imgHeight, 20, 20, 0, 0, 16, 16,
                    16, 16);
        }

        guiGraphics.pose().pushPose();

        guiGraphics.pose().scale(0.7f, 0.7f, 0.7f);

        guiGraphics.pose().translate(16, translateHeight, 1);

        guiGraphics.drawString(Minecraft.getInstance().font, isJammed ? "§kHELLBOMB" : "HELLBOMB", 35, textHeight, 0xFFFFFF);

        guiGraphics.pose().popPose();

        if (!isJammed) {
            guiGraphics.drawString(Minecraft.getInstance().font, StratagemHudOverlay.percentageToTime(cooldownLeft, 8, 20),
                    35, arrowHeight, 0xFFFFFF);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, "JAMMED", 35, arrowHeight, 0xFFFFFF);
        }
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
