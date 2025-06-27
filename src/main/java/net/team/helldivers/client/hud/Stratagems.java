package net.team.helldivers.client.hud;


import net.team.helldivers.HellcraftMod;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.StratagemPickerItem;
import net.team.helldivers.item.inventory.StratagemPickerInventory;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SGiveStratagemOrbPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HellcraftMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Stratagems {
    private static boolean allInputsDown = false;
    private static boolean hasPlayedOpenSound = false;
    private static boolean hasPlayedCloseSound = false;
    public static boolean hasPlayedOpenAnim = false;
    public static boolean hasPlayedCloseAnim = false;


    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || event.phase != TickEvent.Phase.END) return;

        //Checking if inputs were pressed

        StratagemHudOverlay.wasUpPressedThisTick = KeyBinding.UP_INPUT_KEY.consumeClick();
        boolean upJustPressed = StratagemHudOverlay.wasUpPressedThisTick && !StratagemHudOverlay.wasUpPressedLastTick;

        StratagemHudOverlay.wasDownPressedThisTick = KeyBinding.DOWN_INPUT_KEY.consumeClick();
        boolean downJustPressed = StratagemHudOverlay.wasDownPressedThisTick && !StratagemHudOverlay.wasDownPressedLastTick;

        StratagemHudOverlay.wasLeftPressedThisTick = KeyBinding.LEFT_INPUT_KEY.consumeClick();
        boolean leftJustPressed = StratagemHudOverlay.wasLeftPressedThisTick && !StratagemHudOverlay.wasLeftPressedLastTick;

        StratagemHudOverlay.wasRightPressedThisTick = KeyBinding.RIGHT_INPUT_KEY.consumeClick();
        boolean rightJustPressed = StratagemHudOverlay.wasRightPressedThisTick && !StratagemHudOverlay.wasRightPressedLastTick;

        //Checking if inputs weren't pressed

        StratagemHudOverlay.wasUpNotPressedLastTick = KeyBinding.DOWN_INPUT_KEY.consumeClick()
                || KeyBinding.LEFT_INPUT_KEY.consumeClick()
                || KeyBinding.RIGHT_INPUT_KEY.consumeClick();
        boolean upNotPressed = downJustPressed || leftJustPressed || rightJustPressed;

        StratagemHudOverlay.wasDownNotPressedThisTick = KeyBinding.UP_INPUT_KEY.consumeClick()
                || KeyBinding.LEFT_INPUT_KEY.consumeClick()
                || KeyBinding.RIGHT_INPUT_KEY.consumeClick();
        boolean downNotPressed = upJustPressed || leftJustPressed || rightJustPressed;

        StratagemHudOverlay.wasLeftNotPressedThisTick = KeyBinding.UP_INPUT_KEY.consumeClick()
                || KeyBinding.DOWN_INPUT_KEY.consumeClick()
                || KeyBinding.RIGHT_INPUT_KEY.consumeClick();
        boolean leftNotPressed = upJustPressed || downJustPressed || rightJustPressed;

        StratagemHudOverlay.wasRightNotPressedThisTick = KeyBinding.UP_INPUT_KEY.consumeClick()
                || KeyBinding.DOWN_INPUT_KEY.consumeClick()
                || KeyBinding.LEFT_INPUT_KEY.consumeClick();
        boolean rightNotPressed = upJustPressed || downJustPressed || leftJustPressed;

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && !hasPlayedOpenSound && player.getDeltaMovement().x == 0
                && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.HELLDIVER_CHESTPLATE.get() && getPickerInventory(player) != null) {

            player.playSound(ModSounds.STRATAGEM_MENU_OPEN.get(), 1f, 1f);
            hasPlayedOpenSound = true;
            hasPlayedCloseSound = false;
            hasPlayedOpenAnim = true;  // This triggers the opening animation
            hasPlayedCloseAnim = false;
        }
        if (!KeyBinding.SHOW_STRATAGEM_KEY.isDown() && !hasPlayedCloseSound && hasPlayedOpenSound && player.getDeltaMovement().x == 0
                && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.HELLDIVER_CHESTPLATE.get() && getPickerInventory(player) != null) {

            player.playSound(ModSounds.STRATAGEM_MENU_CLOSE.get(), 1f, 1f);
            hasPlayedOpenSound = false;
            hasPlayedCloseSound = true;
            hasPlayedOpenAnim = false;
            hasPlayedCloseAnim = true;  // This triggers the closing animation
        }

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.HELLDIVER_CHESTPLATE.get() && getPickerInventory(player) != null) {

            // Hellbomb inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                switch (HellbombHud.inputStep) {
                    case 0 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.firstInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (downNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.secondInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (upNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.thirdInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (leftNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.fourthInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (downNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.fifthInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (upNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 5 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.sixthInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (rightNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 6 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            HellbombHud.seventhInputDown = true;
                            HellbombHud.inputStep++;
                        } else if (downNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                    case 7 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            HellbombHud.eighthInputDown = true;
                            HellbombHud.allInputsDown = true;
                            HellbombHud.inputStep++;
                        } else if (upNotPressed) {
                            HellbombHud.resetInputValues();
                        }
                    }
                }
            }

            // Precision Strike inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                switch (PrecisionStrikeHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            PrecisionStrikeHud.firstInputDown = true;
                            PrecisionStrikeHud.inputStep++;
                        } else if (rightNotPressed) {
                            PrecisionStrikeHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            PrecisionStrikeHud.secondInputDown = true;
                            PrecisionStrikeHud.inputStep++;
                        } else if (rightNotPressed) {
                            PrecisionStrikeHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            PrecisionStrikeHud.thirdInputDown = true;
                            PrecisionStrikeHud.allInputsDown = true;
                            PrecisionStrikeHud.inputStep++;
                        } else if (upNotPressed) {
                            PrecisionStrikeHud.resetInputValues();
                        }
                    }
                }
            }

            // 120MM Barrage inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                switch (SmallBarrageHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            SmallBarrageHud.firstInputDown = true;
                            SmallBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            SmallBarrageHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            SmallBarrageHud.secondInputDown = true;
                            SmallBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            SmallBarrageHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            SmallBarrageHud.thirdInputDown = true;
                            SmallBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            SmallBarrageHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            SmallBarrageHud.fourthInputDown = true;
                            SmallBarrageHud.inputStep++;
                        } else if (leftNotPressed) {
                            SmallBarrageHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            SmallBarrageHud.fifthInputDown = true;
                            SmallBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            SmallBarrageHud.resetInputValues();
                        }
                    }
                    case 5 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            SmallBarrageHud.sixthInputDown = true;
                            SmallBarrageHud.allInputsDown = true;
                            SmallBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            SmallBarrageHud.resetInputValues();
                        }
                    }
                }
            }

            // 380MM Barrage inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                switch (BigBarrageHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            BigBarrageHud.firstInputDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            BigBarrageHud.secondInputDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            BigBarrageHud.thirdInputDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (upNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            BigBarrageHud.fourthInputDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (upNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            BigBarrageHud.fifthInputDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (leftNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
                    }
                    case 5 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            BigBarrageHud.sixthInputDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
                    }
                    case 6 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            BigBarrageHud.seventhInputDown = true;
                            BigBarrageHud.allInputsDown = true;
                            BigBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            BigBarrageHud.resetInputValues();
                        }
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
        if (SmallBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital 120MM HE Barrage"));
            resetInputValues();
        }
        if (BigBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital 380MM HE Barrage"));
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
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.HELLDIVER_CHESTPLATE.get() && getPickerInventory(player) != null) {

            guiGraphics.blit(StratagemHudOverlay.STRATAGEM_BACKGROUND,
                    6, 6, 132 , 125, 0, 0, 16, 16,
                    16, 16);

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                HellbombHud.renderHellbombHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            }
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderPrecisionStrikeHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            }
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderPrecisionStrikeHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            }
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                SmallBarrageHud.renderSmallBarrageHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            }
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                BigBarrageHud.renderBigBarrageHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            }

        }
    }

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        Player player = Minecraft.getInstance().player;
        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0
                && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() && getPickerInventory(player) != null) {
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
        SmallBarrageHud.resetInputValues();
        BigBarrageHud.resetInputValues();
        allInputsDown = false;
    }

    public static StratagemPickerInventory getPickerInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) {
                if (stack.getItem() instanceof StratagemPickerItem) {
                return new StratagemPickerInventory(stack, StratagemPickerInventory.INVENTORY_SIZE);
            }
        }
        return null;
    }
}
