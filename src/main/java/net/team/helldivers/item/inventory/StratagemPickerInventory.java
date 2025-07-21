package net.team.helldivers.item.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.team.helldivers.item.custom.IStratagemItem;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.systems.container.ItemInventory;

public class StratagemPickerInventory extends ItemInventory {
    public static final int INVENTORY_SIZE = 4;
    public StratagemPickerInventory(ItemStack stack, int expectedSize) {
        super(stack, expectedSize);
    }

    public boolean isValidItem(ItemStack stack) {
        return stack.getItem() instanceof IStratagemItem;
    }

    public boolean contains(ItemStack stack) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack itemStack = getItem(i);
            if (itemStack.is(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    public int getSlotWithItem(ItemStack stack) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack itemStack = getItem(i);
            if (itemStack.is(stack.getItem())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isOnCooldown(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return !player.getCooldowns().isOnCooldown(this.getItem(getSlotWithItem(stack)).getItem());
    }

    public int getCooldownLeft(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return (int) (player.getCooldowns().getCooldownPercent(stack.getItem(), 20) * 100);
    }
}
