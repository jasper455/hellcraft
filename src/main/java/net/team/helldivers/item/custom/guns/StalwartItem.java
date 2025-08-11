package net.team.helldivers.item.custom.guns;

import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.StalwartRenderer;
import net.team.helldivers.sound.ModSounds;

public class StalwartItem extends AbstractGunItem {

    public StalwartItem(Properties properties) {
        super(properties.durability(202).rarity(Rarity.COMMON), 2.5f, true, true, "§e[Machine-Gun]", 1, 2, 0.05,  new StalwartRenderer(),  ModSounds.STALWART_SHOOT, ModSounds.AR_23_RELOAD);
    }  
}