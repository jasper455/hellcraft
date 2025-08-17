package net.team.helldivers.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.AR23Renderer;
import net.team.helldivers.sound.ModSounds;

public class Ar23Item extends AbstractGunItem {

    public Ar23Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), 2f, true,  true, "§e[Assault-Rifle]", 2, 3, 0.02, ModSounds.AR_23_SHOOT, ModSounds.AR_23_RELOAD);
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new AR23Renderer();
    }
}
