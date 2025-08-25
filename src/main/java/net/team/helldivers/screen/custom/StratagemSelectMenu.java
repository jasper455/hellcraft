package net.team.helldivers.screen.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.team.helldivers.block.entity.custom.GalacticTerminalBlockEntity;
import net.team.helldivers.item.custom.IStratagemItem;
import net.team.helldivers.screen.ModMenuTypes;

public class StratagemSelectMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final Player player;
    private final BlockPos pos;

    public StratagemSelectMenu(int containerId, Inventory playerInventory, Container inventory, BlockPos pos) {
        super(ModMenuTypes.EXTRACTION_TERMINAL.get(), containerId);
        this.inventory = inventory;
        this.player = playerInventory.player;
        this.pos = pos;

        // Add the stratagem inventory slots
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(inventory, i, 44 + i * 24, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !contains(stack) && stack.getItem() instanceof IStratagemItem;
                }
            });
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    // Client-side constructor
    public StratagemSelectMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(4), buf.readBlockPos());

        // Add the stratagem inventory slots
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(inventory, i, 44 + i * 24, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !contains(stack) && stack.getItem() instanceof IStratagemItem;
                }
            });
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
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
    public boolean stillValid(Player player) {
        return true;
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
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            BlockEntity blockEntity = player.level().getBlockEntity(this.pos);
            if (blockEntity instanceof GalacticTerminalBlockEntity GalacticTerminal) {
                GalacticTerminal.savePlayerInventory(player, this.inventory);
            }
        }
    }

    public boolean contains(ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (itemStack.is(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    public int getSlotWithItem(ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (itemStack.is(stack.getItem())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isOnCooldown(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return !player.getCooldowns().isOnCooldown(this.inventory.getItem(getSlotWithItem(stack)).getItem());
    }

    public int getCooldownLeft(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        return (int) (player.getCooldowns().getCooldownPercent(stack.getItem(), 1) * 100);
    }

}
