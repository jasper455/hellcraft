package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.team.helldivers.client.hud.StratagemHudOverlay;
import net.team.helldivers.screen.custom.HellbombInputScreen;
import net.team.helldivers.sound.ModSounds;

public class HellbombCombinations {
    // Different possible combinations for the hellbomb activation code

    public static void combo1render(int x, int y, GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderRightArrow(guiGraphics, x, y, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, x + 20, y, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, x + 40, y, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, x + 60, y, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, x + 80, y, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, x + 100, y, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo1Inputs(HellbombInputScreen screen, boolean upPressed, boolean downPressed,
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

    public static void combo2render(int x, int y, GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderDownArrow(guiGraphics, x, y, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, x + 20, y, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, x + 40, y, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, x + 60, y, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, x + 80, y, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, x + 100, y, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo2Inputs(HellbombInputScreen screen, boolean upPressed, boolean downPressed,
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

    public static void combo3render(int x, int y, GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderLeftArrow(guiGraphics, x, y, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, x + 20, y, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, x + 40, y, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, x + 60, y, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, x + 80, y, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderUpArrow(guiGraphics, x + 100, y, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo3Inputs(HellbombInputScreen screen, boolean upPressed, boolean downPressed,
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

    public static void combo4render(int x, int y, GuiGraphics guiGraphics, boolean firstInputDown, boolean secondInputDown, boolean thirdInputDown,
                                    boolean fourthInputDown, boolean fifthInputDown, boolean sixthInputDown) {
        StratagemHudOverlay.renderUpArrow(guiGraphics, x, y, 20, 20,
                0, 0, 16, 16, 16, 16, firstInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, x + 20, y, 20, 20,
                0, 0, 16, 16, 16, 16, secondInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, x + 40, y, 20, 20,
                0, 0, 16, 16, 16, 16, thirdInputDown);
        StratagemHudOverlay.renderRightArrow(guiGraphics, x + 60, y, 20, 20,
                0, 0, 16, 16, 16, 16, fourthInputDown);
        StratagemHudOverlay.renderLeftArrow(guiGraphics, x + 80, y, 20, 20,
                0, 0, 16, 16, 16, 16, fifthInputDown);
        StratagemHudOverlay.renderDownArrow(guiGraphics, x + 100, y, 20, 20,
                0, 0, 16, 16, 16, 16, sixthInputDown);
    }

    public static void combo4Inputs(HellbombInputScreen screen, boolean upPressed, boolean downPressed,
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
