package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ClientItemCache {
    private static final Map<Integer, ItemStack> slotCache = new HashMap<>();

    public static ItemStack getItem(int slot) {
        return slotCache.get(slot);
    }

    public static void addToSlotCache(int slot, ItemStack itemStack) {
        slotCache.put(slot, itemStack);
    }

    public static void removeFromSlotCache(int slot, ItemStack itemStack) {
        slotCache.replace(slot, itemStack, new ItemStack(Items.AIR));
    }

    public static boolean contains(ItemStack stack) {
        for (int i = 0; i < 4; i++) {
            ItemStack cached = slotCache.get(i);
            if (cached != null && cached.is(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    public static int getSlotWithItem(ItemStack stack) {
        for (int i = 0; i < 4; i++) {
            ItemStack cached = slotCache.get(i);
            if (cached != null && cached.is(stack.getItem())) {
                return i;
            }
        }
        return -1;
    }
    public static boolean isOnCooldown(ItemStack stack) {
        return !Minecraft.getInstance().player.getCooldowns().isOnCooldown(stack.getItem());
    }

    public static int getCooldownLeft(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return (int) (player.getCooldowns().getCooldownPercent(stack.getItem(), 20) * 100);
    }

}