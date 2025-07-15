package net.team.helldivers.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.helper.HellbombCombinations;
import net.team.helldivers.helper.HellbombEntityCombinations;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SHellbombActivatePacket;
import net.team.helldivers.network.SHellbombExplodePacket;
import net.team.helldivers.util.KeyBinding;


public class HellbombEntityInputScreen extends AbstractContainerScreen<HellbombEntityInputMenu> {
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

    public HellbombEntityInputScreen(HellbombEntityInputMenu menu, Inventory inventory, Component title) {
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

        HellbombHellpodEntity hellbombEntity = this.menu.hellbombEntity;

        if (hellbombEntity.randomCode == 1) {
            HellbombCombinations.combo1render(guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }
        if (hellbombEntity.randomCode == 2) {
            HellbombCombinations.combo2render(guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }
        if (hellbombEntity.randomCode == 3) {
            HellbombCombinations.combo3render(guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }
        if (hellbombEntity.randomCode == 4) {
            HellbombCombinations.combo4render(guiGraphics, firstInputDown, secondInputDown,
                    thirdInputDown, fourthInputDown, fifthInputDown, sixthInputDown);
        }


        if (allInputsDown) {
            Minecraft.getInstance().player.closeContainer();
            menu.hellbombEntity.setActivated(true);
            PacketHandler.sendToServer(new SHellbombExplodePacket(hellbombEntity.getOnPos()));
            PacketHandler.sendToServer(new SHellbombActivatePacket(hellbombEntity.getId(), true));
            resetInputValues();
        }
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        HellbombHellpodEntity hellbombEntity = this.menu.hellbombEntity;
        boolean upPressed = KeyBinding.UP_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean downPressed = KeyBinding.DOWN_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean leftPressed = KeyBinding.LEFT_INPUT_KEY.matches(pKeyCode, pScanCode);
        boolean rightPressed = KeyBinding.RIGHT_INPUT_KEY.matches(pKeyCode, pScanCode);

        boolean upNotPressed = downPressed || leftPressed || rightPressed;
        boolean downNotPressed = upPressed || leftPressed || rightPressed;
        boolean leftNotPressed = upPressed || downPressed || rightPressed;
        boolean rightNotPressed = upPressed || downPressed || leftPressed;

        if (hellbombEntity.randomCode == 1) {
            HellbombEntityCombinations.combo1Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
        }
        if (hellbombEntity.randomCode == 2) {
            HellbombEntityCombinations.combo2Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
        }
        if (hellbombEntity.randomCode == 3) {
            HellbombEntityCombinations.combo3Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
        }
        if (hellbombEntity.randomCode == 4) {
            HellbombEntityCombinations.combo4Inputs(this, upPressed, downPressed, leftPressed, rightPressed);
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