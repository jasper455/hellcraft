package net.team.helldivers.item.custom.backpacks;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.client.renderer.item.JumpPackRenderer;
import net.team.helldivers.damage.ModDamageSources;
import net.team.helldivers.damage.ModDamageTypes;

public class JumpPackItem extends AbstractBackpackItem {
    public JumpPackItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUse(Player player) {
        Pig pig = new Pig(EntityType.PIG, player.level());
        pig.setPos(player.position());
        player.level().addFreshEntity(pig);
        shootFromRotation(player, pig, player.getXRot(), player.getYRot(), 0.0f, 3, 15);
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new JumpPackRenderer();
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
