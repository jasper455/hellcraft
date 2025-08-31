package net.team.helldivers.entity.custom.bots;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.entity.goal.BotRangedAttackGoal;

public class AutomatonCannonEntity extends AbstractBotEntity {
    public AutomatonCannonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ServerPlayer.class, false, true));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Villager.class, false, true));
//        this.goalSelector.addGoal(1, new BotRangedAttackGoal(this, 1.25, 60, 64f) {
//            @Override
//            public boolean canContinueToUse() {
//                return this.canUse();
//            }
//        });
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150f)
                .add(Attributes.MOVEMENT_SPEED, 0f)
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

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 60 == 0) {
            Pig pig = new Pig(EntityType.PIG, this.level());
            pig.setPos(this.position());
            this.level().addFreshEntity(pig);
            shootFromRotation(this, pig, this.getXRot(), this.getYHeadRot(), 0.0f, 5, 0);
        }
    }
    public void shoot(Entity shotEntity, double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(shotEntity.level().random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), shotEntity.level().random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), shotEntity.level().random.triangle(0.0D, 0.0172275D * (double)pInaccuracy)).scale((double)pVelocity);
        shotEntity.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        shotEntity.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        shotEntity.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        shotEntity.yRotO = shotEntity.getYRot();
        shotEntity.xRotO = shotEntity.getXRot();
    }

    public void shootFromRotation(Entity pShooter, Entity shotEntity, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        shoot(shotEntity, (double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        shotEntity.setDeltaMovement(shotEntity.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0D : vec3.y, vec3.z));
    }
}
