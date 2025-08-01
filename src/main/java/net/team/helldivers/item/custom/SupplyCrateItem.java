package net.team.helldivers.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.team.helldivers.block.ModBlocks;
import net.team.helldivers.item.ModItems;

public class SupplyCrateItem extends Item {

    public SupplyCrateItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if (!pLevel.isClientSide) {
            ItemStack ammoCrate = new ItemStack(ModBlocks.AMMO_CRATE.get().asItem()); // Change to your item
            ammoCrate.setCount(3);

            boolean added = pPlayer.getInventory().add(ammoCrate);
            pPlayer.getItemInHand(pUsedHand).shrink(1);

            if (!added) {
                pPlayer.drop(ammoCrate, false); // false = no random offset
            }
        }

        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
    }
}