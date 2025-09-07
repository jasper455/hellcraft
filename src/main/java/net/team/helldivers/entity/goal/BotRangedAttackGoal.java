package net.team.helldivers.entity.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

// I quite literally pasted this in almost 1-1 from the MCreator version, so excuse any bad variable names / code
public class BotRangedAttackGoal extends Goal {
		private final Mob mob;
		private final Entity rangedAttackMob;
		@Nullable
		private LivingEntity target;
		private int attackTime = -1;
		private final double speedModifier;
		private int seeTime;
		private final int attackIntervalMin;
		private final int attackIntervalMax;
		private final float attackRadius;
		private final float attackRadiusSqr;

		public BotRangedAttackGoal(Entity rangedAttackMob, double speedModifier, int attackIntervalMin, float attackRadius) {
			this(rangedAttackMob, speedModifier, attackIntervalMin, attackIntervalMin, attackRadius);
		}

		public BotRangedAttackGoal(Entity rangedAttackMob, double speedModifier, int attackIntervalMin, int attackIntervalMax, float attackRadius) {
			if (!(rangedAttackMob instanceof LivingEntity)) {
				throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
			} else {
				this.rangedAttackMob = rangedAttackMob;
				this.mob = (Mob) rangedAttackMob;
				this.speedModifier = speedModifier;
				this.attackIntervalMin = attackIntervalMin;
				this.attackIntervalMax = attackIntervalMax;
				this.attackRadius = attackRadius;
				this.attackRadiusSqr = attackRadius * attackRadius;
				this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
			}
		}

		public boolean canUse() {
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity != null && livingentity.isAlive()) {
				this.target = livingentity;
				return true;
			} else {
				return false;
			}
		}

		public boolean canContinueToUse() {
			return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
		}

		public void stop() {
			this.target = null;
			this.seeTime = 0;
			this.attackTime = -1;
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			double distanceToTarget = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
			boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(this.target);
			if (hasLineOfSight) {
				++this.seeTime;
			} else {
				this.seeTime = 0;
			}
			if (!(distanceToTarget > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
				this.mob.getNavigation().stop();
			} else {
				this.mob.getNavigation().moveTo(this.target, this.speedModifier);
			}
			this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
			if (--this.attackTime == 0) {
				if (!hasLineOfSight) {
					return;
				}
				float f = (float) Math.sqrt(distanceToTarget) / this.attackRadius;
				float velocity = Mth.clamp(f, 0.1F, 1.0F);

				Pig pig = new Pig(EntityType.PIG, this.rangedAttackMob.level());
				pig.setPos(this.rangedAttackMob.position());
				this.rangedAttackMob.level().addFreshEntity(pig);
				shootFromRotation(this.rangedAttackMob, pig, this.rangedAttackMob.getXRot(), this.rangedAttackMob.getYRot(), 0.0f, 5, 0);

				this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
			} else if (this.attackTime < 0) {
				this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(distanceToTarget) / (double) this.attackRadius, (double) this.attackIntervalMin, (double) this.attackIntervalMax));
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
