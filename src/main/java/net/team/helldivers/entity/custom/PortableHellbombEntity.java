package net.team.helldivers.entity.custom;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.team.helldivers.block.custom.BotContactMineBlock;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SExplosionPacket;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.worldgen.dimension.ModDimensions;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PortableHellbombEntity extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Vec3 groundedOffset;
    private float rotation;

    public PortableHellbombEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PortableHellbombEntity(LivingEntity shooter, Level level) {
        super(ModEntities.PORTABLE_HELLBOMB.get(), shooter, level);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(pResult.getDirection() == Direction.SOUTH) {
            groundedOffset = new Vec3(180f, 180f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.NORTH) {
            groundedOffset = new Vec3(180f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.EAST) {
            groundedOffset = new Vec3(180f, -90f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.WEST) {
            groundedOffset = new Vec3(180f, 90f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }

        if(pResult.getDirection() == Direction.DOWN) {
            groundedOffset = new Vec3(0f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
        if(pResult.getDirection() == Direction.UP) {
            groundedOffset = new Vec3(270f, 0f, Mth.randomBetween(RandomSource.create(), 0f, 360f));
        }
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {}

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.SUSPICIOUS_GRAVEL_STEP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}