package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SHellpodDestroyBlocksPacket;
import net.team.helldivers.screen.custom.SupportHellpodMenu;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;


public class HellpodEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation FALL = RawAnimation.begin().thenLoop("fall");
    public static final RawAnimation LAND = RawAnimation.begin().thenLoop("land");

    public SimpleContainer inventory;

    private int groundedTicks = 0;
    private boolean shouldStopCounting = false;
    private boolean hasLanded = false;
    private boolean hasBeenSet = false;

    public HellpodEntity(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HellpodEntity(Level level) {
        super(ModEntities.SUPPORT_HELLPOD.get(), level);
    }

    public boolean isGrounded() {
        return hasLanded;
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.5F; // This centers the eye height
    }


    @Override
    public void tick() {
        // Check if we've landed by looking for solid blocks below
        BlockPos belowPos = this.blockPosition().below();
        boolean onSolidBlock = this.level().getBlockState(belowPos).isSolid();

        if (onSolidBlock && this.getDeltaMovement().y <= 0) {
            if (!hasLanded) {
                hasLanded = true;
                // Play landing sound if needed
                this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                this.setDeltaMovement(Vec3.ZERO);
            }
            groundedTicks++;
        }

        if (!hasLanded) {
            // Apply gravity
            Vec3 movement = this.getDeltaMovement();
            this.setDeltaMovement(movement.x, movement.y - 0.04, movement.z);
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (this.level().isClientSide && !this.isGrounded() && Minecraft.getInstance().player.getPersistentData()
                .getBoolean("helldivers.useLodestone")) {
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
        if (this.isGrounded()) {
            this.setPos(this.getX(), this.getY(), this.getZ());
            hasBeenSet = true;
        }
        if (hasBeenSet && !shouldStopCounting) {
            shouldStopCounting = true;
        }
        if (groundedTicks >= 60) {
            this.discard();
        }

        if (!this.isGrounded()) {
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(1.0)).forEach(entity -> {
                if (!(entity instanceof Player)) {
                    entity.hurt(level().damageSources().explosion(null), 9999.0F);
                }
            });
            BlockPos pos = new BlockPos((int) this.getX(), (int) (this.getY() - 5), (int) this.getZ());
            PacketHandler.sendToServer(new SHellpodDestroyBlocksPacket(pos, 2));
        }
        if (this.isGrounded() && groundedTicks <= 10 && !this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.getX(), this.getY(), this.getZ(),
                    5, 0f, 0f, 0f, 0.25f);
        }

        if (!this.level().isClientSide && this.getFirstPassenger() instanceof ServerPlayer player) {
            if (this.onGround()) {
                // Let them dismount
                player.stopRiding();
            } else {
                // Force them to stay on
                player.startRiding(this, true);
            }
        }
        super.tick();
    }

    private PlayState animations(AnimationState event) {
        if (!isGrounded()) {
            event.getController().setAnimation(FALL);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(LAND);
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController animationController = new AnimationController(this, "animationController", 0, this::animations);
        data.add(animationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {}
    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {}

    @Override
    protected void defineSynchedData() {}

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double xOffset = 0.0D;
            double yOffset = -1.0D; // Higher makes them float above the entity
            double zOffset = 0.0D;

            // Convert relative offset into world position
            Vec3 offset = new Vec3(xOffset, yOffset, zOffset).yRot(-this.getYRot() * ((float)Math.PI / 180F));
            Vec3 targetPos = this.position().add(offset);

            moveFunction.accept(passenger, targetPos.x, targetPos.y, targetPos.z);
        }
    }
}
