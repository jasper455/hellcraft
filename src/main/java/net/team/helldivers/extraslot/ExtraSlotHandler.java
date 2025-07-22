package net.team.helldivers.extraslot;

import net.minecraftforge.items.ItemStackHandler;

public class ExtraSlotHandler extends ItemStackHandler {

    public ExtraSlotHandler() {
        super(1); // Only one extra slot
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        // You can sync to client here if needed
    }
}