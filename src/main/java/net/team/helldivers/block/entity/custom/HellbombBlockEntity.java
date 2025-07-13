package net.team.helldivers.block.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.team.helldivers.block.custom.HellbombBlock;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.team.helldivers.helper.DelayedExplosion;
import net.team.helldivers.screen.custom.HellbombInputMenu;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HellbombBlockEntity extends BlockEntity implements GeoBlockEntity, Container, MenuProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final int input1 = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);
    public final int input2 = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);
    public final int input3 = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);
    public final int input4 = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);
    public final int input5 = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);
    public final int input6 = Mth.randomBetweenInclusive(RandomSource.create(), 1, 4);

    // We statically instantiate our RawAnimations for efficiency, consistency, and error-proofing
    private static final RawAnimation HELLBOMB_ANIMS = RawAnimation.begin().thenPlay("hellbomb.activate").thenLoop("hellbomb.active_idle");
    private static final RawAnimation HELLBOMB_IDLE = RawAnimation.begin().thenPlay("hellbomb.idle");

    public HellbombBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.HELLBOMB.get(), pPos, pBlockState);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (this.getBlockState().getValue(HellbombBlock.isActivated)) {
                return state.setAndContinue(HELLBOMB_ANIMS);
            }
            else {
                return state.setAndContinue(HELLBOMB_IDLE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return null;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {}

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {}

    @Override
    public Component getDisplayName() {
        return Component.literal("Hellbomb");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new HellbombInputMenu(pContainerId, pPlayerInventory, this);
    }
}
