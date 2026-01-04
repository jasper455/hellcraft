package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.entity.custom.PortableHellbombEntity;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.backpacks.PortableHellbombItem;
import net.team.helldivers.network.CLargeExplosionParticlesPacket;
import net.team.helldivers.network.PacketHandler;
import net.team.helldivers.network.SSphereExplosionPacket;
import net.team.helldivers.sound.ModSounds;
import net.team.helldivers.sound.custom.MovingSoundInstance;

public class DelayedPortableHellbombExplosion {
    private MovingSoundInstance hellbombArmedSound;
    private MovingSoundInstance clearTheAreaSound;
    private final Level level;
    private BlockPos pos;
    private final int radius;
    private int ticksRemaining;
    private boolean hasPlayedSound = false; // Add this field
    private Entity entity;

    public DelayedPortableHellbombExplosion(Level level, BlockPos pos, int radius, int delayTicks, Entity entity, boolean shouldPlaySound) {
        this.level = level;
        this.pos = pos;
        this.radius = radius;
        this.ticksRemaining = delayTicks;
        this.entity = entity;
        this.hasPlayedSound = !shouldPlaySound;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !this.level.isClientSide()) {
            entity.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
                ItemStackHandler handler = backSlot.getInventory();
                ItemStack backSlotItem = handler.getStackInSlot(0);

                if (backSlotItem.isEmpty() || !backSlotItem.is(ModItems.PORTABLE_HELLBOMB.get())) {
                    // spawn the hellbomb entity
                    PortableHellbombEntity portableHellbomb = new PortableHellbombEntity((LivingEntity) entity, entity.level());

                    entity.level().addFreshEntity(portableHellbomb);

                    portableHellbomb.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0f, 0.5f, 1.0f);
                    this.hellbombArmedSound.setSourceEntity(portableHellbomb);
                    MinecraftForge.EVENT_BUS.register(new DelayedPortableHellbombExplosion(portableHellbomb.level(), portableHellbomb.blockPosition(),
                            15, ticksRemaining, portableHellbomb, false));
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            });
            if (!hasPlayedSound) {
                this.hellbombArmedSound = new MovingSoundInstance(entity, ModSounds.HELLBOMB_ARMED.get(), 10.0f,  false);
                this.clearTheAreaSound = new MovingSoundInstance(entity, ModSounds.CLEAR_THE_AREA.get(), 10000000.0f,  false);
                Minecraft.getInstance().getSoundManager()
                        .play(hellbombArmedSound);
                Minecraft.getInstance().getSoundManager()
                        .play(clearTheAreaSound);
                hasPlayedSound = true;
            }

            this.pos = entity.blockPosition();
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                PacketHandler.sendToServer(new SSphereExplosionPacket(pos, radius));
                PacketHandler.sendToAllClients(new CLargeExplosionParticlesPacket(pos));
                level.playSound(null, pos, ModSounds.HELLBOMB_EXPLOSION.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                entity.kill();
                this.level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(radius)).forEach(entity -> {
                    if (entity.getItem().getItem() instanceof PortableHellbombItem) {
                        entity.kill();
                    }
                });
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}