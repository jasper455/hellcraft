package net.team.helldivers.client.hud;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.helper.ClientItemCache;
import net.team.helldivers.helper.ClientJammedSync;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.armor.IHelldiverArmorItem;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SGiveStratagemOrbPacket;
import net.team.helldivers.network.SInitializeExtractionTerminalInventoryPacket;
import net.team.helldivers.network.SItemGiveCooldownPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;

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
        boolean isJammed = ClientJammedSync.getIsJammed();

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
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem) {

            player.playSound(ModSounds.STRATAGEM_MENU_OPEN.get(), 1f, 1f);
            hasPlayedOpenSound = true;
            hasPlayedCloseSound = false;
            hasPlayedOpenAnim = true;  // This triggers the opening animation
            hasPlayedCloseAnim = false;
        }
        if (!KeyBinding.SHOW_STRATAGEM_KEY.isDown() && !hasPlayedCloseSound && hasPlayedOpenSound && player.getDeltaMovement().x == 0
                && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem) {

            player.playSound(ModSounds.STRATAGEM_MENU_CLOSE.get(), 1f, 1f);
            hasPlayedOpenSound = false;
            hasPlayedCloseSound = true;
            hasPlayedOpenAnim = false;
            hasPlayedCloseAnim = true;  // This triggers the closing animation
        }

        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem) {
            PacketHandler.sendToServer(new SInitializeExtractionTerminalInventoryPacket());

            // Hellbomb inputs

            if (ClientItemCache.contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) && !isJammed) {
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

            if (ClientItemCache.contains(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance()) && !isJammed) {
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

            if (ClientItemCache.contains(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) && !isJammed) {
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

            // Stalwart inputs

            if (ClientItemCache.contains(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()) && !isJammed) {
                switch (StalwartHud.inputStep) {
                    case 0 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            StalwartHud.firstInputDown = true;
                            StalwartHud.inputStep++;
                        } else if (downNotPressed) {
                            StalwartHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            StalwartHud.secondInputDown = true;
                            StalwartHud.inputStep++;
                        } else if (leftNotPressed) {
                            StalwartHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            StalwartHud.thirdInputDown = true;
                            StalwartHud.inputStep++;
                        } else if (downNotPressed) {
                            StalwartHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            StalwartHud.fourthInputDown = true;
                            StalwartHud.inputStep++;
                        } else if (upNotPressed) {
                            StalwartHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            StalwartHud.fifthInputDown = true;
                            StalwartHud.inputStep++;
                        } else if (upNotPressed) {
                            StalwartHud.resetInputValues();
                        }
                    }
                    case 5 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            StalwartHud.fifthInputDown = true;
                            StalwartHud.allInputsDown = true;
                            StalwartHud.inputStep++;
                        } else if (leftNotPressed) {
                            StalwartHud.resetInputValues();
                        }
                    }
                }
            }

            // Anti-Materiel Rifle inputs

            if (ClientItemCache.contains(ModItems.AMR_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.AMR_STRATAGEM.get().getDefaultInstance()) && !isJammed) {
                switch (AmrHud.inputStep) {
                    case 0 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            AmrHud.firstInputDown = true;
                            AmrHud.inputStep++;
                        } else if (downNotPressed) {
                            AmrHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            AmrHud.secondInputDown = true;
                            AmrHud.inputStep++;
                        } else if (leftNotPressed) {
                            AmrHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            AmrHud.thirdInputDown = true;
                            AmrHud.inputStep++;
                        } else if (rightNotPressed) {
                            AmrHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            AmrHud.fourthInputDown = true;
                            AmrHud.inputStep++;
                        } else if (upNotPressed) {
                            AmrHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            AmrHud.fifthInputDown = true;
                            AmrHud.allInputsDown = true;
                            AmrHud.inputStep++;
                        } else if (downNotPressed) {
                            AmrHud.resetInputValues();
                        }
                    }
                }
            }

            // Precision Strike inputs

            if (ClientItemCache.contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) && !isJammed) {
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

            if (ClientItemCache.contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) && !isJammed) {
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

            if (ClientItemCache.contains(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance()) && !isJammed) {
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

            if (ClientItemCache.contains(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance()) && !isJammed) {
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

            // Napalm Barrage inputs

            if (ClientItemCache.contains(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()) && !isJammed) {
                switch (NapalmBarrageHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmBarrageHud.firstInputDown = true;
                            NapalmBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            NapalmBarrageHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmBarrageHud.secondInputDown = true;
                            NapalmBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            NapalmBarrageHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmBarrageHud.thirdInputDown = true;
                            NapalmBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            NapalmBarrageHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (leftJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmBarrageHud.fourthInputDown = true;
                            NapalmBarrageHud.inputStep++;
                        } else if (leftNotPressed) {
                            NapalmBarrageHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmBarrageHud.fifthInputDown = true;
                            NapalmBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            NapalmBarrageHud.resetInputValues();
                        }
                    }
                    case 5 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            NapalmBarrageHud.sixthInputDown = true;
                            NapalmBarrageHud.allInputsDown = true;
                            NapalmBarrageHud.inputStep++;
                        } else if (upNotPressed) {
                            NapalmBarrageHud.resetInputValues();
                        }
                    }
                }
            }

            // Walking Barrage inputs

            if (ClientItemCache.contains(ModItems.WALKING_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.WALKING_BARRAGE.get().getDefaultInstance()) && !isJammed) {
                switch (WalkingBarrageHud.inputStep) {
                    case 0 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            WalkingBarrageHud.firstInputDown = true;
                            WalkingBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            WalkingBarrageHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            WalkingBarrageHud.secondInputDown = true;
                            WalkingBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            WalkingBarrageHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            WalkingBarrageHud.thirdInputDown = true;
                            WalkingBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            WalkingBarrageHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            WalkingBarrageHud.fourthInputDown = true;
                            WalkingBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            WalkingBarrageHud.resetInputValues();
                        }
                    }
                    case 4 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            WalkingBarrageHud.fifthInputDown = true;
                            WalkingBarrageHud.inputStep++;
                        } else if (rightNotPressed) {
                            WalkingBarrageHud.resetInputValues();
                        }
                    }
                    case 5 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            WalkingBarrageHud.sixthInputDown = true;
                            WalkingBarrageHud.allInputsDown = true;
                            WalkingBarrageHud.inputStep++;
                        } else if (downNotPressed) {
                            WalkingBarrageHud.resetInputValues();
                        }
                    }
                }
            }

            // 500KG Bomb inputs

            if (ClientItemCache.contains(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) && !isJammed) {
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

            if (ClientItemCache.contains(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) && !isJammed) {
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

            // Eagle Airstrike inputs

            if (ClientItemCache.contains(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()) && !isJammed) {
                switch (EagleAirstrikeHud.inputStep) {
                    case 0 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EagleAirstrikeHud.firstInputDown = true;
                            EagleAirstrikeHud.inputStep++;
                        } else if (upNotPressed) {
                            EagleAirstrikeHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EagleAirstrikeHud.secondInputDown = true;
                            EagleAirstrikeHud.inputStep++;
                        } else if (rightNotPressed) {
                            EagleAirstrikeHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            EagleAirstrikeHud.thirdInputDown = true;
                            EagleAirstrikeHud.inputStep++;
                        } else if (downNotPressed) {
                            EagleAirstrikeHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            EagleAirstrikeHud.fourthInputDown = true;
                            EagleAirstrikeHud.allInputsDown = true;
                            EagleAirstrikeHud.inputStep++;
                        } else if (rightNotPressed) {
                            EagleAirstrikeHud.resetInputValues();
                        }
                    }
                }
            }

            // Napalm Airstrike inputs

            if (ClientItemCache.contains(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()) && !isJammed) {
                switch (NapalmAirstrikeHud.inputStep) {
                    case 0 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmAirstrikeHud.firstInputDown = true;
                            NapalmAirstrikeHud.inputStep++;
                        } else if (upNotPressed) {
                            NapalmAirstrikeHud.resetInputValues();
                        }
                    }
                    case 1 -> {
                        if (rightJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmAirstrikeHud.secondInputDown = true;
                            NapalmAirstrikeHud.inputStep++;
                        } else if (rightNotPressed) {
                            NapalmAirstrikeHud.resetInputValues();
                        }
                    }
                    case 2 -> {
                        if (downJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            NapalmAirstrikeHud.thirdInputDown = true;
                            NapalmAirstrikeHud.inputStep++;
                        } else if (downNotPressed) {
                            NapalmAirstrikeHud.resetInputValues();
                        }
                    }
                    case 3 -> {
                        if (upJustPressed) {
                            player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                            player.playSound(ModSounds.STRATAGEM_ACTIVATE.get(), 0.5f, 1f);
                            NapalmAirstrikeHud.fourthInputDown = true;
                            NapalmAirstrikeHud.allInputsDown = true;
                            NapalmAirstrikeHud.inputStep++;
                        } else if (upNotPressed) {
                            NapalmAirstrikeHud.resetInputValues();
                        }
                    }
                }
            }


        } else {
            resetInputValues();
        }

        if (HellbombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Hellbomb"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())), 9999));
            resetInputValues();
        }
        if (ResupplyHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Resupply"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.RESUPPLY.get().getDefaultInstance())), 3600));
            resetInputValues();
        }
        if (EAT17Hud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Expendable Anti-Tank"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())), 1400));
            resetInputValues();
        }
        if (StalwartHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Stalwart"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.STALWART_STRATAGEM.get().getDefaultInstance())), 9600));
            resetInputValues();
        }
        if (AmrHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Anti-Materiel Rifle"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.AMR.get().getDefaultInstance())), 9600));
            resetInputValues();
        }
        if (PrecisionStrikeHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Precision Strike"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.PRECISION_STRIKE.get().getDefaultInstance())), 1800));
            resetInputValues();
        }
        if (SmallBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital 120MM HE Barrage"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.SMALL_BARRAGE.get().getDefaultInstance())), 3600));
            resetInputValues();
        }
        if (BigBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital 380MM HE Barrage"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.BIG_BARRAGE.get().getDefaultInstance())), 4800));
            resetInputValues();
        }
        if (OrbitalLaserHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Laser"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.ORBITAL_LASER.get().getDefaultInstance())), 6000));
            resetInputValues();
        }
        if (NapalmBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Napalm Barrage"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.NAPALM_BARRAGE.get().getDefaultInstance())), 4800));
            resetInputValues();
        }
        if (WalkingBarrageHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Orbital Walking Barrage"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.WALKING_BARRAGE.get().getDefaultInstance())), 4800));
            resetInputValues();
        }
        if (Eagle500KgBombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Eagle 500KG Bomb"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())), 3000));
            resetInputValues();
        }
        if (ClusterBombHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Eagle Cluster Bomb"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.CLUSTER_BOMB.get().getDefaultInstance())), 3000));
            resetInputValues();
        }
        if (EagleAirstrikeHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Eagle Airstrike"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance())), 3000));
            resetInputValues();
        }
        if (NapalmAirstrikeHud.allInputsDown) {
            PacketHandler.sendToServer(new SGiveStratagemOrbPacket("Eagle Napalm Airstrike"));
            PacketHandler.sendToServer(new SItemGiveCooldownPacket(ClientItemCache.getItem(
                    ClientItemCache.getSlotWithItem(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance())), 3000));
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

        // only render the menu if your not moving, all inputs aren't down, your hand is empty, you have a helldiver chestplate on
        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem) {

            // render the background
            guiGraphics.blit(StratagemHudOverlay.STRATAGEM_BACKGROUND,
                    6, 6, 132 , 125, 0, 0, 16, 16,
                    16, 16);

            // OTHER

            // Hellbomb Render HUD Code
            if (ClientItemCache.contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                HellbombHud.renderHellbombHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                HellbombHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            }

            // SUPPORT

            // Resupply Render HUD Code
            if (ClientItemCache.contains(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance())) {
                ResupplyHud.renderResupplyHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.RESUPPLY.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance())) {
                ResupplyHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.RESUPPLY.get().getDefaultInstance()));
            }

            // EAT Render HUD Code
            if (ClientItemCache.contains(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())) {
                EAT17Hud.renderEAT17Hud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())) {
                EAT17Hud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()));
            }

            // Stalwart Render HUD Code
            if (ClientItemCache.contains(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.STALWART_STRATAGEM.get().getDefaultInstance())) {
                StalwartHud.renderStalwartHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.STALWART_STRATAGEM.get().getDefaultInstance())) {
                StalwartHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()));
            }

            // Stalwart Render HUD Code
            if (ClientItemCache.contains(ModItems.AMR_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.AMR_STRATAGEM.get().getDefaultInstance())) {
                AmrHud.renderAmrHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.AMR_STRATAGEM.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.AMR_STRATAGEM.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.AMR_STRATAGEM.get().getDefaultInstance())) {
                AmrHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.AMR_STRATAGEM.get().getDefaultInstance()));
            }

            // ORBITAL

            // Precision Strike HUD Render Code
            if (ClientItemCache.contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderPrecisionStrikeHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            }

            // 120 Barrage Render HUD Code
            if (ClientItemCache.contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                SmallBarrageHud.renderSmallBarrageHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                SmallBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            }

            // 380 Barrage Render HUD Code
            if (ClientItemCache.contains(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                BigBarrageHud.renderBigBarrageHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                BigBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            }

            // Orbital Laser Render HUD Code
            if (ClientItemCache.contains(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance())) {
                OrbitalLaserHud.renderOrbitalLaserHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.ORBITAL_LASER.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance())) {
                OrbitalLaserHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.ORBITAL_LASER.get().getDefaultInstance()));
            }

            // Napalm Barrage Render HUD Code
            if (ClientItemCache.contains(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.NAPALM_BARRAGE.get().getDefaultInstance())) {
                NapalmBarrageHud.renderNapalmBarrageHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.NAPALM_BARRAGE.get().getDefaultInstance())) {
                NapalmBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()));
            }

            // Napalm Barrage Render HUD Code
            if (ClientItemCache.contains(ModItems.WALKING_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.WALKING_BARRAGE.get().getDefaultInstance())) {
                WalkingBarrageHud.renderWalkingBarrageHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.WALKING_BARRAGE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.WALKING_BARRAGE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.WALKING_BARRAGE.get().getDefaultInstance())) {
                WalkingBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.WALKING_BARRAGE.get().getDefaultInstance()));
            }

            // EAGLES

            // 500 KG Render HUD Code
            if (ClientItemCache.contains(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())) {
                Eagle500KgBombHud.render500KgBombHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())) {
                Eagle500KgBombHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()));
            }

            // Cluster Bomb Render HUD Code
            if (ClientItemCache.contains(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance())) {
                ClusterBombHud.renderClusterBombHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.CLUSTER_BOMB.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance())) {
                ClusterBombHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.CLUSTER_BOMB.get().getDefaultInstance()));
            }

            // Eagle Airstrike Render HUD Code
            if (ClientItemCache.contains(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance())) {
                EagleAirstrikeHud.renderEagleAirstrikeHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance())) {
                EagleAirstrikeHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()));
            }

            // Eagle Airstrike Render HUD Code
            if (ClientItemCache.contains(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.isOnCooldown(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance())) {
                NapalmAirstrikeHud.renderNapalmAirstrikeHud(guiGraphics, ClientItemCache.getSlotWithItem(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()));
            } // Render the cooldown hud
            else if (ClientItemCache.contains(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()) &&
                    !ClientItemCache.isOnCooldown(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance())) {
                NapalmAirstrikeHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()));
            }
        }

        // render the popups in the top left if the stratagem is almost done cooling down
        if (!allInputsDown &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IHelldiverArmorItem) {
            // OTHER

            // Hellbomb Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.HELLBOMB_ITEM.get().getDefaultInstance())) {
                HellbombHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.HELLBOMB_ITEM.get().getDefaultInstance()));
            }

            // SUPPORT

            // Resupply Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.RESUPPLY.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.RESUPPLY.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.RESUPPLY.get().getDefaultInstance())) {
                ResupplyHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.RESUPPLY.get().getDefaultInstance()));
            }

            // EAT Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance())) {
                EAT17Hud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.ANTI_TANK_STRATAGEM.get().getDefaultInstance()));
            }

            // EAT Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.STALWART_STRATAGEM.get().getDefaultInstance())) {
                StalwartHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.STALWART_STRATAGEM.get().getDefaultInstance()));
            }

            // EAT Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.AMR_STRATAGEM.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.AMR_STRATAGEM.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.AMR_STRATAGEM.get().getDefaultInstance())) {
                AmrHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.AMR_STRATAGEM.get().getDefaultInstance()));
            }

            // ORBITAL

            if (!ClientItemCache.isOnCooldown(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.PRECISION_STRIKE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.PRECISION_STRIKE.get().getDefaultInstance())) {
                PrecisionStrikeHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.PRECISION_STRIKE.get().getDefaultInstance()));
            }

            // 120 Barrage Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.SMALL_BARRAGE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.SMALL_BARRAGE.get().getDefaultInstance())) {
                SmallBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.SMALL_BARRAGE.get().getDefaultInstance()));
            }

            // 380 Barrage Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.BIG_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.BIG_BARRAGE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.BIG_BARRAGE.get().getDefaultInstance())) {
                BigBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.BIG_BARRAGE.get().getDefaultInstance()));
            }

            // Orbital Laser Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.ORBITAL_LASER.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.ORBITAL_LASER.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.ORBITAL_LASER.get().getDefaultInstance())) {
                OrbitalLaserHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.ORBITAL_LASER.get().getDefaultInstance()));
            }

            // Napalm Barrage Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.NAPALM_BARRAGE.get().getDefaultInstance())) {
                NapalmBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.NAPALM_BARRAGE.get().getDefaultInstance()));
            }

            // Walking Barrage Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.WALKING_BARRAGE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.WALKING_BARRAGE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.WALKING_BARRAGE.get().getDefaultInstance())) {
                WalkingBarrageHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.WALKING_BARRAGE.get().getDefaultInstance()));
            }

            // EAGLES

            // 500 KG Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance())) {
                Eagle500KgBombHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.EAGLE_500KG_BOMB.get().getDefaultInstance()));
            }

            // Cluster Bomb Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.CLUSTER_BOMB.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.CLUSTER_BOMB.get().getDefaultInstance())) {
                ClusterBombHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.CLUSTER_BOMB.get().getDefaultInstance()));
            }

            // Eagle Airstrike Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance())) {
                EagleAirstrikeHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.EAGLE_AIRSTRIKE.get().getDefaultInstance()));
            }

            // Napalm Airstrike Cooldown Complete Popup Code
            if (!ClientItemCache.isOnCooldown(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()) &&
                    ClientItemCache.getCooldownLeft(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()) <= 5 &&
                    ClientItemCache.contains(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance())) {
                NapalmAirstrikeHud.renderCooldownHud(guiGraphics, ClientItemCache.getCooldownLeft(ModItems.NAPALM_AIRSTRIKE.get().getDefaultInstance()));
            }
        }
    }

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        // Stop the player from moving when the stratagem menu is open
        Player player = Minecraft.getInstance().player;
        if (KeyBinding.SHOW_STRATAGEM_KEY.isDown() && player.getDeltaMovement().x == 0
                && player.getDeltaMovement().z == 0 && !allInputsDown &&
                player.getMainHandItem().isEmpty()) {
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
        NapalmBarrageHud.resetInputValues();
        WalkingBarrageHud.resetInputValues();
        EagleAirstrikeHud.resetInputValues();
        NapalmAirstrikeHud.resetInputValues();
        StalwartHud.resetInputValues();
        AmrHud.resetInputValues();
        allInputsDown = false;
    }
}