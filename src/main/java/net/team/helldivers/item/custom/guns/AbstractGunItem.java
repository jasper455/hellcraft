package net.team.helldivers.item.custom.guns;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.team.helldivers.block.custom.AmmoCrateBlock;
import net.team.helldivers.client.renderer.item.P2Renderer;
import net.team.helldivers.client.renderer.item.Sg225Renderer;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SGunReloadPacket;
import net.team.helldivers.network.SShootPacket;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractGunItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";
    private boolean isShooting = false;
    private boolean isReloading = false;
    private boolean hasStartedReload = false; // Add this flag
    private boolean isAiming = false;
    private boolean wasAiming = false; // Track previous aiming state
    private int shootCooldown = 0;
    public int durability;
    public boolean reloadable;
    public String type;
    public int fireDelay;
    public BlockEntityWithoutLevelRenderer renderer;


    public AbstractGunItem(Properties properties, boolean reloadable, String type, int fireDelay, BlockEntityWithoutLevelRenderer renderer) {
        super(properties);
        this.type = type;
        this.reloadable = reloadable;
        this.renderer = renderer;
        this.fireDelay = fireDelay;
    }

    private boolean canShoot(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

            // Changing the players arm pose when holding the item
            private static final HumanoidModel.ArmPose P2PeacemakerPose = HumanoidModel.ArmPose.create("P2Peacemaker", false, (model, entity, arm) -> {
                if (arm == HumanoidArm.LEFT) {
                } else {
                    model.rightArm.xRot = 30F + model.head.xRot;
                    model.rightArm.yRot = 0F + model.head.yRot;
                    model.leftArm.xRot = 30F + model.head.xRot;
                    model.leftArm.yRot = 0.5F + model.head.yRot;
                }
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty()) {
                    if (entityLiving.getUsedItemHand() == hand) {
                        return P2PeacemakerPose;
                    }
                }
                return HumanoidModel.ArmPose.EMPTY;
            }

            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                int i = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate(i * 0.56F, -0.52F, -0.72F);
                if (player.getUseItem() == itemInHand) {
                    poseStack.translate(0.05, 0.05, 0.05);
                }
                return true;
            }
        });
    }

    // Animations
    private PlayState idlePredicate(AnimationState event) {
        if (!animationprocedure.equals("empty")) return PlayState.STOP;

        // 1. Reload always overrides everything else
        if (isReloading) {
            if (!hasStartedReload) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("reload"));
                hasStartedReload = true;
            }
            return PlayState.CONTINUE;
        }

        // 2. Shooting always overrides aim transitions
        if (isShooting && shootCooldown == 0) {
            if (canShoot(Minecraft.getInstance().player.getMainHandItem())) {
                if (isAiming) {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("shoot_aim").thenPlay("aim"));
                } else {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("shoot"));
                }
            }
            PacketHandler.sendToServer(new SShootPacket());
            shootCooldown = fireDelay;
            return PlayState.CONTINUE;
        }

        // 3. Aim transitions (only when not shooting)
        if (!isShooting) {
            if (isAiming && !wasAiming) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("aim"));
                wasAiming = true;
                return PlayState.CONTINUE;
            }

            if (!isAiming && wasAiming) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("stop_aim"));
                wasAiming = false;
                return PlayState.CONTINUE;
            }
        }

        // 4. Idle fallback
        if (!isAiming && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }




    String prevAnim = "empty";

    // Ignore this, I don't even know what it does lowk
    private PlayState procedurePredicate(AnimationState event) {
        if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
            if (!this.animationprocedure.equals(prevAnim))
                event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationprocedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (this.animationprocedure.equals("empty")) {
            prevAnim = "empty";
            return PlayState.STOP;
        }
        prevAnim = this.animationprocedure;
        return PlayState.CONTINUE;
    }

    // Register the animation controllers
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController procedureController = new AnimationController(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        AnimationController idleController = new AnimationController(this, "idleController", 0, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        if(reloadable) {
            list.add(Component.literal("[Reloadable]"));
        }
        else{
            list.add(Component.literal("[Not Reloadable]"));
        }
        list.add(Component.literal(type));
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        if (world.isClientSide() && entity instanceof Player player) {
            if (selected) {
                isShooting = KeyBinding.SHOOT.isDown();

                if (shootCooldown > 0) {
                    shootCooldown--;
                }

                // Handle reload
                if (KeyBinding.RELOAD.consumeClick()) {
                    for (ItemStack stack : player.getInventory().items) {
                        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AmmoCrateBlock) {
                            isReloading = true;
                            hasStartedReload = false; // Reset the flag when starting a new reload
                            // Reset aiming states when reloading
                            isAiming = false;
                            wasAiming = false;
                            PacketHandler.sendToServer(new SGunReloadPacket()); // Send packet only once
                            break;
                        }
                    }
                }

                // Reset reload state when animation is done
                if (isReloading && hasStartedReload) {
                    isReloading = false;
                    hasStartedReload = false;
                }

                // Handle aiming only if not reloading
                if (!isReloading) {
                    boolean newAimingState = KeyBinding.AIM.isDown();
                    if (newAimingState != isAiming) {
                        wasAiming = isAiming;
                        isAiming = newAimingState;
                    }
                }
            } else {
                isShooting = false;
                isReloading = false;
                hasStartedReload = false;
                isAiming = false;
                wasAiming = false;
                shootCooldown = 0;
            }
        }
        super.inventoryTick(itemstack, world, entity, slot, selected);
    }


    // Get Rid of the vanilla punch animation
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }
}