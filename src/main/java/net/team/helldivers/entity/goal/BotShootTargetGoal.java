package net.team.helldivers.entity.goal;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.team.helldivers.entity.custom.bots.AbstractBotEntity;
import net.team.helldivers.util.ShootHelper;

import java.util.EnumSet;

public class BotShootTargetGoal extends Goal {
    private final AbstractBotEntity botEntity;

    public BotShootTargetGoal(AbstractBotEntity botEntity) {
        this.botEntity = botEntity;
    }

    @Override
    public boolean canUse() {
        return botEntity.getTarget() != null && botEntity.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = botEntity.getTarget();
        if (target != null) {
            botEntity.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
            //if(botEntity.tickCount % 5==0){
                ShootHelper.shoot(botEntity, botEntity.level(), 0.2, 5, 0.3, false);
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return EnumSet.of(Goal.Flag.LOOK); // Notice: NO Goal.Flag.MOVE here
    }
}