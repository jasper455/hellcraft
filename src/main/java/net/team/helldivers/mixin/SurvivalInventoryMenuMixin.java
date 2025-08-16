package net.team.helldivers.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.team.helldivers.backslot.PlayerBackSlot;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class SurvivalInventoryMenuMixin extends AbstractContainerMenu {

    protected SurvivalInventoryMenuMixin(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Inventory pPlayerInventory, boolean pActive, Player player, CallbackInfo ci) {
        // Avoid injecting into creative screen

        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
            if (player.isCreative()) return;
            this.addSlot(new SlotItemHandler(backSlot.getInventory(), 0, 77, 44) {
                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            });
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stackInSlot = slot.getItem();
        ItemStack copy = stackInSlot.copy();

        int backSlotIndex = this.slots.size() - 1; // usually last slot added
        int playerInventoryStart = 0 /* usually 0 */;
        int playerInventoryEnd = backSlotIndex /* usually 36 or so */;

        if (index == backSlotIndex) {
            // Move item from back slot to player inventory
            if (!moveItemStackTo(stackInSlot, playerInventoryStart, playerInventoryEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Move item from player inventory to back slot if allowed
            if (true) {
                if (!moveItemStackTo(stackInSlot, backSlotIndex, backSlotIndex + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (stackInSlot.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stackInSlot.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stackInSlot);
        return copy;
    }
}
