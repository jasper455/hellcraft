package net.team.helldivers.item.custom.guns;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.client.renderer.item.FLAM40Renderer;
import net.team.helldivers.entity.custom.FlameBulletEntity;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.particle.EffekLoader;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.util.ShootHelper;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks;
    public FLAM40Item(Properties properties) {
        super(properties.durability(100).rarity(Rarity.COMMON), true, true, "§e[Flamethrower]", 1, ModSounds.AR_23_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        super.onShoot(itemStack, player);
        float rotY = (float) Math.toRadians(player.getYRot());
        float rotX = (float) Math.toRadians(player.getXRot());
        Vec2 dir = new Vec2(rotX, -rotY);
        ParticleEmitterInfo fire = EffekLoader.FIRE.clone().position(player.getEyePosition().add(0, -0.1, 0)).rotation(dir);
        AAALevel.addParticle(player.level(), true, fire);
        System.out.println("rotx " + rotX);
        System.out.println("roty " + rotY);
        FlameBulletEntity flame = new FlameBulletEntity(player, player.level());
        flame.setPos(player.getEyePosition().add(0, -0.3, 0));
        flame.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 0.5f, 7f);
        player.level().addFreshEntity(flame);
        if (!player.isCreative()) {
            itemStack.hurt(1, player.level().getRandom().fork(), player);
        }
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new FLAM40Renderer();
    }

}