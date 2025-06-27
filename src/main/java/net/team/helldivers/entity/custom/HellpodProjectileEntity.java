package net.team.helldivers.entity.custom;

import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.entity.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;

import static net.team.helldivers.block.custom.HellbombBlock.FACING;

public class HellpodProjectileEntity extends AbstractArrow {
    public Vec2 groundedOffset;
    private int groundedTicks = 0;

    public HellpodProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HellpodProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.HELLPOD.get(), shooter, level);
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide && !this.isGrounded()) {
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setScaleData(GenericParticleData.create(3f, 0f).build())
                        .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                        .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39, 200)).build())
                        .addMotion(Mth.randomBetween(RandomSource.create(), -1, 1),
                                this.getDeltaMovement().y * -1,
                                Mth.randomBetween(RandomSource.create(), -1, 1))
                        .setLifetime(20)
                        .setForceSpawn(true)
                        .spawn(this.level(), this.getX(), this.getY() - 3.5, this.getZ());
        }
        if (this.isGrounded()) {
            groundedTicks++;
        }

        if (groundedTicks == 60 && !this.level().isClientSide) {
            BlockState state = this.level().getBlockState(this.blockPosition());
            this.level().setBlockAndUpdate(this.blockPosition(), ModBlocks.HELLBOMB.get().defaultBlockState().setValue(FACING, Direction.SOUTH));
            this.discard();
        }
        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return Items.AIR.getDefaultInstance();
    }
}
