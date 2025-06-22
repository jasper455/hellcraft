package net.infinite1274.helldivers.sound.custom;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class FallingShellSoundInstance extends AbstractTickableSoundInstance {
    private final Entity missile;

    public FallingShellSoundInstance(Entity missile, SoundEvent sound) {
        super(sound, SoundSource.AMBIENT, RandomSource.create()); // Or use SoundSource.NEUTRAL
        this.missile = missile;
        this.looping = true;
        this.volume = 5.0F;
        this.pitch = 1.0F;
        this.x = missile.getX();
        this.y = missile.getY();
        this.z = missile.getZ();
    }
    @Override
    public void tick() {
        // Update position
        this.x = missile.getX();
        this.y = missile.getY();
        this.z = missile.getZ();

        // Stop if missile is removed or dead
        if (!missile.isAlive() || missile.isRemoved()) {
            this.stop();
        }
    }

    @Override
    public Attenuation getAttenuation() {
        return Attenuation.LINEAR; // Default, but you can also use NONE for global
    }

}