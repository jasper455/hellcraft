package net.team.helldivers.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.team.helldivers.util.KeyBinding;

public class EffectTesterItem extends Item {

    public EffectTesterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (level.isClientSide()) {
            return;
        }
        player.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            ItemStack item = handler.getStackInSlot(0);
            if (item.isEmpty() && KeyBinding.EQUIP_BACKPACK.consumeClick()) {
                handler.insertItem(0, new ItemStack(stack.getItem(), 64), false);
                return;
            }
            if (!item.isEmpty() && KeyBinding.AIM.consumeClick()) {
                handler.extractItem(0, 64, false);
                return;
            }
            if (item.isEmpty() && KeyBinding.SHOOT.consumeClick()) {
                player.sendSystemMessage(Component.literal("slot is empty"));
            }
            if (!item.isEmpty() && KeyBinding.SHOOT.consumeClick()) {
                player.sendSystemMessage(Component.literal("slot is not empty"));
            }
        });
    }
}