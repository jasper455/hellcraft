package net.team.helldivers.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.team.helldivers.entity.custom.FragGrenadeEntity;
import net.team.helldivers.entity.custom.StratagemOrbEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FragGrenadeItem extends Item {
    public FragGrenadeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeCharged;
            if (!pLevel.isClientSide()) {
                FragGrenadeEntity grenade = new FragGrenadeEntity(player, pLevel);
                float power = Math.min(i / 20F, 1.5F); // Max power after 1 second charge
                grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 1.5F, 0F);
                pLevel.addFreshEntity(grenade);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                pStack.shrink(1);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(Component.literal("§e[Throwable]"));
        super.appendHoverText(pStack, pLevel, list, pIsAdvanced);
    }
}