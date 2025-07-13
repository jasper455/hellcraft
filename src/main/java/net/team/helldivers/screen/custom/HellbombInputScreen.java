package net.team.helldivers.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.team.helldivers.client.hud.StratagemHudOverlay;
import net.team.helldivers.helper.DelayedExplosion;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SHellbombExplodePacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;

import static net.team.helldivers.block.custom.HellbombBlock.isActivated;

public class HellbombInputScreen extends AbstractContainerScreen<HellbombInputMenu> {
    public boolean firstInputDown = false;
    public boolean secondInputDown = false;
    public boolean thirdInputDown = false;
    public boolean fourthInputDown = false;
    public boolean fifthInputDown = false;
    public boolean sixthInputDown = false;
    public int inputStep;
    public boolean allInputsDown = false;

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HelldiversMod.MOD_ID, "textures/gui/hellbomb_input/hellbomb_input.png");

    public HellbombInputScreen(HellbombInputMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelX = 1000;
        this.inventoryLabelY = 1000;
        this.titleLabelX = 1000;
        this.titleLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (int) ((width - 350) / 2);
        guiGraphics.blit(GUI_TEXTURE,
                x, 20, 512, 512, 0, 0, 256, 256,
                256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        renderTooltip(guiGraphics, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, delta);

        HellbombBlockEntity hellbombBlockEntity = this.menu.hellbombBlockEntity;
        // hellbombBlockEntity != null is only here for organization, you can minimize the if statements by pressing "ctrl" + "-"
        // each one just renders a random arrow 20 units further to the right than the last
        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input1 == 1) {
                StratagemHudOverlay.renderUpArrow(guiGraphics, 260, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, firstInputDown);
            } else if (hellbombBlockEntity.input1 == 2) {
                StratagemHudOverlay.renderDownArrow(guiGraphics, 260, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, firstInputDown);
            } else if (hellbombBlockEntity.input1 == 3) {
                StratagemHudOverlay.renderLeftArrow(guiGraphics, 260, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, firstInputDown);
            } else if (hellbombBlockEntity.input1 == 4) {
                StratagemHudOverlay.renderRightArrow(guiGraphics, 260, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, firstInputDown);
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input2 == 1) {
                StratagemHudOverlay.renderUpArrow(guiGraphics, 280, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, secondInputDown);
            } else if (hellbombBlockEntity.input2 == 2) {
                StratagemHudOverlay.renderDownArrow(guiGraphics, 280, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, secondInputDown);
            } else if (hellbombBlockEntity.input2 == 3) {
                StratagemHudOverlay.renderLeftArrow(guiGraphics, 280, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, secondInputDown);
            } else if (hellbombBlockEntity.input2 == 4) {
                StratagemHudOverlay.renderRightArrow(guiGraphics, 280, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, secondInputDown);
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input3 == 1) {
                StratagemHudOverlay.renderUpArrow(guiGraphics, 300, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, thirdInputDown);
            } else if (hellbombBlockEntity.input3 == 2) {
                StratagemHudOverlay.renderDownArrow(guiGraphics, 300, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, thirdInputDown);
            } else if (hellbombBlockEntity.input3 == 3) {
                StratagemHudOverlay.renderLeftArrow(guiGraphics, 300, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, thirdInputDown);
            } else if (hellbombBlockEntity.input3 == 4) {
                StratagemHudOverlay.renderRightArrow(guiGraphics, 300, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, thirdInputDown);
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input4 == 1) {
                StratagemHudOverlay.renderUpArrow(guiGraphics, 320, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fourthInputDown);
            }
            else if (hellbombBlockEntity.input4 == 2) {
                StratagemHudOverlay.renderDownArrow(guiGraphics, 320, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fourthInputDown);
            }
            else if (hellbombBlockEntity.input4 == 3) {
                StratagemHudOverlay.renderLeftArrow(guiGraphics, 320, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fourthInputDown);
            }
            else if (hellbombBlockEntity.input4 == 4) {
                StratagemHudOverlay.renderRightArrow(guiGraphics, 320, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fourthInputDown);
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input5 == 1) {
                StratagemHudOverlay.renderUpArrow(guiGraphics, 340, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fifthInputDown);
            }
            else if (hellbombBlockEntity.input5 == 2) {
                StratagemHudOverlay.renderDownArrow(guiGraphics, 340, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fifthInputDown);
            }
            else if (hellbombBlockEntity.input5 == 3) {
                StratagemHudOverlay.renderLeftArrow(guiGraphics, 340, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fifthInputDown);
            }
            else if (hellbombBlockEntity.input5 == 4) {
                StratagemHudOverlay.renderRightArrow(guiGraphics, 340, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, fifthInputDown);
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input6 == 1) {
                StratagemHudOverlay.renderUpArrow(guiGraphics, 360, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, sixthInputDown);
            }
            else if (hellbombBlockEntity.input6 == 2) {
                StratagemHudOverlay.renderDownArrow(guiGraphics, 360, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, sixthInputDown);
            }
            else if (hellbombBlockEntity.input6 == 3) {
                StratagemHudOverlay.renderLeftArrow(guiGraphics, 360, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, sixthInputDown);
            }
            else if (hellbombBlockEntity.input6 == 4) {
                StratagemHudOverlay.renderRightArrow(guiGraphics, 360, 162, 20, 20,
                        0, 0, 16, 16, 16, 16, sixthInputDown);
            }
        }

        if (allInputsDown) {
            Minecraft.getInstance().player.closeContainer();
            hellbombBlockEntity.getLevel().setBlockAndUpdate(
                    hellbombBlockEntity.getBlockPos(), hellbombBlockEntity.getBlockState().setValue(isActivated, true));
            PacketHandler.sendToServer(new SHellbombExplodePacket(hellbombBlockEntity.getBlockPos()));
            resetInputValues();
        }
    }

    // TODO: Fix issue with multiple inputs being pressed at once

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        boolean upPressed = KeyBinding.UP_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean downPressed = KeyBinding.DOWN_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean leftPressed = KeyBinding.LEFT_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean rightPressed = KeyBinding.RIGHT_INPUT_KEY.matches(pKeyCode, pScanCode);

        boolean upNotPressed = downPressed || leftPressed || rightPressed;
        boolean downNotPressed = upPressed || leftPressed || rightPressed;
        boolean leftNotPressed = upPressed || downPressed || rightPressed;
        boolean rightNotPressed = upPressed || downPressed || leftPressed;

        HellbombBlockEntity hellbombBlockEntity = this.menu.hellbombBlockEntity;

        Player player = Minecraft.getInstance().player;

        // Same thing here, only checking if it's not null for organization

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input1 == 1) {
                if (upPressed && inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    firstInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input1 == 2) {
                if (downPressed && inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    firstInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input1 == 3) {
                if (leftPressed && inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    firstInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input1 == 4) {
                if (rightPressed && inputStep == 0) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    firstInputDown = true;
                    inputStep++;
                }

            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input2 == 1) {
                if (upPressed && inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    secondInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input2 == 2) {
                if (downPressed && inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    secondInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input2 == 3) {
                if (leftPressed && inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    secondInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input2 == 4) {
                if (rightPressed && inputStep == 1) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    secondInputDown = true;
                    inputStep++;
                }
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input3 == 1) {
                if (upPressed && inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    thirdInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input3 == 2) {
                if (downPressed && inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    thirdInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input3 == 3) {
                if (leftPressed && inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    thirdInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input3 == 4) {
                if (rightPressed && inputStep == 2) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    thirdInputDown = true;
                    inputStep++;
                }
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input4 == 1) {
                if (upPressed && inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fourthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input4 == 2) {
                if (downPressed && inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fourthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input4 == 3) {
                if (leftPressed && inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fourthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input4 == 4) {
                if (rightPressed && inputStep == 3) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fourthInputDown = true;
                    inputStep++;
                }

            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input5 == 1) {
                if (upPressed && inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fifthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input5 == 2) {
                if (downPressed && inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fifthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input5 == 3) {
                if (leftPressed && inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fifthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input5 == 4) {
                if (rightPressed && inputStep == 4) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    fifthInputDown = true;
                    inputStep++;
                }
            }
        }

        if (hellbombBlockEntity != null) {
            if (hellbombBlockEntity.input6 == 1) {
                if (upPressed && inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    sixthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input6 == 2) {
                if (downPressed && inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    sixthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input6 == 3) {
                if (leftPressed && inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    sixthInputDown = true;
                    inputStep++;
                }

            } else if (hellbombBlockEntity.input6 == 4) {
                if (rightPressed && inputStep == 5) {
                    player.playSound(ModSounds.STRATAGEM_INPUT.get(), 0.5f, 1f);
                    sixthInputDown = true;
                    allInputsDown = true;
                    inputStep++;
                }
            }
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }


    private void resetInputValues() {
        firstInputDown = false;
        secondInputDown = false;
        thirdInputDown = false;
        fourthInputDown = false;
        fifthInputDown = false;
        sixthInputDown = false;
        allInputsDown = false;
        inputStep = 0;
    }
}