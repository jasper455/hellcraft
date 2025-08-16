package net.team.helldivers.item.custom.guns;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.custom.AmmoCrateBlock;
import net.team.helldivers.network.CApplyRecoilPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SGunReloadPacket;
import net.team.helldivers.network.SShootPacket;
import net.team.helldivers.network.SStartShootPacket;
import net.team.helldivers.network.SStopShootPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.util.ShootHelper;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
    public boolean firstShot;
    public boolean isAuto;
    public float dam;
    public double drift;
    public float recoil;
    private RegistryObject<SoundEvent> reloadSound;
    private  RegistryObject<SoundEvent> shootSound;


    public AbstractGunItem(Properties properties, float recoil, boolean isAuto, boolean reloadable, String type, int fireDelay, float dam, double drift, RegistryObject<SoundEvent> shootSound, RegistryObject<SoundEvent> reloadSound) {
        super(properties);
        this.type = type;
        this.reloadable = reloadable;
        this.fireDelay = fireDelay;
        this.isAuto = isAuto;
        this.dam = dam;
        this.drift = drift;
        this.shootSound = shootSound;
        this.reloadSound = reloadSound;
        this.recoil = recoil;
    }
    public AbstractGunItem(Properties properties, boolean isAuto, boolean reloadable, String type, int fireDelay, RegistryObject<SoundEvent> reloadSound) {
        super(properties);
        this.type = type;
        this.reloadable = reloadable;
        this.fireDelay = fireDelay;
        this.isAuto = isAuto;
        this.reloadSound = reloadSound;
        drift = -1;
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
                return createRenderer();
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

   private PlayState idlePredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            // Handle reloading
            if (isReloading && !hasStartedReload) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("reload"));
                hasStartedReload = true;
                return PlayState.CONTINUE;
            }

            // Handle shooting with proper aim state
            if (isShooting && shootCooldown == 0 && canShoot(Minecraft.getInstance().player.getMainHandItem()) && !isReloading) {
                if(!(!isAuto && !firstShot)){
                    if (isAiming) {
                    event.getController().setAnimation(RawAnimation.begin().thenPlay("shoot_aim").thenPlay("aim"));
                    } else {
                        event.getController().setAnimation(RawAnimation.begin().thenPlay("shoot"));
                    }
                    //PacketHandler.sendToServer(new SShootPacket());
                    return PlayState.CONTINUE;
                }
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
                firstShot = KeyBinding.SHOOT.consumeClick();
                if (isShooting && shootCooldown == 0 && canShoot(Minecraft.getInstance().player.getMainHandItem()) && !isReloading && isAuto) {
                    PacketHandler.sendToServer(new SShootPacket());
                     shootCooldown = fireDelay;
                }
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
                            player.level().playSound(null, player.blockPosition(), reloadSound.get(), SoundSource.PLAYERS, 10.0f, 1.0f);
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

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return true;
    }

    public void onStartShoot(ItemStack itemStack, ServerPlayer player){};
    public void onEndShoot(ItemStack itemStack, ServerPlayer player){};
    public void onShoot(ItemStack itemStack, ServerPlayer player){
       shoot(itemStack, player);
    }
    public void shoot(ItemStack itemStack, ServerPlayer player){
         if (!player.getCooldowns().isOnCooldown(itemStack.getItem()) && drift != -1) {
            if (itemStack.getDamageValue() < itemStack.getMaxDamage() - 5) {

                // Play sound
                player.level().playSound(null, player.blockPosition(),
                        shootSound.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                PacketHandler.sendToPlayer(new CApplyRecoilPacket(recoil), player);
                ShootHelper.shoot(player, player.level(), drift, dam, 0.3f, true);
                player.getCooldowns().addCooldown(itemStack.getItem(), fireDelay);

                // Damage the item
                if (!player.getAbilities().instabuild) {
                    itemStack.hurt(1, player.getRandom(), player);
                }
                player.getCooldowns().addCooldown(itemStack.getItem(), fireDelay);
            } else {
                player.level().playSound(null, player.blockPosition(),
                        ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
                player.getCooldowns().addCooldown(itemStack.getItem(), 10);
            }
        }
    }

    /**
     * Client-only: Creates the renderer for this item.
     * This should never be called on the server or during datagen.
     */
    @OnlyIn(Dist.CLIENT)
    public abstract BlockEntityWithoutLevelRenderer createRenderer();
}