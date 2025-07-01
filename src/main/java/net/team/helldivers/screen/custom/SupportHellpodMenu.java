package net.team.helldivers.screen.custom;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.team.helldivers.entity.custom.SupportHellpodEntity;
import org.jetbrains.annotations.Nullable;
import net.team.helldivers.screen.ModMenuTypes;

public class SupportHellpodMenu extends AbstractContainerMenu {
    private final Container hellpodInventory;
    private final SupportHellpodEntity hellpodEntity;  // Add this field
    private static final int HELLPOD_SLOTS = 2;
    private static final int INVENTORY_SLOTS = 36; // Player inventory slots (9*4)

    // Constructor for client side
    public SupportHellpodMenu(int containerId, Inventory playerInventory, SupportHellpodEntity hellpodEntity) {
        this(containerId, playerInventory, new SimpleContainer(HELLPOD_SLOTS), hellpodEntity);
    }

    // Constructor for server side
    public SupportHellpodMenu(int containerId, Inventory playerInventory, Container hellpodInventory, SupportHellpodEntity hellpodEntity) {
        super(ModMenuTypes.SUPPORT_HELLPOD_MENU.get(), containerId);
        this.hellpodInventory = hellpodInventory;
        this.hellpodEntity = hellpodEntity;

        // Add hellpod inventory slots
        addHellpodSlots();
        
        // Add player inventory slots
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (hellpodEntity != null) {
            hellpodEntity.onInventoryClosed();
        }
    }

    private void addHellpodSlots() {
        // Add the two hellpod slots side by side
        addSlot(new Slot(hellpodInventory, 0, 80, 14));
        addSlot(new Slot(hellpodInventory, 1, 80, 40));
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
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < HELLPOD_SLOTS) {
                // If the item is in the hellpod inventory, try to move it to player inventory
                if (!this.moveItemStackTo(itemstack1, HELLPOD_SLOTS, HELLPOD_SLOTS + INVENTORY_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // If the item is in the player inventory, try to move it to hellpod inventory
                if (!this.moveItemStackTo(itemstack1, 0, HELLPOD_SLOTS, false)) {
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
        return hellpodInventory.stillValid(player);
    }
}