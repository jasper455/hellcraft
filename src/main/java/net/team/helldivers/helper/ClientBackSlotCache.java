package net.team.helldivers.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ClientBackSlotCache {
    private static final Map<Integer, ItemStack> slotCache = new HashMap<>();

    public static ItemStack getItem() {
        return slotCache.get(0);
    }

    public static void addToSlotCache(ItemStack itemStack) {
        slotCache.replace(0, itemStack);
    }
    public static void removeFromSlotCache(ItemStack itemStack) {
        slotCache.replace(0, itemStack, new ItemStack(Items.AIR));
    }
    public static boolean contains(ItemStack stack) {
        ItemStack cached = slotCache.get(0);
        return cached != null && cached.is(stack.getItem());
    }
    public static boolean isOnCooldown(ItemStack stack) {
        return !Minecraft.getInstance().player.getCooldowns().isOnCooldown(stack.getItem());
    }
    public static int getCooldownLeft(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return (int) (player.getCooldowns().getCooldownPercent(stack.getItem(), 20) * 100);
    }
}