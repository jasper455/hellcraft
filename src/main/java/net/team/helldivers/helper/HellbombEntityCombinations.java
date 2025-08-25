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
