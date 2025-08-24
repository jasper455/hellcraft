package net.team.helldivers.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.P2Renderer;
import net.team.helldivers.sound.ModSounds;

public class P2Item extends AbstractGunItem {

    public P2Item(Properties properties) {
        super(properties.durability(2).rarity(Rarity.COMMON), 5f,  false, true, "§e[Side-Arm]", 1, 3, 0.04,  ModSounds.P2_SHOOT, ModSounds.AR_23_RELOAD);
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new P2Renderer();
    }
}