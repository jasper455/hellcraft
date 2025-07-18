package net.team.helldivers.helper;

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

    public static void clear() {
        slotCache.clear();
    }
}