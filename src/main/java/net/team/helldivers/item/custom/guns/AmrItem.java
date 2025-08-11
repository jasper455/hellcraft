package net.team.helldivers.item.custom.guns;

import net.minecraft.world.item.Rarity;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.sound.ModSounds;

public class AmrItem extends AbstractGunItem {
    public AmrItem(Properties properties) {
        super(properties.durability(8).rarity(Rarity.COMMON), 10f, false,true, "§e[Sniper-Rifle]", 25, 10, 0, new AmrRenderer(),  ModSounds.AMR_SHOOT, ModSounds.AMR_RELOAD);
    }
   
}
