package net.team.helldivers.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.entity.custom.FlameBulletEntity;
import net.team.helldivers.network.CFlamesParticlePacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.util.ShootHelper;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks;
    public FLAM40Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), true, true, "§e[Flamethrower]", 1, ModSounds.AMR_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        System.out.println("shot");
        PacketHandler.sendToPlayer(new CFlamesParticlePacket(), player);
        FlameBulletEntity flame = new FlameBulletEntity(player, player.level());
        flame.setPos(player.getEyePosition().add(0, -0.3, 0));
        flame.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 0.5f, 7f);
        player.level().addFreshEntity(flame);
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new AmrRenderer();
    }

}