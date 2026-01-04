package net.team.helldivers.entity.goal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.team.helldivers.util.ShootHelper;

import java.util.EnumSet;

public class BotWalkAndShootGoal extends Goal {
    private final Mob mob;
    private final double speed;
    private final float attackRange;
    private final int attackCooldown; // ticks
    private int cooldownTicks;

    public BotWalkAndShootGoal(Mob mob, double speed, float attackRange, int attackCooldown) {
        this.mob = mob;
        this.speed = speed;
        this.attackRange = attackRange;
        this.attackCooldown = attackCooldown;
        this.cooldownTicks = 0;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null && mob.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        mob.getLookControl().setLookAt(target, 180f, 180f);
        double distanceSq = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());

        if (distanceSq > (attackRange * attackRange)) {
            mob.getNavigation().moveTo(target, speed); // Move toward player like zombie
        } else {
            mob.getNavigation().stop();
//            if (cooldownTicks <= 0) {
//                target.sendSystemMessage(Component.literal("test"));
                shootAt(target); // Custom shoot logic
                cooldownTicks = attackCooldown;
//            }
        }

        if (cooldownTicks > 0) {
            cooldownTicks--;
        }
    }

    private void shootAt(LivingEntity target) {
        // Your shooting logic here (spawn projectile, play animation, etc.)
        if (target != null && !mob.level().isClientSide) {
            mob.getLookControl().setLookAt(target, 180f, 180f);
            ShootHelper.shoot(mob, mob.level(), 0, 5, 0.3, false);
        }
    }
}