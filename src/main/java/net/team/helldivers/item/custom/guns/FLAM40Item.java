package net.team.helldivers.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.team.helldivers.client.renderer.item.AmrRenderer;
import net.team.helldivers.sound.ModSounds;

public class FLAM40Item extends AbstractGunItem {
    public boolean pilotLightLit;
    public int litTicks;

    public FLAM40Item(Properties properties) {
        super(properties.durability(47).rarity(Rarity.COMMON), true, true, "§e[Flamethrower]", ModSounds.AMR_RELOAD);//TODO temp renderer and sounds
    }
    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        if(selected) System.out.println(pilotLightLit);
        if(litTicks > 260){
            pilotLightLit = false;
        }
        if(pilotLightLit){
            litTicks++;
        }
    }
    @Override
    public void onShoot(ItemStack itemStack, ServerPlayer player) {
        if(itemStack.getDamageValue() < itemStack.getMaxDamage() - 1 && !player.getCooldowns().isOnCooldown(itemStack.getItem())){
            if(pilotLightLit){
                //Flames.spawnFlames(player);
                System.out.println("lit");
            }
                else{
                pilotLightLit = true;
                player.getCooldowns().addCooldown(itemStack.getItem(), 5);
                System.out.println("lighting");
            }
        }
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new AmrRenderer();
    }

}