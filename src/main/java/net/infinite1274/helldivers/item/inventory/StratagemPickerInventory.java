package net.infinite1274.helldivers.item.inventory;

import net.infinite1274.helldivers.item.custom.IStratagemItem;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.systems.container.ItemInventory;

import java.util.ArrayList;
import java.util.List;

public class StratagemPickerInventory extends ItemInventory {
    public static final int INVENTORY_SIZE = 4;
    public StratagemPickerInventory(ItemStack stack, int expectedSize) {
        super(stack, expectedSize);
    }

    public boolean isStratagemItem(ItemStack stack) {
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
}
