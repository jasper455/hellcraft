package net.team.helldivers.client.hud;


import net.team.helldivers.HelldiversMod;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.IHelldiverArmorItem;
import net.team.helldivers.item.custom.StratagemPickerItem;
import net.team.helldivers.item.inventory.StratagemPickerInventory;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SGiveStratagemOrbPacket;
import net.team.helldivers.network.SStratagemGiveCooldownPacket;
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

@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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

        StratagemHudOverlay.wasUpNotPressedThisTick = KeyBinding.DOWN_INPUT_KEY.consumeClick()
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
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem && getPickerInventory(player) != null) {

            player.playSound(ModSounds.STRATAGEM_MENU_OPEN.get(), 1f, 1f);
            hasPlayedOpenSound = true;
            hasPlayedCloseSound = false;
            hasPlayedOpenAnim = true;  // This triggers the opening animation
            hasPlayedCloseAnim = false;
        }
        if (!KeyBinding.SHOW_STRATAGEM_KEY.isDown() && !hasPlayedCloseSound && hasPlayedOpenSound && player.getDeltaMovement().x == 0
                && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem && getPickerInventory(player) != null) {

            player.playSound(ModSounds.STRATAGEM_MENU_CLOSE.get(), 1f, 1f);
            hasPlayedOpenSound = false;
            hasPlayedCloseSound = true;
            hasPlayedOpenAnim = false;
            hasPlayedCloseAnim = true;  // This triggers the closing animation
        }

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem && getPickerInventory(player) != null) {

            // Hellbomb inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
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

            // Resupply inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance())) {
                switch (ResupplyHud.inputStep) {
                    case 0 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ResupplyHud.firstInputDown = true;
                            ResupplyHud.inputStep++;
                        } else if (downNotPressed) {
                            ResupplyHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ResupplyHud.secondInputDown = true;
                            ResupplyHud.inputStep++;
                        } else if (downNotPressed) {
                            ResupplyHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ResupplyHud.thirdInputDown = true;
                            ResupplyHud.inputStep++;
                        } else if (upNotPressed) {
                            ResupplyHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            ResupplyHud.fourthInputDown = true;
                            ResupplyHud.inputStep++;
                            ResupplyHud.allInputsDown = true;
                        } else if (rightNotPressed) {
                            ResupplyHud.resetInputValues();
                        }
                    }
                }
            }

            // Expendable Anti-Tank inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())) {
                switch (EAT17Hud.inputStep) {
                    case 0 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EAT17Hud.firstInputDown = true;
                            EAT17Hud.inputStep++;
                        } else if (downNotPressed) {
                            EAT17Hud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EAT17Hud.secondInputDown = true;
                            EAT17Hud.inputStep++;
                        } else if (downNotPressed) {
                            EAT17Hud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EAT17Hud.thirdInputDown = true;
                            EAT17Hud.inputStep++;
                        } else if (leftNotPressed) {
                            EAT17Hud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EAT17Hud.fourthInputDown = true;
                            EAT17Hud.inputStep++;
                        } else if (upNotPressed) {
                            EAT17Hud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            EAT17Hud.fifthInputDown = true;
                            EAT17Hud.allInputsDown = true;
                            EAT17Hud.inputStep++;
                        } else if (rightNotPressed) {
                            EAT17Hud.resetInputValues();
                        }
                    }
                }
            }

            // Precision Strike inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
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

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
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

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
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

            // Orbital Laser inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance())) {
                switch (OrbitalLaserHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            OrbitalLaserHud.firstInputDown = true;
                            OrbitalLaserHud.inputStep++;
                        } else if (rightNotPressed) {
                            OrbitalLaserHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            OrbitalLaserHud.secondInputDown = true;
                            OrbitalLaserHud.inputStep++;
                        } else if (downNotPressed) {
                            OrbitalLaserHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            OrbitalLaserHud.thirdInputDown = true;
                            OrbitalLaserHud.inputStep++;
                        } else if (upNotPressed) {
                            OrbitalLaserHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            OrbitalLaserHud.fourthInputDown = true;
                            OrbitalLaserHud.inputStep++;
                        } else if (rightNotPressed) {
                            OrbitalLaserHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            OrbitalLaserHud.fifthInputDown = true;
                            OrbitalLaserHud.allInputsDown = true;
                            OrbitalLaserHud.inputStep++;
                        } else if (downNotPressed) {
                            OrbitalLaserHud.resetInputValues();
                        }
                    }
                }
            }

            // 500KG Bomb inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())) {
                switch (Eagle500KgBombHud.inputStep) {
                    case 0 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            Eagle500KgBombHud.firstInputDown = true;
                            Eagle500KgBombHud.inputStep++;
                        } else if (upNotPressed) {
                            Eagle500KgBombHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            Eagle500KgBombHud.secondInputDown = true;
                            Eagle500KgBombHud.inputStep++;
                        } else if (rightNotPressed) {
                            Eagle500KgBombHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            Eagle500KgBombHud.thirdInputDown = true;
                            Eagle500KgBombHud.inputStep++;
                        } else if (downNotPressed) {
                            Eagle500KgBombHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            Eagle500KgBombHud.fourthInputDown = true;
                            Eagle500KgBombHud.inputStep++;
                        } else if (downNotPressed) {
                            Eagle500KgBombHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            Eagle500KgBombHud.fifthInputDown = true;
                            Eagle500KgBombHud.allInputsDown = true;
                            Eagle500KgBombHud.inputStep++;
                        } else if (downNotPressed) {
                            Eagle500KgBombHud.resetInputValues();
                        }
                    }
                }
            }

            // Cluster Bomb inputs

            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance())) {
                switch (ClusterBombHud.inputStep) {
                    case 0 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ClusterBombHud.firstInputDown = true;
                            ClusterBombHud.inputStep++;
                        } else if (upNotPressed) {
                            ClusterBombHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ClusterBombHud.secondInputDown = true;
                            ClusterBombHud.inputStep++;
                        } else if (rightNotPressed) {
                            ClusterBombHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ClusterBombHud.thirdInputDown = true;
                            ClusterBombHud.inputStep++;
                        } else if (downNotPressed) {
                            ClusterBombHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            ClusterBombHud.fourthInputDown = true;
                            ClusterBombHud.inputStep++;
                        } else if (downNotPressed) {
                            ClusterBombHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            ClusterBombHud.fifthInputDown = true;
                            ClusterBombHud.allInputsDown = true;
                            ClusterBombHud.inputStep++;
                        } else if (rightNotPressed) {
                            ClusterBombHud.resetInputValues();
                        }
                    }
                }
            }


        } else {
            resetInputValues();
        }

        if (HellbombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Hellbomb"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())), 9999));
            resetInputValues();
        }
        if (ResupplyHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Resupply"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.RESUPPLY.get().getDefaultInstance())), 3600));
            resetInputValues();
        }
        if (EAT17Hud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Expendable Anti-Tank"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())), 1400));
            resetInputValues();
        }
        if (PrecisionStrikeHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Precision Strike"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.PRECISION_STRIKE.get().getDefaultInstance())), 1800));
            resetInputValues();
        }
        if (SmallBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital 120MM HE Barrage"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.SMALL_BARRAGE.get().getDefaultInstance())), 3600));
            resetInputValues();
        }
        if (BigBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital 380MM HE Barrage"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.BIG_BARRAGE.get().getDefaultInstance())), 4800));
            resetInputValues();
        }
        if (OrbitalLaserHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Laser"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.ORBITAL_LASER.get().getDefaultInstance())), 6000));
            resetInputValues();
        }
        if (Eagle500KgBombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Eagle 500KG Bomb"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())), 3000));
            resetInputValues();
        }
        if (ClusterBombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Eagle Cluster Bomb"));
            PacketHandler.sendToServer(new SStratagemGiveCooldownPacket(getPickerInventory(player)
                    .getItem(getPickerInventory(player).getSlotWithItem(ModItems.CLUSTER_BOMB.get().getDefaultInstance())), 3000));
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

        // only render the menu if your not moving, all inputs aren't down, your hand is empty, you have a helldiver chestplate on,
        // and the stratagem picker inventory exists
        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem && getPickerInventory(player) != null) {

            // render the background
            guiGraphics.blit(StratagemHudOverlay.STRATAGEM_BACKGROUND,
                    6, 6, 132 , 125, 0, 0, 16, 16,
                    16, 16);

            // OTHER

            // Hellbomb Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                HellbombHud.renderHellbombHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                HellbombHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            }

            // SUPPORT

            // Resupply Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance())) {
                ResupplyHud.renderResupplyHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.RESUPPLY.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance())) {
                ResupplyHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.RESUPPLY.get().getDefaultInstance()));
            }

            // EAT Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())) {
                EAT17Hud.renderEAT17Hud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())) {
                EAT17Hud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()));
            }

            // ORBITAL

            // Precision Strike HUD Render Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderPrecisionStrikeHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            }

            // 120 Barrage Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                SmallBarrageHud.renderSmallBarrageHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                SmallBarrageHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            }

            // 380 Barrage Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                BigBarrageHud.renderBigBarrageHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                BigBarrageHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            }

            // Orbital Laser Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance())) {
                OrbitalLaserHud.renderOrbitalLaserHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.ORBITAL_LASER.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance())) {
                OrbitalLaserHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.ORBITAL_LASER.get().getDefaultInstance()));
            }

            // EAGLES

            // 500 KG Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())) {
                Eagle500KgBombHud.render500KgBombHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())) {
                Eagle500KgBombHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()));
            }

            // Cluster Bomb Render HUD Code
            if (getPickerInventory(player) != null && getPickerInventory(player).contains(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    getPickerInventory(player).isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance())) {
                ClusterBombHud.renderClusterBombHud(guiGraphics, getPickerInventory(player).getSlotWithItem(ModItems.CLUSTER_BOMB.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (!getPickerInventory(player).isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance())) {
                ClusterBombHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.CLUSTER_BOMB.get().getDefaultInstance()));
            }
        }

        // render the popups in the top left if the stratagem is almost done cooling down
        if (!allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem && getPickerInventory(player) != null) {
            // OTHER

            // Hellbomb Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) <= 5) {
                HellbombHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            }

            // SUPPORT

            // Resupply Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.RESUPPLY.get().getDefaultInstance()) <= 5) {
                ResupplyHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.RESUPPLY.get().getDefaultInstance()));
            }

            // EAT Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) <= 5) {
                EAT17Hud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()));
            }

            // ORBITAL

            if (!getPickerInventory(player).isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) <= 5) {
                PrecisionStrikeHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            }

            // 120 Barrage Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) <= 5) {
                SmallBarrageHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            }

            // 380 Barrage Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.BIG_BARRAGE.get().getDefaultInstance()) <= 5) {
                BigBarrageHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            }

            // Orbital Laser Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.ORBITAL_LASER.get().getDefaultInstance()) <= 5) {
                OrbitalLaserHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.ORBITAL_LASER.get().getDefaultInstance()));
            }

            // EAGLES

            // 500 KG Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) <= 5) {
                Eagle500KgBombHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()));
            }

            // Cluster Bomb Cooldown Complete Popup Code
            if (!getPickerInventory(player).isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    getPickerInventory(player).getCooldownLeft(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) <= 5) {
                ClusterBombHud.renderCooldownHud(guiGraphics, getPickerInventory(player).getCooldownLeft(ModItems.CLUSTER_BOMB.get().getDefaultInstance()));
            }
        }
    }

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        // Stop the player from moving when the stratagem menu is open
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
        Eagle500KgBombHud.resetInputValues();
        EAT17Hud.resetInputValues();
        OrbitalLaserHud.resetInputValues();
        ResupplyHud.resetInputValues();
        ClusterBombHud.resetInputValues();
        allInputsDown = false;
    }

    // get the stratagem picker item's inventory
    public static StratagemPickerInventory getPickerInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof StratagemPickerItem) {
                return new StratagemPickerInventory(stack, StratagemPickerInventory.INVENTORY_SIZE);
            }
        }
        return null;
    }
}