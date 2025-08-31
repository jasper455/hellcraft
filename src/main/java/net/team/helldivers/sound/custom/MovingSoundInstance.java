package net.team.helldivers.sound.custom;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class MovingSoundInstance extends AbstractTickableSoundInstance {
    private final Entity entity;

    public MovingSoundInstance(Entity entity, SoundEvent sound, float volume, boolean looping) {
        super(sound, SoundSource.AMBIENT, RandomSource.create()); // Or use SoundSource.NEUTRAL
        this.entity = entity;
        this.looping = looping;
        this.volume = volume;
        this.pitch = 1.0F;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }
    @Override
    public void tick() {
        // Update position
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();

        // Stop if missile is removed or dead
        if (!entity.isAlive() || entity.isRemoved()) {
            this.stop();
        }
    }

    @Override
    public Attenuation getAttenuation() {
        return Attenuation.LINEAR; // Default, but you can also use NONE for global
    }

    public void stopSound() {
//        entity.sendSystemMessage(Component.literal("test"));
        this.stop();
    }
}