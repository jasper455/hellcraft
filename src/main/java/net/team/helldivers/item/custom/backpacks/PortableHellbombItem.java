package net.team.helldivers.item.custom.backpacks;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.team.helldivers.client.renderer.item.JumpPackRenderer;
import net.team.helldivers.client.renderer.item.PortableHellbombRenderer;
import net.team.helldivers.helper.DelayedExplosion;

public class PortableHellbombItem extends AbstractBackpackItem {
    public PortableHellbombItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUse(Player player) {
        MinecraftForge.EVENT_BUS.register(new DelayedExplosion(player.level(), player.blockPosition(), 25, 300));
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new PortableHellbombRenderer();
    }

}
