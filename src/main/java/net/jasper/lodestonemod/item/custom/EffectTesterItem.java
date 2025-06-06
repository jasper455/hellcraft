package net.jasper.lodestonemod.item.custom;

import net.jasper.lodestonemod.client.shader.post.multi.glow.GlowPostProcessor;
import net.jasper.lodestonemod.client.shader.post.multi.glow.LightingFx;
import net.jasper.lodestonemod.client.shader.post.tint.TintPostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.mixin.client.CameraMixin;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.systems.fireeffect.FireEffectType;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.screen.LodestoneScreenParticleType;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;

public class EffectTesterItem extends Item {
    public EffectTesterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Vec3 pos = pContext.getClickLocation();
//        for (int i = 0; i < 100; i++) {
//            spawnExampleParticles(pContext.getLevel(), pos);
//        }
//        triggerExplosionEffect(pContext.getLevel(), pos);
        Vector3f center = new Vector3f(0, 0, 0);
        Vector3f color = new Vector3f(1, 0, 1);
        GlowPostProcessor.INSTANCE.addFxInstance(new LightingFx(center, color));
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        triggerScreenShake(0.5f, 200);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void triggerExplosionEffect(Level level, Vec3 pos) {
        RandomSource random = level.getRandom();

        // Smoke
        for (int i = 0; i < 30; i++) {
            float angle = random.nextFloat() * ((float) Math.PI * 2);
            float speed = 0.1f + random.nextFloat() * 0.2f;
            float dx = Mth.cos(angle) * speed;
            float dz = Mth.sin(angle) * speed;
            float dy = 0.05f + random.nextFloat() * 0.1f;

            float scaleStart = 1.5f + random.nextFloat() * 1.5f;
            float scaleEnd = 0f;

            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                    .setScaleData(GenericParticleData.create(scaleStart, scaleEnd)
                            .setEasing(Easing.EXPO_OUT).build())
                    .setTransparencyData(GenericParticleData.create(0.4f, 0f)
                            .setEasing(Easing.QUAD_OUT).build())
                    .setColorData(ColorParticleData.create(new Color(30, 30, 30), new Color(10, 10, 10)).build())
                    .setLifetime(60 + random.nextInt(20))
                    .addMotion(dx, dy, dz)
                    .enableNoClip()
                    .spawn(level, pos.x, pos.y, pos.z);
        }

        // Flash particle
        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(5f, 2.5f).build())
                .setTransparencyData(GenericParticleData.create(0.8f, 1f).build())
                .setColorData(ColorParticleData.create(new Color(255, 216, 20), new Color(255, 237, 165)).build())
                .setLifetime(100)
                .addMotion(Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector())
                .spawn(level, pos.x, pos.y, pos.z);

        TintPostProcessor.INSTANCE.setActive(false);


        // Screen shake (client only)
//        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(600)
//                .setEasing(Easing.SINE_IN_OUT)
//                .setIntensity(1.5f));
    }
}
