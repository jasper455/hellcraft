package net.team.helldivers.gamerule;

import net.minecraft.world.level.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> DO_FLYING_BLOCKS =
            GameRules.register(
                    "doFlyingBlocks",
                    GameRules.Category.MISC,
                    GameRules.BooleanValue.create(false) // default value
            );
}