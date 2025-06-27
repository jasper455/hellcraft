package net.team.helldivers.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_HELLDIVERS = "key.category.helldivers";
        public static final String KEY_SHOW_STRATAGEM_HUD = "key.helldivers.show_stratagem_hud";
    public static final String KEY_UP_INPUT = "key.helldivers.up_input";
    public static final String KEY_DOWN_INPUT = "key.helldivers.down_input";
    public static final String KEY_LEFT_INPUT = "key.helldivers.left_input";
    public static final String KEY_RIGHT_INPUT = "key.helldivers.right_input";

    public static final KeyMapping SHOW_STRATAGEM_KEY = new KeyMapping(KEY_SHOW_STRATAGEM_HUD, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, KEY_CATEGORY_HELLDIVERS);

    public static final KeyMapping UP_INPUT_KEY = new KeyMapping(KEY_UP_INPUT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_W, KEY_CATEGORY_HELLDIVERS);

    public static final KeyMapping DOWN_INPUT_KEY = new KeyMapping(KEY_DOWN_INPUT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_S, KEY_CATEGORY_HELLDIVERS);

    public static final KeyMapping LEFT_INPUT_KEY = new KeyMapping(KEY_LEFT_INPUT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_A, KEY_CATEGORY_HELLDIVERS);

    public static final KeyMapping RIGHT_INPUT_KEY = new KeyMapping(KEY_RIGHT_INPUT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_D, KEY_CATEGORY_HELLDIVERS);

}
