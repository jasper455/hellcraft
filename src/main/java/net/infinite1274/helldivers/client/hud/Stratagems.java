package net.infinite1274.helldivers.client.hud;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.infinite1274.helldivers.HelldiversMod;
import net.infinite1274.helldivers.item.ModItems;
import net.infinite1274.helldivers.network.PacketHandler;
import net.infinite1274.helldivers.network.SGiveStratagemOrbPacket;
import net.infinite1274.helldivers.sound.ModSounds;
import net.infinite1274.helldivers.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Stratagems {
    private static boolean allInputsDown = false;


    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || event.phase != TickEvent.Phase.END) return;

        StratagemHudOverlay.wasUpPressedThisTick = KeyBinding.UP_INPUT_KEY.consumeClick();
        boolean upJustPressed = StratagemHudOverlay.wasUpPressedThisTick && !StratagemHudOverlay.wasUpPressedLastTick;

        StratagemHudOverlay.wasDownPressedThisTick = KeyBinding.DOWN_INPUT_KEY.consumeClick();
        boolean downJustPressed = StratagemHudOverlay.wasDownPressedThisTick && !StratagemHudOverlay.wasDownPressedLastTick;

        StratagemHudOverlay.wasLeftPressedThisTick = KeyBinding.LEFT_INPUT_KEY.consumeClick();
        boolean leftJustPressed = StratagemHudOverlay.wasLeftPressedThisTick && !StratagemHudOverlay.wasLeftPressedLastTick;

        StratagemHudOverlay.wasRightPressedThisTick = KeyBinding.RIGHT_INPUT_KEY.consumeClick();
        boolean rightJustPressed = StratagemHudOverlay.wasRightPressedThisTick && !StratagemHudOverlay.wasRightPressedLastTick;

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.HELLDIVER_CHESTPLATE.get()) {

            // Hellbomb inputs

            switch (HellbombHud.inputStep) {
                case 0 -> {
                    if (downJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.firstInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 1 -> {
                    if (upJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.secondInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 2 -> {
                    if (leftJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.thirdInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 3 -> {
                    if (downJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.fourthInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 4 -> {
                    if (upJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.fifthInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 5 -> {
                    if (rightJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.sixthInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 6 -> {
                    if (downJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        HellbombHud.seventhInputDown = true;
                        HellbombHud.inputStep++;
                    }
                }
                case 7 -> {
                    if (upJustPressed) {
                        player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                        player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 1f, 1f);
                        HellbombHud.eighthInputDown = true;
                        HellbombHud.allInputsDown = true;
                        HellbombHud.inputStep++;
                    }
                }
            }

            // Precision Strike inputs

            switch (PrecisionStrikeHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                            PrecisionStrikeHud.firstInputDown = true;
                            PrecisionStrikeHud.inputStep++;
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                            PrecisionStrikeHud.secondInputDown = true;
                            PrecisionStrikeHud.inputStep++;
                        }
                    }
                    case 2 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 1f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 1f, 1f);
                            PrecisionStrikeHud.thirdInputDown = true;
                            PrecisionStrikeHud.allInputsDown = true;
                            PrecisionStrikeHud.inputStep++;
                        }
                    }
            }

        } else {
            resetInputValues();
        }
        if (HellbombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Hellbomb"));
            resetInputValues();

        }
        if (PrecisionStrikeHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Precision Strike"));
            resetInputValues();
        }
    }



    @SubscribeEvent
    public static void registerGuiOverlays(CustomizeGuiOverlayEvent event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.HELLDIVER_CHESTPLATE.get()) {

            guiGraphics.blit(StratagemHudOverlay.STRATAGEM_BACKGROUND,
                    6, 6, 125 , 125, 0, 0, 16, 16,
                    16, 16);

            HellbombHud.renderHellbombHud(guiGraphics);

            PrecisionStrikeHud.renderPrecisionStrikeHud(guiGraphics);

        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PoseStack poseStack = event.getPoseStack();

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && Minecraft.getInstance().player.getDeltaMovement().x == 0
                && Minecraft.getInstance().player.getDeltaMovement().z == 0 && !allInputsDown &&
                Minecraft.getInstance().player.getMainHandItem().isEmpty()) {
            event.getInput().forwardImpulse = 0;
            event.getInput().leftImpulse = 0;
            event.getInput().jumping = false;
            event.getInput().up = false;
            event.getInput().down = false;
            event.getInput().left = false;
            event.getInput().right = false;
        }
    }

    private static void resetInputValues() {
        HellbombHud.resetInputValues();
        PrecisionStrikeHud.resetInputValues();
        allInputsDown = false;
    }

}
