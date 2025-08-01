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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.team.helldivers.block.custom.AmmoCrateBlock;
import net.team.helldivers.client.renderer.item.AR23Renderer;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SShootPacket;
import net.team.helldivers.network.SGunReloadPacket;
import net.team.helldivers.util.KeyBinding;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class Ar23Item extends Item implements GeoItem, IGunItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";
    private boolean isShooting = false;
    private boolean isReloading = false;
    private boolean hasStartedReload = false; // Add this flag
    private boolean isAiming = false;
    private boolean wasAiming = false; // Track previous aiming state
    private int shootCooldown = 0;
    // Adjust this value to control fire rate (in ticks, 20 ticks = 1 second)
    private static final int SHOOT_DELAY = 2; // This will give you about 600 RPM


    public Ar23Item(Item.Properties properties) {
        super(new Item.Properties().durability(47).rarity(Rarity.COMMON));
    }

    private boolean canShoot(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new AR23Renderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

            // Changing the players arm pose when holding the item
            private static final HumanoidModel.ArmPose AR23Pose = HumanoidModel.ArmPose.create("AR23",
                    false, (model, entity, arm) -> {
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
                        return AR23Pose;
                    }
                }
                return HumanoidModel.ArmPose.EMPTY;
            }

            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
                                                   ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
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
        if (this.animationprocedure.equals("empty")) {
            // Handle reloading
            if (isReloading && !hasStartedReload) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("reload"));
                hasStartedReload = true;
                return PlayState.CONTINUE;
            }

            // Handle shooting with proper aim state
            if (isShooting && shootCooldown == 0 &&
                    canShoot(Minecraft.getInstance().player.getMainHandItem()) && !isReloading) {
                if (isAiming) {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("shoot_aim").thenPlay("aim"));
                } else {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("shoot"));
                }
                PacketHandler.sendToServer(new SShootPacket());
                shootCooldown = SHOOT_DELAY;
                return PlayState.CONTINUE;
            }
            if (isShooting && shootCooldown == 0 &&
                    !canShoot(Minecraft.getInstance().player.getMainHandItem()) && !isReloading &&
                    !Minecraft.getInstance().player.getCooldowns().isOnCooldown(this)) {
                PacketHandler.sendToServer(new SShootPacket());
                shootCooldown = SHOOT_DELAY;
                return PlayState.CONTINUE;
            }

            // Handle aiming states only if not reloading or shooting
            if (!isReloading && !isShooting) {
                if (isAiming && !wasAiming) {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("aim"));
                    wasAiming = true;
                    return PlayState.CONTINUE;
                }

                if (wasAiming && !isAiming) {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("stop_aim"));
                    wasAiming = false;
                    return PlayState.CONTINUE;
                }
            }

            // Default idle animation only if not aiming
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED && !isAiming) {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            }

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
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
        list.add(Component.literal("§e[Assault-Rifle]"));
        list.add(Component.literal("[Reloadable]"));
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