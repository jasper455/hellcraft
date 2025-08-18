package net.team.helldivers.item.custom.guns;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.client.renderer.item.FLAM40Renderer;
import net.team.helldivers.entity.custom.FlameBulletEntity;
import net.team.helldivers.network.CFlamesParticlePacket;
import net.team.helldivers.network.CStopSoundPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.sound.custom.MovingSoundInstance;
import net.team.helldivers.util.KeyBinding;
import net.team.helldivers.util.ShootHelper;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks = 201;
    private MovingSoundInstance flamethrowerSound;
    private MovingSoundInstance flamethrowerStartSound;
    private MovingSoundInstance flamethrowerStopSound;

    public FLAM40Item(Properties properties) {
        super(properties.durability(100).rarity(Rarity.COMMON), true, true, "§e[Flamethrower]", 1, ModSounds.AR_23_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        super.onShoot(itemStack, player);
        if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
            if (flamethrowerSound != null && flamethrowerStartSound != null) {
                PacketHandler.sendToAllClients(new CStopSoundPacket(flamethrowerSound.getLocation()));
                PacketHandler.sendToAllClients(new CStopSoundPacket(flamethrowerStartSound.getLocation()));
            }
            return;
        };
        PacketHandler.sendToPlayer(new CFlamesParticlePacket(), player);
        FlameBulletEntity flame = new FlameBulletEntity(player, player.level());
        flame.setPos(player.getEyePosition().add(0, -0.3, 0));
        flame.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 0.7f, 7f);
        player.level().addFreshEntity(flame);
        if (!player.isCreative()) {
            itemStack.hurt(1, player.level().getRandom().fork(), player);
        }
        if (!pilotLightLit) {
            this.flamethrowerStartSound = new MovingSoundInstance(player, ModSounds.FLAMETHROWER_START_SHOOTING.get(), 1.25f,  false);
            Minecraft.getInstance().getSoundManager()
                    .play(flamethrowerStartSound);
            pilotLightLit = true;
        }
        if (litTicks <= 0 || litTicks == 200 || litTicks == 201) {
            this.flamethrowerSound = new MovingSoundInstance(player, ModSounds.FLAMETHROWER_SHOOT.get(), 1.25f, true);
            Minecraft.getInstance().getSoundManager()
                    .play(flamethrowerSound);
            litTicks = 200;
        }
        litTicks--;
    }

    @Override
    public void onEndShoot(ItemStack itemStack, ServerPlayer player) {
        super.onEndShoot(itemStack, player);
        if (flamethrowerSound != null && flamethrowerStartSound != null) {
            PacketHandler.sendToAllClients(new CStopSoundPacket(flamethrowerSound.getLocation()));
            PacketHandler.sendToAllClients(new CStopSoundPacket(flamethrowerStartSound.getLocation()));
        }
        litTicks = 201;
        pilotLightLit = false;
        if (!(itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1)) {
            this.flamethrowerStopSound = new MovingSoundInstance(player, ModSounds.FLAMETHROWER_STOP_SHOOTING.get(), 1.25f,  false);
            Minecraft.getInstance().getSoundManager()
                    .play(flamethrowerStopSound);
        }
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new FLAM40Renderer();
    }
}