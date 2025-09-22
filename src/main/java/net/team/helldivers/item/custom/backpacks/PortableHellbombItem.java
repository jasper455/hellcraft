package net.team.helldivers.item.custom.backpacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.team.helldivers.block.custom.ExtractionTerminalBlock;
import net.team.helldivers.client.renderer.item.JumpPackRenderer;
import net.team.helldivers.client.renderer.item.PortableHellbombRenderer;
import net.team.helldivers.helper.Delay;
import net.team.helldivers.helper.DelayedExplosion;
import net.team.helldivers.helper.DelayedPortableHellbombExplosion;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.STeleportToDimensionPacket;
import net.team.helldivers.worldgen.dimension.ModDimensions;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PortableHellbombItem extends AbstractBackpackItem {
    public static final RawAnimation ACTIVATE = RawAnimation.begin().thenPlay("activate");
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlayAndHold("idle");

    private boolean isActivated;

    public PortableHellbombItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void onUse(Player player) {
        if (!this.isActivated) {
            MinecraftForge.EVENT_BUS.register(new DelayedPortableHellbombExplosion(player.level(), player.blockPosition(),
                    15, 300, player));
            this.isActivated = true;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<?> activateController = new AnimationController<>(this, "activate", 0,
                state -> state.setAndContinue(this.isActivated ? ACTIVATE : IDLE));

        controllers.add(activateController);

    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new PortableHellbombRenderer();
    }

    public boolean isActivated() {
        return this.isActivated;
    }

    public void setActivated(boolean activated) {
        this.isActivated = activated;
    }
}
