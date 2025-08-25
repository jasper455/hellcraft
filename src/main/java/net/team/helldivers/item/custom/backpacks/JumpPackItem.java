package net.team.helldivers.item.custom.backpacks;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;
import net.team.helldivers.HelldiversMod;
import net.team.helldivers.backslot.PlayerBackSlot;
import net.team.helldivers.backslot.PlayerBackSlotProvider;
import net.team.helldivers.client.renderer.item.JumpPackRenderer;
import net.team.helldivers.damage.ModDamageSources;
import net.team.helldivers.damage.ModDamageTypes;


@Mod.EventBusSubscriber(modid = HelldiversMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JumpPackItem extends AbstractBackpackItem {
    public JumpPackItem(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityWithoutLevelRenderer createRenderer() {
        return new JumpPackRenderer();
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if(pEntity instanceof Player player){
             player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
                ItemStackHandler handler = backSlot.getInventory();
                ItemStack backItem = handler.getStackInSlot(0);
                if (!backItem.isEmpty() && backItem.getItem() instanceof JumpPackItem) {
                   MobEffectInstance slowFall = new MobEffectInstance(MobEffects.SLOW_FALLING, 20);
                   player.addEffect(slowFall);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            jump(player);
//            System.out.println("jump");
        }
    }

    @Override
    public void onUse(Player player) {
        jump(player);
    }

    private static void jump(Player player){
        player.getCapability(PlayerBackSlotProvider.PLAYER_BACK_SLOT).ifPresent(backSlot -> {
        ItemStackHandler handler = backSlot.getInventory();
        ItemStack backItem = handler.getStackInSlot(0);
            if (!backItem.isEmpty() && backItem.getItem() instanceof JumpPackItem pack && !player.getCooldowns().isOnCooldown(pack))  {
                Vec3 look = player.getLookAngle();
                player.addDeltaMovement(new Vec3(look.x * 0.5, 1.0, look.z * 0.5));
                player.getCooldowns().addCooldown(pack, 260);
                MobEffectInstance slowFall = new MobEffectInstance(MobEffects.SLOW_FALLING, 60);
                player.addEffect(slowFall);
//                System.out.print(pack);
            }
            });
//        System.out.println("jump");
    }
}
