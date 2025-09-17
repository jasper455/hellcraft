package net.team.helldivers.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.team.helldivers.entity.custom.BulletProjectileEntity;
import net.team.helldivers.particle.ModParticles;
import net.team.helldivers.util.KeyBinding;

public class EffectTesterItem extends Item {

    public EffectTesterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
            for (int i = 0; i < 15; i++) {
                pLevel.addParticle(ModParticles.SHRAPNEL.get(), pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 1,
                        0, 0);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return super.onLeftClickEntity(stack, player, entity);
    }

    private ItemStack getFirstShulkerBoxWithTag(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof BlockItem blockItem &&
                    blockItem.getBlock() instanceof ShulkerBoxBlock &&
                    stack.hasTag() &&
                    stack.getTag().contains("BlockEntityTag")) {
                return stack;
            }
        }
        return new ItemStack(Items.DIAMOND);
    }
}