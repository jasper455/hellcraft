package net.team.helldivers.gamerule;

import net.minecraft.world.level.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> DO_FLYING_BLOCKS =
            GameRules.register(
                    "doFlyingBlocks",
                    GameRules.Category.MISC,
                    GameRules.BooleanValue.create(false) // default value
            );
    public static final GameRules.Key<GameRules.IntegerValue> FLYING_BLOCKS_INTENSITY =
            GameRules.register(
                    "flyingBlocksIntensity",
                    GameRules.Category.MISC,
                    GameRules.IntegerValue.create(3) // default value
            );
    public static final GameRules.Key<GameRules.BooleanValue> DO_AIM_DOWN_SIGHT =
            GameRules.register(
                    "doAimDownSight",
                    GameRules.Category.MISC,
                    GameRules.BooleanValue.create(false) // default value
            );
}