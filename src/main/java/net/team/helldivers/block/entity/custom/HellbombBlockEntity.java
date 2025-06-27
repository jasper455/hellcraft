package net.team.helldivers.block.entity.custom;

import net.team.helldivers.block.custom.HellbombBlock;
import net.team.helldivers.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HellbombBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

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
}
