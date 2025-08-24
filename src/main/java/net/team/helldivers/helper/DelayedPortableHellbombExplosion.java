package net.team.helldivers.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.item.custom.backpacks.AbstractBackpackItem;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;
import net.team.helldivers.network.CLargeExplosionParticlesPacket;
import net.team.helldivers.network.CSyncBackSlotPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SSphereExplosionPacket;
import net.team.helldivers.sound.ModSounds;

public class DelayedPortableHellbombExplosion {
    private final Level level;
    private BlockPos pos;
    private final int radius;
    private int ticksRemaining;
    private boolean hasPlayedSound = false; // Add this field
    private Player player;

    public DelayedPortableHellbombExplosion(Level level, BlockPos pos, int radius, int delayTicks, Player player) {
        this.level = level;
        this.pos = pos;
        this.radius = radius;
        this.ticksRemaining = delayTicks;
        this.player = player;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !this.level.isClientSide()) {
            if (!hasPlayedSound) {
                level.playSound(null, pos, ModSounds.HELLBOMB_ARMED.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                level.playSound(null, pos, ModSounds.CLEAR_THE_AREA.get(), SoundSource.PLAYERS, 10000000.0f, 1.0f);
                hasPlayedSound = true;
            }

            this.pos = player.blockPosition();
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                PacketHandler.sendToServer(new SSphereExplosionPacket(pos, radius));
                PacketHandler.sendToAllClients(new CLargeExplosionParticlesPacket(pos));
                level.playSound(null, pos, ModSounds.HELLBOMB_EXPLOSION.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                player.kill();
                this.level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(radius)).forEach(entity -> {
                    if (entity.getItem().getItem() instanceof PortableHellbombItem) {
                        entity.kill();
                    }
                });
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    if (player.getInventory().getItem(i).getItem() instanceof PortableHellbombItem) {
                        player.getInventory().getItem(i).setCount(0);
                    }
                }
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}