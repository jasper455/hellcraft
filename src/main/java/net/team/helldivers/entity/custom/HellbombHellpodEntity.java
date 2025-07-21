package net.team.helldivers.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
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
import net.minecraftforge.network.NetworkHooks;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.entity.ModEntities;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.screen.custom.HellbombEntityInputMenu;
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


public class HellbombHellpodEntity extends Entity implements GeoEntity {
    private static final EntityDataAccessor<Boolean> IS_ACTIVATED =
            SynchedEntityData.defineId(HellbombHellpodEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final int randomCode = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);

    public static final RawAnimation DEPLOY = RawAnimation.begin().thenPlayAndHold("deploy");
    public static final RawAnimation EMPTY = RawAnimation.begin().thenPlayAndHold("empty");
    public static final RawAnimation FALL = RawAnimation.begin().thenLoop("fall");
    public static final RawAnimation LAND = RawAnimation.begin().thenLoop("land");
    public static final RawAnimation ACTIVATE = RawAnimation.begin().thenPlayAndHold("hellbomb.activate");
    public static final RawAnimation ACTIVE_IDLE = RawAnimation.begin().thenLoop("hellbomb.active_idle");

    private int groundedTicks = 0;
    private int groundedTicksSinceActivation = 0;
    private int clickedTicks = 0;
    private boolean shouldStopCounting = false;
    private boolean hasBeenClicked = false;
    private boolean hasLanded = false;
    private boolean hasBeenSet = false;

    public HellbombHellpodEntity(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HellbombHellpodEntity(Level level) {
        super(ModEntities.HELLBOMB_HELLPOD.get(), level);
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
            this.setDeltaMovement(movement.x, movement.y - 0.1, movement.z);
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
        if (this.isGrounded() && isActivated()) {
            groundedTicksSinceActivation++;
        }
        if (this.isGrounded() && !hasBeenSet) {
            this.setPos(this.getX(), this.getY(), this.getZ());
            hasBeenSet = true;
        }
        if (hasBeenClicked && hasBeenSet && !shouldStopCounting) {
            clickedTicks = groundedTicks;
            shouldStopCounting = true;
        }
        if (groundedTicks >= clickedTicks + 60 && hasBeenClicked) {
            this.discard();
        }

        if (this.isGrounded() && isActivated() && groundedTicksSinceActivation >= 300) {
            this.discard();
        }

        if (!this.isGrounded()) {
            this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(1.0)).forEach(entity -> {
                entity.hurt(level().damageSources().explosion(null), 9999.0F);
            });
        }

        if (this.isGrounded() && groundedTicks <= 10 && !this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.getX(), this.getY(), this.getZ(),
                    5, 0f, 0f, 0f, 2);
        }
        super.tick();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.level().isClientSide && hand == InteractionHand.MAIN_HAND && !this.isActivated()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.literal("Hellbomb");
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                            return new HellbombEntityInputMenu(windowId, inventory, HellbombHellpodEntity.this);
                        }
                    },
                    buf -> buf.writeInt(this.getId())
            );
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }


    private PlayState animations(AnimationState event) {
        if (isGrounded() && groundedTicks >= 20 && hasBeenClicked) {
            event.getController().setAnimation(EMPTY);
            return PlayState.CONTINUE;
        }
        if (isGrounded() && isActivated()) {
            event.getController().setAnimation(ACTIVATE);
            return PlayState.CONTINUE;
        }
        if (isGrounded() && groundedTicks >= 20) {
            event.getController().setAnimation(DEPLOY);
            return PlayState.CONTINUE;
        }
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
    public boolean canBeCollidedWith() {
        return !hasBeenClicked;  // Allow the entity to be collided with, enabling interaction
    }

    @Override
    public boolean isPushable() {
        return false;  // Prevent the entity from being pushed by other entities
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {}
    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {}

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    // Add this method to handle the client-side sync

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 1) {
            hasBeenClicked = true;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public int getGroundedTicks() {
        return groundedTicks;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_ACTIVATED, false);
    }

    public boolean isActivated() {
        return this.entityData.get(IS_ACTIVATED);
    }

    public void setActivated(boolean activated) {
        this.entityData.set(IS_ACTIVATED, activated);
    }

}
