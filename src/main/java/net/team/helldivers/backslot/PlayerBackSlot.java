package net.team.helldivers.backslot;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

public class PlayerBackSlot {
    // 1-slot inventory for the back slot
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // If you need to trigger sync, you can do it here
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1; // only 1 item allowed
        }
    };

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void copyFrom(PlayerBackSlot source) {
        // deep copy the inventory
        CompoundTag temp = source.inventory.serializeNBT();
        this.inventory.deserializeNBT(temp);
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.put("BackSlot", inventory.serializeNBT());
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("BackSlot")) {
            inventory.deserializeNBT(nbt.getCompound("BackSlot"));
        }
    }
}