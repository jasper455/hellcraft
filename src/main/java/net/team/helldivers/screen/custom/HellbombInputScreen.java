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
import net.team.helldivers.helper.HellbombCombinations;
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

        int x = (int) ((Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2);
        int y = (int) ((Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2) + 20;
        guiGraphics.blit(GUI_TEXTURE,
                x, y, 512, 512, 0, 0, 256, 256,
                256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        renderTooltip(guiGraphics, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, delta);

        HellbombBlockEntity hellbombBlockEntity = this.menu.hellbombBlockEntity;

        int x = (int) ((Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2);
        int y = (int) ((Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2) + 20;

        if (hellbombBlockEntity.randomCode == 1) {
            HellbombCombinations.combo1render(x, y, guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }
        if (hellbombBlockEntity.randomCode == 2) {
            HellbombCombinations.combo2render(x, y, guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }
        if (hellbombBlockEntity.randomCode == 3) {
            HellbombCombinations.combo3render(x, y, guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }
        if (hellbombBlockEntity.randomCode == 4) {
            HellbombCombinations.combo4render(x, y, guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }


        if (allInputsDown) {
            Minecraft.getInstance().player.closeContainer();
            hellbombBlockEntity.getLevel().setBlockAndUpdate(
                    hellbombBlockEntity.getBlockPos(), hellbombBlockEntity.getBlockState().setValue(isActivated, true));
            PacketHandler.sendToServer(new SHellbombExplodePacket(hellbombBlockEntity.getBlockPos()));
            resetInputValues();
        }
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        HellbombBlockEntity hellbombBlockEntity = this.menu.hellbombBlockEntity;
        boolean upPressed = KeyBinding.UP_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean downPressed = KeyBinding.DOWN_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean leftPressed = KeyBinding.LEFT_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean rightPressed = KeyBinding.RIGHT_INPUT_KEY.matches(pKeyCode, pScanCode);

        boolean upNotPressed = downPressed || leftPressed || rightPressed;
        boolean downNotPressed = upPressed || leftPressed || rightPressed;
        boolean leftNotPressed = upPressed || downPressed || rightPressed;
        boolean rightNotPressed = upPressed || downPressed || leftPressed;

        if (hellbombBlockEntity.randomCode == 1) {
            HellbombCombinations.combo1Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
        }
        if (hellbombBlockEntity.randomCode == 2) {
            HellbombCombinations.combo2Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
        }
        if (hellbombBlockEntity.randomCode == 3) {
            HellbombCombinations.combo3Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
        }
        if (hellbombBlockEntity.randomCode == 4) {
            HellbombCombinations.combo4Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
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