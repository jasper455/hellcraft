package net.team.helldivers.screen.custom;

import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.inventory.StratagemPickerInventory;
import net.team.helldivers.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StratagemPickerMenu extends AbstractContainerMenu {
    private final StratagemPickerInventory inventory;
    private final ItemStack stack;

    // Constructor for server-side
    public StratagemPickerMenu(int containerId, Inventory playerInventory, ItemStack stack) {
        super(ModMenuTypes.STRATAGEM_PICKER.get(), containerId);
        this.stack = stack;
        this.inventory = new StratagemPickerInventory(stack, StratagemPickerInventory.INVENTORY_SIZE);

        // Add the stratagem inventory slots
        for (int i = 0; i < StratagemPickerInventory.INVENTORY_SIZE; i++) {
            this.addSlot(new Slot(inventory, i, 44 + i * 34, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return inventory.isValidItem(stack);
                }
            });
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    // Constructor for client-side
    public StratagemPickerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.STRATAGEM_PICKER.get(), containerId);
        this.stack = extraData.readItem();
        this.inventory = new StratagemPickerInventory(this.stack, StratagemPickerInventory.INVENTORY_SIZE);

        // Add the stratagem inventory slots
        for (int i = 0; i < StratagemPickerInventory.INVENTORY_SIZE; i++) {
            this.addSlot(new Slot(inventory, i, 44 + i * 24, 35));
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }



    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 4) {
                // If the item is in the hellpod inventory, try to move it to player inventory
                if (!this.moveItemStackTo(itemstack1, 4, 4 + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // If the item is in the player inventory, try to move it to hellpod inventory
                if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        boolean hasStratagemPicker = player.getMainHandItem().is(ModItems.STRATAGEM_PICKER.get()) ||
                player.getOffhandItem().is(ModItems.STRATAGEM_PICKER.get());

        return hasStratagemPicker && !stack.isEmpty();
    }


    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        // Only allow stratagem items to be placed in the stratagem slots
        if (slot.container == inventory) {
            return inventory.isValidItem(stack);
        }
        return super.canTakeItemForPickAll(stack, slot);
    }
}
