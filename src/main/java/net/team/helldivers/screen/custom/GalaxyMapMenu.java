package net.team.helldivers.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.team.helldivers.block.entity.custom.GalacticTerminalBlockEntity;
import net.team.helldivers.screen.ModMenuTypes;

public class GalaxyMapMenu extends AbstractContainerMenu {
    public final GalacticTerminalBlockEntity galacticTerminalBlockEntity;
    public GalaxyMapMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public GalaxyMapMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.GALAXY_MAP_MENU.get(), pContainerId);
        galacticTerminalBlockEntity = ((GalacticTerminalBlockEntity) blockEntity);
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
