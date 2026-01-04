package net.team.helldivers.item.custom.backpacks;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.client.renderer.item.PortableHellbombRenderer;
import net.team.helldivers.client.renderer.item.ShieldPackRenderer;
import net.team.helldivers.helper.DelayedPortableHellbombExplosion;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class ShieldPackItem extends AbstractBackpackItem {
    public ShieldPackItem(Properties properties) {
        super(properties.durability(20));
    }

    @Override
    public void onUse(Player player) {}

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new ShieldPackRenderer();
    }
}
