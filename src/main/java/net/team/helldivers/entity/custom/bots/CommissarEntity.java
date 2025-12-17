package net.team.helldivers.entity.custom.bots;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.team.helldivers.entity.goal.BotWalkAndShootGoal;

public class CommissarEntity extends AbstractBotEntity {
    public CommissarEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, false);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new BotWalkAndShootGoal(this, 1.0D, 2.0F, 40)); // speed, range, cooldown
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40f)
                .add(Attributes.MOVEMENT_SPEED, 0.35f)
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
