package net.team.helldivers.entity.custom.bots;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.team.helldivers.entity.goal.BotWalkAndShootGoal;

public class AutomatonTrooperEntity extends AbstractBotEntity {
    public AutomatonTrooperEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, false);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(1, new BotWalkAndShootGoal(this, 1.0D, 10.0F, 40)); // speed, range, cooldown
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 120f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_DAMAGE, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 25);
    }

    @Override
    public AABB getDamageHitbox() {
        AABB mainBox = this.getBoundingBox();
        double headHeight = mainBox.maxY - (this.getBbHeight() * 0.25);
        return new AABB(
                mainBox.minX,
                headHeight,
                mainBox.minZ,
                mainBox.maxX,
                mainBox.maxY,
                mainBox.maxZ
        );
    }
}
