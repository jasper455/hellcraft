package net.infinite1274.helldivers.item.custom;

import net.infinite1274.helldivers.entity.custom.StratagemOrbEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StratagemOrbItem extends Item {
    public StratagemOrbItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {
            StratagemOrbEntity stratagemOrb = new StratagemOrbEntity(pPlayer, pLevel);
            stratagemOrb.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0f, 1f, 0f);
            stratagemOrb.stratagemType = pPlayer.getMainHandItem().getTag().toString();
            pLevel.addFreshEntity(stratagemOrb);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (!getStratagemType(pStack).isEmpty()) {
            pTooltipComponents.add(Component.literal(getStratagemType(pStack)));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static String getStratagemType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("stratagemType") ? tag.getString("stratagemType") : "";
    }
}
