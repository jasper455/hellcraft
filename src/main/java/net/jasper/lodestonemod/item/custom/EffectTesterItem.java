package net.jasper.lodestonemod.item.custom;


import net.jasper.lodestonemod.LodestoneMod;
import net.jasper.lodestonemod.client.shader.post.tint.TintPostProcessor;
import net.jasper.lodestonemod.entity.custom.ExplosiveProjectileEntity;
import net.jasper.lodestonemod.event.ModClientEvents;
import net.jasper.lodestonemod.network.PacketHandler;
import net.jasper.lodestonemod.network.SSphereExplosionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;

@Mod.EventBusSubscriber(modid = LodestoneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EffectTesterItem extends Item {
    private static float flashTime = 0;
    private static boolean isFlashed = false;

    public EffectTesterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Vec3 pos = pContext.getClickLocation();

            ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(300)
                    .setEasing(Easing.SINE_IN_OUT)
                    .setIntensity(0.5f));
            triggerExplosionEffect(pContext.getLevel(), pContext.getClickLocation());
//        TintPostProcessor.INSTANCE.setActive(true);
        isFlashed = true;
//        PacketHandler.sendToServer(new SSphereExplosionPacket(pContext.getClickedPos(), 90));
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ExplosiveProjectileEntity explosive = new ExplosiveProjectileEntity(pPlayer, pLevel);
//        explosive.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0, 1.5f, 0);
        explosive.setNoGravity(true);
        pLevel.addFreshEntity(explosive);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void triggerExplosionEffect(Level level, Vec3 pos) {
        RandomSource random = level.getRandom();

        // Smoke
        for (int i = 0; i < 30; i++) {
            float angle = random.nextFloat() * ((float) Math.PI * 2);
            float speed = 0.1f + random.nextFloat() * 0.05f;
            float dx = Mth.cos(angle) * speed;
            float dz = Mth.sin(angle) * speed;
            float dy = 0.05f + random.nextFloat() * 0.1f;
//            for (int j = 0; j < 100; j++) {
//                WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
//                        .setScaleData(GenericParticleData.create(5f, 20f).build())
//                        .setTransparencyData(GenericParticleData.create(0.5f, 0f).build())
//                        .setColorData(ColorParticleData.create(
//                                new Color(83, 64, 48),
//                                new Color(83, 64, 48)).build())
//                        .setLifetime(700)
//                        .addMotion(dx / 5, dy / 1.5, dz / 4)
//                        .enableNoClip()
//                        .spawn(level, pos.x, pos.y - 10, pos.z);
//            }

            // Blast particle outer
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(50f, 100f).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 216, 20), new Color(255, 70, 45, 200)).build())
                    .addMotion(dx / 20, dy / 4, dz / 20)
                    .setLifetime(600)
                    .spawn(level, pos.x, pos.y - 10, pos.z);
            // Blast particle inner
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(25f, 50f).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                    .setLifetime(600)
                    .addMotion(dx / 25, dy / 5, dz / 25)
                    .spawn(level, pos.x, pos.y - 10, pos.z);

            // Flash Particle
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(30f, 175f).build())
                    .setTransparencyData(GenericParticleData.create(0.05f, 0.02f).build())
                    .setColorData(ColorParticleData.create(new Color(255, 146, 22), new Color(255, 39, 39)).build())
                    .setLifetime(600)
                    .addMotion(dx * 2, dy / 5, dz * 2)
                    .spawn(level, pos.x, pos.y - 10, pos.z);

        }
        // Light Flash Particle (yk what I mean)
        WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
                .setScaleData(GenericParticleData.create(250f, 2.5f).build())
                .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                .setColorData(ColorParticleData.create(new Color(255, 224, 20), new Color(255, 237, 165)).build())
                .setLifetime(40)
                .addMotion(0, 0, 0)
                .spawn(level, pos.x, pos.y - 10, pos.z);

        ModClientEvents.triggerFlashEffect(0.00005f);
        TintPostProcessor.INSTANCE.setActive(false);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(level.isClientSide())));
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
