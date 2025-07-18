package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ClientItemCache {
    private static final Map<Integer, ItemStack> slotCache = new HashMap<>();

    public static void setSlot(int slot, ItemStack stack) {
        slotCache.put(slot, stack);
    }

    public static ItemStack getSlot(int slot) {
        return slotCache.getOrDefault(slot, ItemStack.EMPTY);
    }

    public static int getSlotWithItem(ItemStack stack) {
        for (int i = 0; i < slotCache.size(); i++) {
            if (slotCache.get(i) == stack) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isOnCooldown(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return player.getCooldowns().isOnCooldown(getSlot(getSlotWithItem(stack)).getItem());
    }

    public static int getCooldownLeft(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return (int) (player.getCooldowns().getCooldownPercent(getSlot(getSlotWithItem(stack)).getItem(), 1) * 100);
    }

    public static boolean contains(ItemStack stack) {
        for (int i = 0; i < slotCache.size(); i++) {
            ItemStack itemStack = getSlot(i);
            if (itemStack.is(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        slotCache.clear();
    }
}