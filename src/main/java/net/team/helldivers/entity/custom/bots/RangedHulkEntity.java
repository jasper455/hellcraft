package net.team.helldivers.entity.custom.bots;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class RangedHulkEntity extends AbstractBotEntity {
    public RangedHulkEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, true, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 25);
    }

}
