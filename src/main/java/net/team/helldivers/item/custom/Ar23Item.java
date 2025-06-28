package net.team.helldivers.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.team.helldivers.client.renderer.item.AR23Renderer;
import software.bernie.example.client.renderer.item.JackInTheBoxRenderer;
import software.bernie.example.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public final class Ar23Item extends Item implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public Ar23Item(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private AR23Renderer renderer;

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if (this.renderer == null)
					this.renderer = new AR23Renderer();
				return this.renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(new AnimationController<>(this, "controller", 0, state -> {
			// You can add your animation logic here
			return PlayState.CONTINUE;
		}));
	}


	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}
}
