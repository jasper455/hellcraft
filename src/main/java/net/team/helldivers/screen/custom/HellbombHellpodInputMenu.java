package net.team.helldivers.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.team.helldivers.screen.ModMenuTypes;

public class HellbombHellpodInputMenu extends AbstractContainerMenu {
    public final HellbombBlockEntity hellbombBlockEntity;
    public HellbombHellpodInputMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public HellbombHellpodInputMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.HELLBOMB_INPUT_MENU.get(), pContainerId);
        hellbombBlockEntity = ((HellbombBlockEntity) blockEntity);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
