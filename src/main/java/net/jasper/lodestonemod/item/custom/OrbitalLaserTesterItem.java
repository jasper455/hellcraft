package net.jasper.lodestonemod.item.custom;


import net.jasper.lodestonemod.LodestoneMod;
import net.jasper.lodestonemod.client.shader.post.tint.TintPostProcessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import net.jasper.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;

@Mod.EventBusSubscriber(modid = LodestoneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OrbitalLaserTesterItem extends Item {
    private static float flashTime = 0;
    private static boolean isFlashed = false;

    public OrbitalLaserTesterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Vec3 pos = pContext.getClickLocation();
        Player player = pContext.getPlayer();
//            triggerLaserEffect(pContext.getLevel(), player.position(), player);
        fireOrbitalLaser(pContext.getLevel(), pos);
        isFlashed = true;
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private void fireOrbitalLaser(Level level, Vec3 targetPos) {
        Vec3 from = new Vec3(targetPos.x, level.getMaxBuildHeight(), targetPos.z);
        // Phase 1: Targeting Beam
        new VFXBuilders.WorldVFXBuilder()
                .setColor(new Color(255, 0, 0))
                .setAlpha(0.6f)
                .renderBeam(new Matrix4f(), from, targetPos, 0.1f);
    }

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (isFlashed) {
            flashTime++;
        }

        if (flashTime >= 500) {
            isFlashed = false;
            flashTime = 0;
            TintPostProcessor.INSTANCE.setActive(false);
        }
    }

}
