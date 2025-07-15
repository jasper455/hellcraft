package net.team.helldivers.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.team.helldivers.block.entity.custom.HellbombBlockEntity;
import net.team.helldivers.entity.custom.HellbombHellpodEntity;
import net.team.helldivers.screen.ModMenuTypes;

public class HellbombEntityInputMenu extends AbstractContainerMenu {
    public final HellbombHellpodEntity hellbombEntity;

    public HellbombEntityInputMenu(int containerId, Inventory inventory, HellbombHellpodEntity entity) {
        super(ModMenuTypes.HELLBOMB_ENTITY_INPUT_MENU.get(), containerId);
        this.hellbombEntity = entity;
    }

    // Add the default constructor for the menu type registration
    public HellbombEntityInputMenu(int containerId, Inventory inventory) {
        super(ModMenuTypes.HELLBOMB_INPUT_MENU.get(), containerId);
        this.hellbombEntity = null;
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
