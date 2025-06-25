package net.infinite1274.helldivers.client.hud;

import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.util.KeyBinding;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
public class StratagemHudOverlay {
    public static boolean wasUpPressedLastTick = false;
    public static boolean wasUpPressedThisTick = false;

    public static boolean wasDownPressedLastTick = false;
    public static boolean wasDownPressedThisTick = false;

    public static boolean wasLeftPressedLastTick = false;
    public static boolean wasLeftPressedThisTick = false;

    public static boolean wasRightPressedLastTick = false;
    public static boolean wasRightPressedThisTick = false;

    public static boolean wasUpNotPressedLastTick = false;
    public static boolean wasUpNotPressedThisTick = false;

    public static boolean wasDownNotPressedLastTick = false;
    public static boolean wasDownNotPressedThisTick = false;

    public static boolean wasLeftNotPressedLastTick = false;
    public static boolean wasLeftNotPressedThisTick = false;

    public static boolean wasRightNotPressedLastTick = false;
    public static boolean wasRightNotPressedThisTick = false;


    public static final ResourceLocation STRATAGEM_BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            HelldiversMod.MOD_ID, "textures/stratagem_hud/stratagem_background.png");

    public static final ResourceLocation HELLBOMB = ResourceLocation.fromNamespaceAndPath(
            HelldiversMod.MOD_ID, "textures/stratagem_hud/hellbomb_gui.png");

    public static final ResourceLocation PRECISION_STRIKE = ResourceLocation.fromNamespaceAndPath(
            HelldiversMod.MOD_ID, "textures/stratagem_hud/precision_strike_gui.png");

    public static final ResourceLocation SMALL_BARRAGE = ResourceLocation.fromNamespaceAndPath(
            HelldiversMod.MOD_ID, "textures/stratagem_hud/120_barrage_gui.png");

    public static final ResourceLocation BIG_BARRAGE = ResourceLocation.fromNamespaceAndPath(
            HelldiversMod.MOD_ID, "textures/stratagem_hud/380_barrage_gui.png");

    public static void renderUpArrow(GuiGraphics guiGraphics, int pX, int pY, int pWidth,
                                        int pHeight, int pUOffset, int pVOffset, int pUWidth, int pVHeight,
                                        int pTextureWidth, int pTextureHeight, boolean isFilled) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, isFilled ? "textures/stratagem_hud/arrows/arrow_up.png" :
                                "textures/stratagem_hud/arrows/arrow_up_hollow.png"),
                pX, pY, pWidth, pHeight, pUOffset, pVOffset, pUWidth, pVHeight,
                pTextureWidth, pTextureHeight);
//        Minecraft.getInstance().player.sendSystemMessage(Component.literal("test"));
    }

    public static void renderDownArrow(GuiGraphics guiGraphics, int pX, int pY, int pWidth,
                                        int pHeight, int pUOffset, int pVOffset, int pUWidth, int pVHeight,
                                        int pTextureWidth, int pTextureHeight, boolean isFilled) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, isFilled ? "textures/stratagem_hud/arrows/arrow_down.png" :
                                "textures/stratagem_hud/arrows/arrow_down_hollow.png"),
                pX, pY, pWidth, pHeight, pUOffset, pVOffset, pUWidth, pVHeight,
                pTextureWidth, pTextureHeight);
//        Minecraft.getInstance().player.sendSystemMessage(Component.literal("test"));
    }

    public static void renderLeftArrow(GuiGraphics guiGraphics, int pX, int pY, int pWidth,
                                        int pHeight, int pUOffset, int pVOffset, int pUWidth, int pVHeight,
                                        int pTextureWidth, int pTextureHeight, boolean isFilled) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, isFilled ? "textures/stratagem_hud/arrows/arrow_left.png" :
                                "textures/stratagem_hud/arrows/arrow_left_hollow.png"),
                pX, pY, pWidth, pHeight, pUOffset, pVOffset, pUWidth, pVHeight,
                pTextureWidth, pTextureHeight);
//        Minecraft.getInstance().player.sendSystemMessage(Component.literal("test"));
    }

    public static void renderRightArrow(GuiGraphics guiGraphics, int pX, int pY, int pWidth,
                                        int pHeight, int pUOffset, int pVOffset, int pUWidth, int pVHeight,
                                        int pTextureWidth, int pTextureHeight, boolean isFilled) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        HelldiversMod.MOD_ID, isFilled ? "textures/stratagem_hud/arrows/arrow_right.png" :
                                "textures/stratagem_hud/arrows/arrow_right_hollow.png"),
                pX, pY, pWidth, pHeight, pUOffset, pVOffset, pUWidth, pVHeight,
                pTextureWidth, pTextureHeight);
//        Minecraft.getInstance().player.sendSystemMessage(Component.literal("test"));
    }

    public static boolean upArrowPressed() {
        boolean currentlyPressed = KeyBinding.UP_INPUT_KEY.isDown();
        boolean justPressed = currentlyPressed && !wasUpPressedLastTick;
        wasUpPressedLastTick = currentlyPressed;
        return justPressed;
    }

    public static boolean downArrowPressed() {
        boolean currentlyPressed = KeyBinding.DOWN_INPUT_KEY.isDown();
        boolean justPressed = currentlyPressed && !wasDownPressedLastTick;
        wasDownPressedLastTick = currentlyPressed;
        return justPressed;
    }

    public static boolean leftArrowPressed() {
        boolean currentlyPressed = KeyBinding.LEFT_INPUT_KEY.isDown();
        boolean justPressed = currentlyPressed && !wasLeftPressedLastTick;
        wasLeftPressedLastTick = currentlyPressed;
        return justPressed;
    }

    public static boolean rightArrowPressed() {
        boolean currentlyPressed = KeyBinding.RIGHT_INPUT_KEY.isDown();
        boolean justPressed = currentlyPressed && !wasRightPressedLastTick;
        wasRightPressedLastTick = currentlyPressed;
        return justPressed;
    }

    public static boolean notUpArrowPressed() {
        return KeyBinding.DOWN_INPUT_KEY.isDown() || KeyBinding.LEFT_INPUT_KEY.isDown() || KeyBinding.RIGHT_INPUT_KEY.isDown();
    }

    public static boolean notDownArrowPressed() {
        return KeyBinding.UP_INPUT_KEY.isDown() || KeyBinding.LEFT_INPUT_KEY.isDown() || KeyBinding.RIGHT_INPUT_KEY.isDown();
    }

    public static boolean notLeftArrowPressed() {
        return KeyBinding.UP_INPUT_KEY.isDown() || KeyBinding.DOWN_INPUT_KEY.isDown() || KeyBinding.RIGHT_INPUT_KEY.isDown();
    }

    public static boolean notRightArrowPressed() {
        return KeyBinding.UP_INPUT_KEY.isDown() || KeyBinding.DOWN_INPUT_KEY.isDown() || KeyBinding.LEFT_INPUT_KEY.isDown();
    }

}
