package net.team.helldivers.screen.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.team.helldivers.block.entity.custom.ExtractionTerminalBlockEntity;
import net.team.helldivers.item.ModItems;
import net.team.helldivers.item.custom.IStratagemItem;
import net.team.helldivers.screen.ModMenuTypes;

public class ExtractionTerminalMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final Player player;
    private final BlockPos pos;
    private final ItemStack menuStack; // Add this to store the stratagem NBT

    public ExtractionTerminalMenu(int containerId, Inventory playerInventory, Container inventory, BlockPos pos) {
        super(ModMenuTypes.EXTRACTION_TERMINAL.get(), containerId);
        this.inventory = inventory;
        this.player = playerInventory.player;
        this.pos = pos;

        // Create menu stack with inventory data
        this.menuStack = new ItemStack(ModItems.STRATAGEM_PICKER.get());
        CompoundTag tag = menuStack.getOrCreateTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                listTag.add(itemTag);
            }
        }
        tag.put("Items", listTag);

        // Add the stratagem inventory slots
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(inventory, i, 44 + i * 24, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof IStratagemItem;
                }

                @Override
                public void setChanged() {
                    super.setChanged();
                    updateMenuStack();
                }
            });
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private void updateMenuStack() {
        CompoundTag tag = menuStack.getOrCreateTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                listTag.add(itemTag);
            }
        }
        tag.put("Items", listTag);
    }

    public ItemStack getMenuStack() {
        return menuStack;
    }



    // Client-side constructor
    public ExtractionTerminalMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(4), buf.readBlockPos());

        // Add the stratagem inventory slots
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(inventory, i, 44 + i * 24, 35));
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
            if (blockEntity instanceof ExtractionTerminalBlockEntity extractionTerminal) {
                extractionTerminal.savePlayerInventory(player, this.inventory);
            }
        }
    }

}
