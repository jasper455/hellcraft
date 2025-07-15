package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.team.helldivers.client.hud.StratagemHudOverlay;
import net.team.helldivers.screen.custom.HellbombEntityInputScreen;
import net.team.helldivers.screen.custom.HellbombInputScreen;
import net.team.helldivers.sound.ModSounds;

public class HellbombEntityCombinations {
    // Different possible combinations for the hellbomb activation code

    public static void combo1render(GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderRightArrow(guiGraphics, 260, 162, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 280, 162, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 300, 162, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 320, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 340, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 360, 162, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo1Inputs(HellbombEntityInputScreen screen, boolean upPressed, boolean downPressed,
                                    boolean leftPressed, boolean rightPressed) {
        Player player = Minecraft.getInstance().player;
                if (rightPressed && screen.inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.firstInputDown = true;
                    screen.inputStep++;
                }
                if (leftPressed && screen.inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.secondInputDown = true;
                    screen.inputStep++;
                }
                if (rightPressed && screen.inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.thirdInputDown = true;
                    screen.inputStep++;
            }
                if (downPressed && screen.inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fourthInputDown = true;
                    screen.inputStep++;
                }
                if (upPressed && screen.inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fifthInputDown = true;
                    screen.inputStep++;
            }
                if (rightPressed && screen.inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.sixthInputDown = true;
                    screen.allInputsDown = true;
                    screen.inputStep++;
                }
    }

    public static void combo2render(GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderDownArrow(guiGraphics, 260, 162, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 280, 162, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 300, 162, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 320, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 340, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 360, 162, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo2Inputs(HellbombEntityInputScreen screen, boolean upPressed, boolean downPressed,
                                    boolean leftPressed, boolean rightPressed) {
        Player player = Minecraft.getInstance().player;
                if (downPressed && screen.inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.firstInputDown = true;
                    screen.inputStep++;
                }
                if (rightPressed && screen.inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.secondInputDown = true;
                    screen.inputStep++;
                }
                if (upPressed && screen.inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.thirdInputDown = true;
                    screen.inputStep++;
            }
                if (leftPressed && screen.inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fourthInputDown = true;
                    screen.inputStep++;
                }
                if (downPressed && screen.inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fifthInputDown = true;
                    screen.inputStep++;
            }
                if (leftPressed && screen.inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.sixthInputDown = true;
                    screen.allInputsDown = true;
                    screen.inputStep++;
                }
    }

    public static void combo3render(GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 260, 162, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 280, 162, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 300, 162, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 320, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 340, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, 360, 162, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo3Inputs(HellbombEntityInputScreen screen, boolean upPressed, boolean downPressed,
                                    boolean leftPressed, boolean rightPressed) {
        Player player = Minecraft.getInstance().player;
                if (leftPressed && screen.inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.firstInputDown = true;
                    screen.inputStep++;
                }
                if (downPressed && screen.inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.secondInputDown = true;
                    screen.inputStep++;
                }
                if (leftPressed && screen.inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.thirdInputDown = true;
                    screen.inputStep++;
            }
                if (rightPressed && screen.inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fourthInputDown = true;
                    screen.inputStep++;
                }
                if (downPressed && screen.inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fifthInputDown = true;
                    screen.inputStep++;
            }
                if (upPressed && screen.inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.sixthInputDown = true;
                    screen.allInputsDown = true;
                    screen.inputStep++;
                }
    }

    public static void combo4render(GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderUpArrow(guiGraphics, 260, 162, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 280, 162, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 300, 162, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, 320, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, 340, 162, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, 360, 162, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo4Inputs(HellbombEntityInputScreen screen, boolean upPressed, boolean downPressed,
                                    boolean leftPressed, boolean rightPressed) {
        Player player = Minecraft.getInstance().player;
                if (upPressed && screen.inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.firstInputDown = true;
                    screen.inputStep++;
                }
                if (rightPressed && screen.inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.secondInputDown = true;
                    screen.inputStep++;
                }
                if (downPressed && screen.inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.thirdInputDown = true;
                    screen.inputStep++;
            }
                if (rightPressed && screen.inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fourthInputDown = true;
                    screen.inputStep++;
                }
                if (leftPressed && screen.inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.fifthInputDown = true;
                    screen.inputStep++;
            }
                if (downPressed && screen.inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    screen.sixthInputDown = true;
                    screen.allInputsDown = true;
                    screen.inputStep++;
                }
    }
}
